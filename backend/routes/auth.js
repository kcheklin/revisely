const express = require('express');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const { User, RefreshToken, PasswordReset } = require('../models');
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
    const user = await User.create({
      email,
      password: hashedPassword,
      name,
      role
    });
    res.status(201).json({ 
      user: { 
        id: user.id, 
        email: user.email, 
        name: user.name, 
        role: user.role 
      } 
    });
  } catch (error) {
    if (error.name === 'SequelizeUniqueConstraintError') {
      return res.status(400).json({ error: 'Email already exists' });
    }
    res.status(400).json({ error: error.message });
  }
});

router.post('/login', async (req, res) => {
  const { email, password } = req.body;
  try {
    const user = await User.findOne({ where: { email } });
    if (!user || !(await bcrypt.compare(password, user.password))) {
      return res.status(401).json({ error: 'Invalid credentials' });
    }
    const token = generateToken(user);
    const refreshToken = generateRefreshToken(user);
    await RefreshToken.create({
      userId: user.id,
      token: refreshToken
    });
    res.json({ 
      token, 
      refreshToken, 
      user: { 
        id: user.id, 
        email: user.email, 
        name: user.name, 
        role: user.role 
      } 
    });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

router.post('/refresh', async (req, res) => {
  const { refreshToken } = req.body;
  try {
    const decoded = jwt.verify(refreshToken, process.env.JWT_REFRESH_SECRET);
    const storedToken = await RefreshToken.findOne({ 
      where: { token: refreshToken, userId: decoded.id } 
    });
    if (!storedToken) {
      return res.status(403).json({ error: 'Invalid refresh token' });
    }
    const user = await User.findByPk(decoded.id);
    if (!user) {
      return res.status(403).json({ error: 'User not found' });
    }
    const token = generateToken(user);
    res.json({ token });
  } catch (error) {
    res.status(403).json({ error: 'Invalid refresh token' });
  }
});

router.post('/password-reset/request', async (req, res) => {
  const { email } = req.body;
  try {
    const user = await User.findOne({ where: { email } });
    if (!user) {
      return res.status(404).json({ error: 'User not found' });
    }
    const otp = Math.floor(100000 + Math.random() * 900000).toString();
    const expiresAt = new Date(Date.now() + 10 * 60 * 1000);
    await PasswordReset.create({
      userId: user.id,
      otp,
      expiresAt
    });
    await sendOTP(email, otp);
    res.json({ message: 'OTP sent to email' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

router.post('/password-reset/verify', async (req, res) => {
  const { email, otp, newPassword } = req.body;
  try {
    const user = await User.findOne({ where: { email } });
    if (!user) {
      return res.status(404).json({ error: 'User not found' });
    }
    const { Op } = require('sequelize');
    const passwordReset = await PasswordReset.findOne({
      where: {
        userId: user.id,
        otp,
        expiresAt: { [Op.gt]: new Date() }
      },
      order: [['createdAt', 'DESC']]
    });
    if (!passwordReset) {
      return res.status(400).json({ error: 'Invalid or expired OTP' });
    }
    const hashedPassword = await bcrypt.hash(newPassword, 10);
    await user.update({ password: hashedPassword });
    await PasswordReset.destroy({ where: { userId: user.id } });
    res.json({ message: 'Password reset successful' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

module.exports = router;
