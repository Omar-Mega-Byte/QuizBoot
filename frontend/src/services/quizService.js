import axios from 'axios';

const API_URL = '/api/quizzes';

const quizService = {
  // Get all quizzes with pagination and filtering
  async getQuizzes(page = 0, size = 10, sort = 'id,desc', categoryId = null, creatorId = null) {
    try {
      const params = { page, size, sort };
      if (categoryId) params.categoryId = categoryId;
      if (creatorId) params.creatorId = creatorId;

      const response = await axios.get(API_URL, { params });
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch quizzes';
    }
  },

  // Get single quiz by ID
  async getQuizById(id) {
    try {
      const response = await axios.get(`${API_URL}/${id}`);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch quiz';
    }
  },

  // Create new quiz
  async createQuiz(quizData) {
    try {
      console.log('QuizService: Sending data to backend:', JSON.stringify(quizData, null, 2));
      const response = await axios.post(API_URL, quizData);
      return response.data;
    } catch (error) {
      console.error('QuizService: Error response:', error.response?.data);
      throw error.response?.data?.message || 'Failed to create quiz';
    }
  },

  // Update existing quiz
  async updateQuiz(id, quizData) {
    try {
      const response = await axios.put(`${API_URL}/${id}`, quizData);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to update quiz';
    }
  },

  // Get all quizzes with pagination (alias for getQuizzes for compatibility)
  async getAllQuizzes(params = {}) {
    try {
      const {
        page = 0,
        size = 10,
        sort = 'id,desc',
        categoryId = null,
        creatorId = null,
        ...otherParams
      } = params;

      return await this.getQuizzes(page, size, sort, categoryId, creatorId);
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch quizzes';
    }
  },

  // Delete quiz
  async deleteQuiz(id) {
    try {
      await axios.delete(`${API_URL}/${id}`);
      return true;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to delete quiz';
    }
  },

  // Get quizzes by current user (for teachers)
  async getMyQuizzes(page = 0, size = 10) {
    try {
      const user = JSON.parse(localStorage.getItem('user'));
      return await this.getQuizzes(page, size, 'id,desc', null, user.id);
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch your quizzes';
    }
  }
};

export default quizService;
