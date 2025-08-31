import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import categoryService from '../services/categoryService';
import LoadingSpinner from '../components/LoadingSpinner';
import Modal from '../components/Modal';
import Pagination from '../components/Pagination';
import '../styles/Quiz.css';

const CategoryList = () => {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [editingCategory, setEditingCategory] = useState(null);

  // Pagination
  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  // Search
  const [searchTerm, setSearchTerm] = useState('');

  // Form data
  const [formData, setFormData] = useState({
    name: '',
    description: ''
  });
  const [formErrors, setFormErrors] = useState({});
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    loadCategories();
  }, [currentPage, pageSize, searchTerm]);

  const loadCategories = async () => {
    try {
      setLoading(true);
      const params = {
        page: currentPage,
        size: pageSize
      };

      if (searchTerm.trim()) {
        params.name = searchTerm.trim();
      }

      const response = await categoryService.getCategories(params.page, params.size);

      setCategories(response.content || []);
      setTotalPages(response.totalPages || 0);
      setTotalElements(response.totalElements || 0);
      setError(null);
    } catch (err) {
      setError('Failed to load categories. Please try again.');
      console.error('Error loading categories:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (value) => {
    setSearchTerm(value);
    setCurrentPage(0); // Reset to first page when searching
  };

  const openCreateModal = () => {
    setFormData({ name: '', description: '' });
    setFormErrors({});
    setEditingCategory(null);
    setShowCreateModal(true);
  };

  const openEditModal = (category) => {
    setFormData({
      name: category.name || '',
      description: category.description || ''
    });
    setFormErrors({});
    setEditingCategory(category);
    setShowCreateModal(true);
  };

  const closeModal = () => {
    setShowCreateModal(false);
    setEditingCategory(null);
    setFormData({ name: '', description: '' });
    setFormErrors({});
  };

  const validateForm = () => {
    const errors = {};

    if (!formData.name.trim()) {
      errors.name = 'Category name is required';
    } else if (formData.name.length < 2) {
      errors.name = 'Category name must be at least 2 characters';
    }

    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    try {
      setSubmitting(true);
      setError(null);

      if (editingCategory) {
        await categoryService.updateCategory(editingCategory.id, formData);
      } else {
        await categoryService.createCategory(formData);
      }

      closeModal();
      loadCategories();
    } catch (err) {
      setError(
        err.response?.data?.message ||
        `Failed to ${editingCategory ? 'update' : 'create'} category. Please try again.`
      );
      console.error('Error submitting category:', err);
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (categoryId, categoryName) => {
    if (!window.confirm(`Are you sure you want to delete "${categoryName}"? This action cannot be undone.`)) {
      return;
    }

    try {
      await categoryService.deleteCategory(categoryId);
      loadCategories();
    } catch (err) {
      setError('Failed to delete category. Please try again.');
      console.error('Error deleting category:', err);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));

    // Clear error for this field
    if (formErrors[name]) {
      setFormErrors(prev => ({ ...prev, [name]: null }));
    }
  };

  if (loading && currentPage === 0) {
    return <LoadingSpinner />;
  }

  return (
    <div className="quiz-app">
      <div className="quiz-header">
        <h1 className="quiz-title">Category Management</h1>
        <div className="header-actions">
          <button onClick={openCreateModal} className="btn btn-primary">
            + Create New Category
          </button>
        </div>
      </div>

      {error && (
        <div className="alert alert-danger">
          {error}
        </div>
      )}

      {/* Search */}
      <div className="filters-container">
        <div className="filters-row">
          <div className="filter-group">
            <label className="filter-label">Search Categories</label>
            <input
              type="text"
              className="filter-input"
              placeholder="Search by category name..."
              value={searchTerm}
              onChange={(e) => handleSearch(e.target.value)}
            />
          </div>
        </div>
      </div>

      {/* Categories */}
      {loading ? (
        <LoadingSpinner />
      ) : categories.length === 0 ? (
        <div className="empty-state">
          <h3>No Categories Found</h3>
          <p>
            {searchTerm ?
              'No categories match your search criteria.' :
              'No categories have been created yet.'
            }
          </p>
          <button onClick={openCreateModal} className="btn btn-primary">
            Create First Category
          </button>
        </div>
      ) : (
        <>
          <div className="cards-grid">
            {categories.map(category => (
              <div key={category.id} className="card">
                <div className="card-header">
                  <h3 className="card-title">{category.name}</h3>
                  <div className="card-actions">
                    <button
                      onClick={() => openEditModal(category)}
                      className="btn btn-sm btn-warning"
                      title="Edit Category"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => handleDelete(category.id, category.name)}
                      className="btn btn-sm btn-danger"
                      title="Delete Category"
                    >
                      Delete
                    </button>
                  </div>
                </div>

                <div className="card-meta">
                  <div className="meta-item">
                    <span>📊</span>
                    <span>{category.quizCount || 0} quizzes</span>
                  </div>
                  <div className="meta-item">
                    <span>📅</span>
                    <span>Created: {new Date(category.createdAt).toLocaleDateString()}</span>
                  </div>
                </div>

                {category.description && (
                  <p className="card-description">
                    {category.description.length > 150
                      ? `${category.description.substring(0, 150)}...`
                      : category.description
                    }
                  </p>
                )}

                <div className="card-footer">
                  <Link
                    to={`/quiz?categoryId=${category.id}`}
                    className="btn btn-sm btn-secondary"
                  >
                    View Quizzes
                  </Link>
                  <div className="text-muted">
                    ID: {category.id}
                  </div>
                </div>
              </div>
            ))}
          </div>

          <Pagination
            currentPage={currentPage}
            totalPages={totalPages}
            totalElements={totalElements}
            pageSize={pageSize}
            onPageChange={setCurrentPage}
            onPageSizeChange={(size) => {
              setPageSize(size);
              setCurrentPage(0);
            }}
          />
        </>
      )}

      {/* Create/Edit Modal */}
      <Modal
        isOpen={showCreateModal}
        onClose={closeModal}
        title={editingCategory ? 'Edit Category' : 'Create New Category'}
      >
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="name" className="form-label">
              Category Name *
            </label>
            <input
              type="text"
              id="name"
              name="name"
              className={`form-input ${formErrors.name ? 'error' : ''}`}
              value={formData.name}
              onChange={handleInputChange}
              placeholder="Enter category name"
              maxLength={100}
            />
            {formErrors.name && <div className="form-error">{formErrors.name}</div>}
          </div>

          <div className="form-group">
            <label htmlFor="description" className="form-label">
              Description
            </label>
            <textarea
              id="description"
              name="description"
              className="form-textarea"
              value={formData.description}
              onChange={handleInputChange}
              placeholder="Enter category description (optional)"
              rows={4}
              maxLength={500}
            />
          </div>

          <div className="form-actions">
            <button
              type="button"
              onClick={closeModal}
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
                  {editingCategory ? 'Updating...' : 'Creating...'}
                </span>
              ) : (
                editingCategory ? 'Update Category' : 'Create Category'
              )}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default CategoryList;
