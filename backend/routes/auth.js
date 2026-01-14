const express = require('express');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const pool = require('../config/db');
const { sendOTP } = require('../config/email');
const router = express.Router();

const generateToken = (user) => jwt.sign(
  { id: user.id, email: user.email, role: user.role },
  process.env.JWT_SECRET,
  { expiresIn: process.env.JWT_EXPIRES_IN }
);

const generateRefreshToken = (user) => jwt.sign(
  { id: user.id },
  process.env.JWT_REFRESH_SECRET,
  { expiresIn: process.env.JWT_REFRESH_EXPIRES_IN }
);

router.post('/signup', async (req, res) => {
  const { email, password, name, role = 'student' } = req.body;
  if (!email || !password || !name) {
    return res.status(400).json({ error: 'Email, password, and name are required' });
  }
  try {
    const hashedPassword = await bcrypt.hash(password, 10);
    const result = await pool.query(
      'INSERT INTO users (email, password, name, role) VALUES ($1, $2, $3, $4) RETURNING id, email, name, role',
      [email, hashedPassword, name, role]
    );
    res.status(201).json({ user: result.rows[0] });
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
});

router.post('/login', async (req, res) => {
  const { email, password } = req.body;
  try {
    const result = await pool.query('SELECT * FROM users WHERE email = $1', [email]);
    const user = result.rows[0];
    if (!user || !(await bcrypt.compare(password, user.password))) {
      return res.status(401).json({ error: 'Invalid credentials' });
    }
    const token = generateToken(user);
    const refreshToken = generateRefreshToken(user);
    await pool.query(
      'INSERT INTO refresh_tokens (user_id, token) VALUES ($1, $2)',
      [user.id, refreshToken]
    );
    res.json({ token, refreshToken, user: { id: user.id, email: user.email, name: user.name, role: user.role } });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

router.post('/refresh', async (req, res) => {
  const { refreshToken } = req.body;
  try {
    const decoded = jwt.verify(refreshToken, process.env.JWT_REFRESH_SECRET);
    const result = await pool.query('SELECT * FROM refresh_tokens WHERE token = $1 AND user_id = $2', [refreshToken, decoded.id]);
    if (!result.rows[0]) return res.status(403).json({ error: 'Invalid refresh token' });
    const userResult = await pool.query('SELECT * FROM users WHERE id = $1', [decoded.id]);
    const token = generateToken(userResult.rows[0]);
    res.json({ token });
  } catch (error) {
    res.status(403).json({ error: 'Invalid refresh token' });
  }
});

router.post('/password-reset/request', async (req, res) => {
  const { email } = req.body;
  try {
    const result = await pool.query('SELECT id FROM users WHERE email = $1', [email]);
    if (!result.rows[0]) return res.status(404).json({ error: 'User not found' });
    const otp = Math.floor(100000 + Math.random() * 900000).toString();
    const expiresAt = new Date(Date.now() + 10 * 60 * 1000);
    await pool.query(
      'INSERT INTO password_resets (user_id, otp, expires_at) VALUES ($1, $2, $3)',
      [result.rows[0].id, otp, expiresAt]
    );
    await sendOTP(email, otp);
    res.json({ message: 'OTP sent to email' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

router.post('/password-reset/verify', async (req, res) => {
  const { email, otp, newPassword } = req.body;
  try {
    const userResult = await pool.query('SELECT id FROM users WHERE email = $1', [email]);
    if (!userResult.rows[0]) return res.status(404).json({ error: 'User not found' });
    const otpResult = await pool.query(
      'SELECT * FROM password_resets WHERE user_id = $1 AND otp = $2 AND expires_at > NOW() ORDER BY created_at DESC LIMIT 1',
      [userResult.rows[0].id, otp]
    );
    if (!otpResult.rows[0]) return res.status(400).json({ error: 'Invalid or expired OTP' });
    const hashedPassword = await bcrypt.hash(newPassword, 10);
    await pool.query('UPDATE users SET password = $1 WHERE id = $2', [hashedPassword, userResult.rows[0].id]);
    await pool.query('DELETE FROM password_resets WHERE user_id = $1', [userResult.rows[0].id]);
    res.json({ message: 'Password reset successful' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

module.exports = router;
