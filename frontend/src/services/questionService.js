import axios from 'axios';

const API_URL = '/api/questions';

const questionService = {
  // Get questions by quiz ID with pagination
  async getQuestionsByQuizId(quizId, page = 0, size = 50) {
    try {
      const params = { quizId, page, size, sort: 'questionOrder,asc' };
      const response = await axios.get(API_URL, { params });
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch questions';
    }
  },

  // Get single question by ID
  async getQuestionById(id) {
    try {
      const response = await axios.get(`${API_URL}/${id}`);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch question';
    }
  },

  // Create new question
  async createQuestion(questionData) {
    try {
      const response = await axios.post(API_URL, questionData);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to create question';
    }
  },

  // Update existing question
  async updateQuestion(id, questionData) {
    try {
      const response = await axios.put(`${API_URL}/${id}`, questionData);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to update question';
    }
  },

  // Delete question
  async deleteQuestion(id) {
    try {
      await axios.delete(`${API_URL}/${id}`);
      return true;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to delete question';
    }
  },

  // Bulk create questions for a quiz
  async createBulkQuestions(questions) {
    try {
      const promises = questions.map(question => this.createQuestion(question));
      const results = await Promise.all(promises);
      return results;
    } catch (error) {
      throw 'Failed to create questions';
    }
  }
};

export default questionService;
