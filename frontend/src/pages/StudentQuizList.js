import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import quizService from '../services/quizService';
import categoryService from '../services/categoryService';
import LoadingSpinner from '../components/LoadingSpinner';
import Pagination from '../components/Pagination';
import '../styles/Quiz.css';

const StudentQuizList = () => {
  const [quizzes, setQuizzes] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Pagination and filters
  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const [filters, setFilters] = useState({
    title: '',
    categoryId: '',
    difficulty: ''
  });

  useEffect(() => {
    loadCategories();
  }, []);

  useEffect(() => {
    loadActiveQuizzes();
  }, [currentPage, pageSize, filters]);

  const loadCategories = async () => {
    try {
      const response = await categoryService.getAllCategories();
      setCategories(response || []);
    } catch (err) {
      console.error('Error loading categories:', err);
    }
  };

  const loadActiveQuizzes = async () => {
    try {
      setLoading(true);
      const params = {
        page: currentPage,
        size: pageSize,
        active: 'true', // Only show active quizzes for students
        ...filters
      };

      // Remove empty filters
      Object.keys(params).forEach(key => {
        if (params[key] === '' || params[key] === null || params[key] === undefined) {
          delete params[key];
        }
      });

      const response = await quizService.getAllQuizzes(params);

      setQuizzes(response.content || []);
      setTotalPages(response.totalPages || 0);
      setTotalElements(response.totalElements || 0);
      setError(null);
    } catch (err) {
      setError('Failed to load quizzes. Please try again.');
      console.error('Error loading quizzes:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleFilterChange = (field, value) => {
    setFilters(prev => ({ ...prev, [field]: value }));
    setCurrentPage(0); // Reset to first page when filtering
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  const handlePageSizeChange = (size) => {
    setPageSize(size);
    setCurrentPage(0);
  };

  const getDifficultyBadgeClass = (difficulty) => {
    switch (difficulty?.toLowerCase()) {
      case 'easy': return 'badge badge-easy';
      case 'medium': return 'badge badge-medium';
      case 'hard': return 'badge badge-hard';
      default: return 'badge badge-secondary';
    }
  };

  if (loading && currentPage === 0) {
    return <LoadingSpinner />;
  }

  return (
    <div className="quiz-app">
      <div className="quiz-header">
        <h1 className="quiz-title">Available Quizzes</h1>
        <div className="header-actions">
          <Link to="/student-dashboard" className="btn btn-secondary">
            ← Back to Dashboard
          </Link>
        </div>
      </div>

      {error && (
        <div className="alert alert-danger">
          {error}
        </div>
      )}

      {/* Filters */}
      <div className="filters-container">
        <div className="filters-row">
          <div className="filter-group">
            <label className="filter-label">Search by Title</label>
            <input
              type="text"
              className="filter-input"
              placeholder="Enter quiz title..."
              value={filters.title}
              onChange={(e) => handleFilterChange('title', e.target.value)}
            />
          </div>

          <div className="filter-group">
            <label className="filter-label">Category</label>
            <select
              className="filter-select"
              value={filters.categoryId}
              onChange={(e) => handleFilterChange('categoryId', e.target.value)}
            >
              <option value="">All Categories</option>
              {categories.map(category => (
                <option key={category.id} value={category.id}>
                  {category.name}
                </option>
              ))}
            </select>
          </div>

          <div className="filter-group">
            <label className="filter-label">Difficulty</label>
            <select
              className="filter-select"
              value={filters.difficulty}
              onChange={(e) => handleFilterChange('difficulty', e.target.value)}
            >
              <option value="">All Difficulties</option>
              <option value="EASY">Easy</option>
              <option value="MEDIUM">Medium</option>
              <option value="HARD">Hard</option>
            </select>
          </div>
        </div>
      </div>

      {/* Quiz Cards */}
      {loading ? (
        <LoadingSpinner />
      ) : quizzes.length === 0 ? (
        <div className="empty-state">
          <h3>No Quizzes Available</h3>
          <p>No active quizzes match your current filters.</p>
          <Link to="/student-dashboard" className="btn btn-primary">
            Back to Dashboard
          </Link>
        </div>
      ) : (
        <div className="cards-grid">
          {quizzes.map(quiz => (
            <div key={quiz.id} className="card">
              <div className="card-header">
                <h3 className="card-title">{quiz.title}</h3>
                <div className="card-actions">
                  <Link
                    to={`/student/quiz/${quiz.id}/take`}
                    className="btn btn-sm btn-primary"
                    title="Take Quiz"
                  >
                    Take Quiz
                  </Link>
                </div>
              </div>

              <div className="card-meta">
                <div className="meta-item">
                  <span>📁</span>
                  <span>{quiz.categoryName || 'No Category'}</span>
                </div>
                <div className="meta-item">
                  <span>⏱️</span>
                  <span>{quiz.timeLimit ? `${quiz.timeLimit} min` : 'No Limit'}</span>
                </div>
                <div className="meta-item">
                  <span>❓</span>
                  <span>{quiz.questionCount || 0} questions</span>
                </div>
                <div className="meta-item">
                  <span>🔄</span>
                  <span>{quiz.maxAttempts || 1} attempts</span>
                </div>
              </div>

              {quiz.description && (
                <p className="card-description">
                  {quiz.description.length > 150
                    ? `${quiz.description.substring(0, 150)}...`
                    : quiz.description
                  }
                </p>
              )}

              <div className="card-footer">
                <div className="d-flex gap-1">
                  <span className={getDifficultyBadgeClass(quiz.difficulty)}>
                    {quiz.difficulty || 'N/A'}
                  </span>
                  <span className="badge badge-active">
                    Active
                  </span>
                </div>
                <div className="text-muted">
                  {quiz.showCorrectAnswers ? 'Answers shown' : 'Answers hidden'}
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Pagination */}
      <Pagination
        currentPage={currentPage}
        totalPages={totalPages}
        totalElements={totalElements}
        pageSize={pageSize}
        onPageChange={handlePageChange}
        onPageSizeChange={handlePageSizeChange}
      />
    </div>
  );
};

export default StudentQuizList;
