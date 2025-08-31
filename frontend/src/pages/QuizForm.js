import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import quizService from '../services/quizService';
import categoryService from '../services/categoryService';
import authService from '../services/authService';
import LoadingSpinner from '../components/LoadingSpinner';
import '../styles/Quiz.css';

const QuizForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEditing = Boolean(id);

  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState(null);

  const [formData, setFormData] = useState({
    title: '',
    description: '',
    categoryId: '',
    duration: '1', // Duration in hours as string for form input
    passingScore: '70.0', // Default passing score as string for form input
    maxAttempts: '1' // Default max attempts as string for form input
  });

  const [errors, setErrors] = useState({});

  useEffect(() => {
    loadCategories();
    if (isEditing) {
      loadQuiz();
    }
  }, [id, isEditing]);

  const loadCategories = async () => {
    try {
      const response = await categoryService.getAllCategories();
      console.log('Loaded categories:', response); // Debug log
      setCategories(response || []);
    } catch (err) {
      console.error('Error loading categories:', err);
    }
  };

  const loadQuiz = async () => {
    try {
      setLoading(true);
      const quiz = await quizService.getQuizById(id);
      setFormData({
        title: quiz.title || '',
        description: quiz.description || '',
        categoryId: quiz.categoryId || '',
        duration: quiz.duration ? quiz.duration.toString() : '60',
        passingScore: quiz.passingScore ? quiz.passingScore.toString() : '70.0',
        maxAttempts: quiz.maxAttempts ? quiz.maxAttempts.toString() : '1'
      });
      setError(null);
    } catch (err) {
      setError('Failed to load quiz. Please try again.');
      console.error('Error loading quiz:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));

    // Clear error for this field
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: null }));
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.title.trim()) {
      newErrors.title = 'Title is required';
    } else if (formData.title.length < 3) {
      newErrors.title = 'Title must be at least 3 characters';
    }

    if (!formData.description.trim()) {
      newErrors.description = 'Description is required';
    } else if (formData.description.length < 10) {
      newErrors.description = 'Description must be at least 10 characters';
    }

    if (!formData.categoryId) {
      newErrors.categoryId = 'Category is required';
    }

    if (!formData.duration || isNaN(Number(formData.duration)) || Number(formData.duration) <= 0) {
      newErrors.duration = 'Duration must be a positive number (in hours)';
    } else if (Number(formData.duration) > 3) {
      newErrors.duration = 'Duration cannot exceed 3 hours';
    }

    if (!formData.passingScore || isNaN(Number(formData.passingScore)) || Number(formData.passingScore) < 0) {
      newErrors.passingScore = 'Passing score must be a positive number';
    } else if (Number(formData.passingScore) > 100) {
      newErrors.passingScore = 'Passing score cannot exceed 100%';
    }

    if (!formData.maxAttempts || isNaN(Number(formData.maxAttempts)) || Number(formData.maxAttempts) <= 0) {
      newErrors.maxAttempts = 'Max attempts must be a positive number';
    } else if (Number(formData.maxAttempts) > 10) {
      newErrors.maxAttempts = 'Max attempts cannot exceed 10';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    try {
      setSubmitting(true);
      setError(null);

      // Get current user for creatorId
      const currentUser = authService.getCurrentUser();
      console.log('Current user:', currentUser); // Debug log
      if (!currentUser || !currentUser.id) {
        throw new Error('User not authenticated');
      }

      // Validate that categoryId is selected
      if (!formData.categoryId || formData.categoryId === '') {
        setError('Please select a category');
        return;
      }

      // Parse and validate categoryId
      const parsedCategoryId = parseInt(formData.categoryId);
      if (isNaN(parsedCategoryId)) {
        setError('Invalid category selected');
        return;
      }

      // Ensure creatorId is a number
      const creatorId = typeof currentUser.id === 'string' ? parseInt(currentUser.id) : currentUser.id;

      const submitData = {
        title: formData.title,
        description: formData.description,
        categoryId: parsedCategoryId,
        duration: parseInt(formData.duration),
        passingScore: parseFloat(formData.passingScore),
        maxAttempts: parseInt(formData.maxAttempts),
        creatorId: creatorId
      };

      console.log('Form data before submission:', formData);
      console.log('Submitting quiz data:', submitData); // Debug log
      console.log('CategoryId value:', formData.categoryId, 'Parsed:', parsedCategoryId);
      console.log('CreatorId value:', currentUser.id, 'Parsed:', creatorId);

      // Double-check the submitData right before API call
      if (!submitData.categoryId || submitData.categoryId === null || isNaN(submitData.categoryId)) {
        console.error('Critical error: categoryId is invalid at API call time:', submitData.categoryId);
        setError('Category selection error. Please refresh and try again.');
        return;
      }

      if (isEditing) {
        await quizService.updateQuiz(id, submitData);
      } else {
        await quizService.createQuiz(submitData);
      }

      navigate('/quiz');
    } catch (err) {
      setError(
        err.response?.data?.message ||
        `Failed to ${isEditing ? 'update' : 'create'} quiz. Please try again.`
      );
      console.error('Error submitting quiz:', err);
    } finally {
      setSubmitting(false);
    }
  };

  const handleCancel = () => {
    navigate('/quiz');
  };

  if (loading) {
    return <LoadingSpinner />;
  }

  return (
    <div className="quiz-app">
      <div className="quiz-header">
        <h1 className="quiz-title">
          {isEditing ? 'Edit Quiz' : 'Create New Quiz'}
        </h1>
      </div>

      {error && (
        <div className="alert alert-danger mb-3">
          {error}
        </div>
      )}

      <div className="form-container">
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="title" className="form-label">
              Quiz Title *
            </label>
            <input
              type="text"
              id="title"
              name="title"
              className={`form-input ${errors.title ? 'error' : ''}`}
              value={formData.title}
              onChange={handleInputChange}
              placeholder="Enter quiz title"
              maxLength={200}
            />
            {errors.title && <div className="form-error">{errors.title}</div>}
          </div>

          <div className="form-group">
            <label htmlFor="description" className="form-label">
              Description *
            </label>
            <textarea
              id="description"
              name="description"
              className={`form-textarea ${errors.description ? 'error' : ''}`}
              value={formData.description}
              onChange={handleInputChange}
              placeholder="Enter quiz description"
              rows={4}
              maxLength={1000}
            />
            {errors.description && <div className="form-error">{errors.description}</div>}
          </div>

          <div className="form-group">
            <label htmlFor="categoryId" className="form-label">
              Category *
            </label>
            <select
              id="categoryId"
              name="categoryId"
              className={`form-select ${errors.categoryId ? 'error' : ''}`}
              value={formData.categoryId}
              onChange={handleInputChange}
            >
              <option value="">Select a category</option>
              {categories.length === 0 ? (
                <option disabled>No categories available</option>
              ) : (
                categories.map(category => (
                  <option key={category.id} value={category.id}>
                    {category.name}
                  </option>
                ))
              )}
            </select>
            {errors.categoryId && <div className="form-error">{errors.categoryId}</div>}
          </div>

          <div className="filters-row">
            <div className="form-group">
              <label htmlFor="duration" className="form-label">
                Duration (hours) *
              </label>
              <input
                type="number"
                id="duration"
                name="duration"
                className={`form-input ${errors.duration ? 'error' : ''}`}
                value={formData.duration}
                onChange={handleInputChange}
                placeholder="e.g., 1"
                min="1"
                max="3"
              />
              {errors.duration && <div className="form-error">{errors.duration}</div>}
              <small className="text-muted">Maximum 3 hours</small>
            </div>

            <div className="form-group">
              <label htmlFor="passingScore" className="form-label">
                Passing Score (%) *
              </label>
              <input
                type="number"
                id="passingScore"
                name="passingScore"
                className={`form-input ${errors.passingScore ? 'error' : ''}`}
                value={formData.passingScore}
                onChange={handleInputChange}
                placeholder="e.g., 70"
                min="0"
                max="100"
                step="0.1"
              />
              {errors.passingScore && <div className="form-error">{errors.passingScore}</div>}
              <small className="text-muted">Minimum score needed to pass</small>
            </div>

            <div className="form-group">
              <label htmlFor="maxAttempts" className="form-label">
                Max Attempts *
              </label>
              <input
                type="number"
                id="maxAttempts"
                name="maxAttempts"
                className={`form-input ${errors.maxAttempts ? 'error' : ''}`}
                value={formData.maxAttempts}
                onChange={handleInputChange}
                min="1"
                max="10"
              />
              {errors.maxAttempts && <div className="form-error">{errors.maxAttempts}</div>}
              <small className="text-muted">Number of attempts allowed</small>
            </div>
          </div>

          <div className="form-actions">
            <button
              type="button"
              onClick={handleCancel}
              className="btn btn-secondary"
              disabled={submitting}
            >
              Cancel
            </button>
            <button
              type="submit"
              className="btn btn-primary"
              disabled={submitting}
            >
              {submitting ? (
                <span>
                  <span className="loading-spinner" style={{ width: '16px', height: '16px', marginRight: '8px' }}></span>
                  {isEditing ? 'Updating...' : 'Creating...'}
                </span>
              ) : (
                isEditing ? 'Update Quiz' : 'Create Quiz'
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default QuizForm;
