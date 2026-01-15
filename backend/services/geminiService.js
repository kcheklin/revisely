const { GoogleGenerativeAI } = require('@google/generative-ai');

class GeminiService {
    constructor() {
        if (!process.env.GEMINI_API_KEY) {
            console.warn('⚠️  GEMINI_API_KEY not found in environment variables');
        }
        this.genAI = new GoogleGenerativeAI(process.env.GEMINI_API_KEY || '');
        this.model = this.genAI.getGenerativeModel({ model: 'gemini-2.5-flash' });
    }

    /**
     * Generate quiz questions based on textbook chapter information
     * @param {Object} chapterData - Chapter information including title, description, topics
     * @param {Object} quizOptions - Options for quiz generation (difficulty, numQuestions, questionTypes)
     * @returns {Promise<Array>} Array of generated questions
     */
    async generateQuizQuestions(chapterData, quizOptions = {}) {
        const {
            difficulty = 'medium',
            numQuestions = 5,
            questionTypes = ['multiple-choice', 'true-false']
        } = quizOptions;

        const prompt = this.buildQuizPrompt(chapterData, difficulty, numQuestions, questionTypes);

        try {
            const result = await this.model.generateContent(prompt);
            const response = await result.response;
            const text = response.text();
            
            // Parse the JSON response from Gemini
            const questions = this.parseQuizResponse(text);
            
            return questions;
        } catch (error) {
            console.error('Error generating quiz questions:', error);
            throw new Error('Failed to generate quiz questions: ' + error.message);
        }
    }

    /**
     * Build a comprehensive prompt for Gemini AI to generate quiz questions
     */
    buildQuizPrompt(chapterData, difficulty, numQuestions, questionTypes) {
        const { textbookTitle, textbookSubject, chapterTitle, chapterDescription, topics } = chapterData;

        const topicsText = Array.isArray(topics) && topics.length > 0 
            ? topics.join(', ') 
            : 'general topics from the chapter';

        return `You are an expert educational content creator. Generate ${numQuestions} quiz questions for students studying the following textbook chapter.

**Textbook Information:**
- Title: ${textbookTitle}
- Subject: ${textbookSubject}

**Chapter Information:**
- Chapter: ${chapterTitle}
- Description: ${chapterDescription}
- Topics Covered: ${topicsText}

**Quiz Requirements:**
- Difficulty Level: ${difficulty}
- Number of Questions: ${numQuestions}
- Question Types: ${questionTypes.join(', ')}
- Ensure questions are diverse and cover different topics from the chapter
- For multiple-choice questions, provide 4 options
- For true-false questions, provide clear statements
- Include explanations for correct answers

**Output Format (JSON):**
Return ONLY a valid JSON array with no additional text, markdown formatting, or code blocks. Each question should follow this exact structure:

[
  {
    "questionText": "The question text here",
    "questionType": "multiple-choice or true-false",
    "options": ["Option 1", "Option 2", "Option 3", "Option 4"],
    "correctAnswer": "0" (for multiple-choice, use index "0", "1", "2", or "3") or "true"/"false" (for true-false),
    "explanation": "Detailed explanation of why this is the correct answer",
    "difficulty": "${difficulty}",
    "points": 2
  }
]

**Important:**
- For multiple-choice questions, correctAnswer must be the index as a string ("0", "1", "2", or "3")
- For true-false questions, correctAnswer must be "true" or "false" (lowercase)
- The options array should have exactly 4 items for multiple-choice, and ["True", "False"] for true-false
- Ensure all JSON is properly formatted and valid
- Do NOT include any markdown code blocks like \`\`\`json or \`\`\`
- Return ONLY the raw JSON array

Generate the questions now:`;
    }

    /**
     * Parse the response from Gemini and extract questions
     */
    parseQuizResponse(responseText) {
        try {
            // Remove any markdown code blocks if present
            let cleanedText = responseText.trim();
            
            // Remove ```json or ``` markers if present
            cleanedText = cleanedText.replace(/```json\s*/g, '');
            cleanedText = cleanedText.replace(/```\s*/g, '');
            
            // Try to find JSON array in the response
            const jsonMatch = cleanedText.match(/\[[\s\S]*\]/);
            if (jsonMatch) {
                cleanedText = jsonMatch[0];
            }

            const questions = JSON.parse(cleanedText);

            if (!Array.isArray(questions)) {
                throw new Error('Response is not an array');
            }

            // Validate and normalize each question
            return questions.map((q, index) => {
                if (!q.questionText || !q.questionType || !q.correctAnswer) {
                    throw new Error(`Invalid question format at index ${index}`);
                }

                // Normalize question types
                const normalizedType = q.questionType.toLowerCase().trim();
                if (!['multiple-choice', 'true-false', 'short-answer', 'essay'].includes(normalizedType)) {
                    throw new Error(`Invalid question type: ${q.questionType}`);
                }

                // Ensure options exist for multiple-choice and true-false
                if (normalizedType === 'multiple-choice' && (!q.options || q.options.length < 2)) {
                    throw new Error(`Multiple-choice question missing options at index ${index}`);
                }

                if (normalizedType === 'true-false') {
                    q.options = ['True', 'False'];
                    // Normalize correctAnswer to lowercase
                    q.correctAnswer = q.correctAnswer.toString().toLowerCase();
                }

                // Ensure correctAnswer is a string
                q.correctAnswer = q.correctAnswer.toString();

                return {
                    questionText: q.questionText,
                    questionType: normalizedType,
                    options: q.options || null,
                    correctAnswer: q.correctAnswer,
                    explanation: q.explanation || 'No explanation provided',
                    difficulty: q.difficulty || 'medium',
                    points: q.points || 1
                };
            });
        } catch (error) {
            console.error('Error parsing Gemini response:', error);
            console.error('Response text:', responseText);
            throw new Error('Failed to parse quiz questions from AI response: ' + error.message);
        }
    }

    /**
     * Test the Gemini API connection
     */
    async testConnection() {
        try {
            const result = await this.model.generateContent('Say "Hello from Gemini API!"');
            const response = await result.response;
            return { success: true, message: response.text() };
        } catch (error) {
            return { success: false, error: error.message };
        }
    }
}

module.exports = new GeminiService();

