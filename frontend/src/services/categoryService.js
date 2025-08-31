import axios from 'axios';

const API_URL = '/api/categories';

const categoryService = {
  // Get all categories with pagination
  async getCategories(page = 0, size = 100, sort = 'name,asc') {
    try {
      const params = { page, size, sort };
      const response = await axios.get(API_URL, { params });
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch categories';
    }
  },

  // Get single category by ID
  async getCategoryById(id) {
    try {
      const response = await axios.get(`${API_URL}/${id}`);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch category';
    }
  },

  // Create new category
  async createCategory(categoryData) {
    try {
      const response = await axios.post(API_URL, categoryData);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to create category';
    }
  },

  // Update existing category
  async updateCategory(id, categoryData) {
    try {
      const response = await axios.put(`${API_URL}/${id}`, categoryData);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to update category';
    }
  },

  // Delete category
  async deleteCategory(id) {
    try {
      await axios.delete(`${API_URL}/${id}`);
      return true;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to delete category';
    }
  },

  // Get all categories (simple list for dropdowns)
  async getAllCategories() {
    try {
      const response = await axios.get(`${API_URL}/all`);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Failed to fetch categories';
    }
  }
};

export default categoryService;
