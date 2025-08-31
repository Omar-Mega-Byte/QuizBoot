import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import quizService from '../services/quizService';
import questionService from '../services/questionService';
import LoadingSpinner from '../components/LoadingSpinner';
import Modal from '../components/Modal';
import Pagination from '../components/Pagination';
import '../styles/Quiz.css';

const QuizDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [quiz, setQuiz] = useState(null);
  const [questions, setQuestions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [questionsLoading, setQuestionsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showDeleteModal, setShowDeleteModal] = useState(false);

  // Pagination for questions
  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  useEffect(() => {
    loadQuiz();
  }, [id]);

  useEffect(() => {
    if (quiz) {
      loadQuestions();
    }
  }, [quiz, currentPage, pageSize]);

  const loadQuiz = async () => {
    try {
      setLoading(true);
      const quizData = await quizService.getQuizById(id);
      setQuiz(quizData);
      setError(null);
    } catch (err) {
      setError('Failed to load quiz details. Please try again.');
      console.error('Error loading quiz:', err);
    } finally {
      setLoading(false);
    }
  };

  const loadQuestions = async () => {
    try {
      setQuestionsLoading(true);
      const params = {
        page: currentPage,
        size: pageSize,
        quizId: id
      };

      const response = await questionService.getAllQuestions(params);
      setQuestions(response.content || []);
      setTotalPages(response.totalPages || 0);
      setTotalElements(response.totalElements || 0);
    } catch (err) {
      console.error('Error loading questions:', err);
    } finally {
      setQuestionsLoading(false);
    }
  };

  const handleDeleteQuiz = async () => {
    try {
      await quizService.deleteQuiz(id);
      navigate('/quiz');
    } catch (err) {
      setError('Failed to delete quiz. Please try again.');
      console.error('Error deleting quiz:', err);
    }
    setShowDeleteModal(false);
  };

  const handleDeleteQuestion = async (questionId) => {
    if (!window.confirm('Are you sure you want to delete this question?')) {
      return;
    }

    try {
      await questionService.deleteQuestion(questionId);
      loadQuestions(); // Reload questions
      loadQuiz(); // Reload quiz to update question count
    } catch (err) {
      setError('Failed to delete question. Please try again.');
      console.error('Error deleting question:', err);
    }
  };

  const getDifficultyBadgeClass = (difficulty) => {
    switch (difficulty?.toLowerCase()) {
      case 'easy': return 'badge badge-easy';
      case 'medium': return 'badge badge-medium';
      case 'hard': return 'badge badge-hard';
      default: return 'badge badge-secondary';
    }
  };

  const getQuestionTypeBadge = (type) => {
    switch (type?.toUpperCase()) {
      case 'MULTIPLE_CHOICE':
        return <span className="badge badge-primary">Multiple Choice</span>;
      case 'TRUE_FALSE':
        return <span className="badge badge-success">True/False</span>;
      case 'SHORT_ANSWER':
        return <span className="badge badge-warning">Short Answer</span>;
      default:
        return <span className="badge badge-secondary">{type}</span>;
    }
  };

  if (loading) {
    return <LoadingSpinner />;
  }

  if (error && !quiz) {
    return (
      <div className="quiz-app">
        <div className="alert alert-danger">
          {error}
        </div>
        <Link to="/quiz" className="btn btn-secondary">
          Back to Quiz List
        </Link>
      </div>
    );
  }

  return (
    <div className="quiz-app">
      <div className="quiz-header">
        <div>
          <Link to="/quiz" className="btn btn-secondary mb-2">
            ← Back to Quiz List
          </Link>
          <h1 className="quiz-title">{quiz?.title}</h1>
        </div>
        <div className="header-actions">
          <Link
            to={`/quiz/${id}/questions/create`}
            className="btn btn-success"
          >
            + Add Question
          </Link>
          <Link
            to={`/quiz/${id}/edit`}
            className="btn btn-warning"
          >
            Edit Quiz
          </Link>
          <button
            onClick={() => setShowDeleteModal(true)}
            className="btn btn-danger"
          >
            Delete Quiz
          </button>
        </div>
      </div>

      {error && (
        <div className="alert alert-danger">
          {error}
        </div>
      )}

      {/* Quiz Information */}
      <div className="form-container mb-3">
        <div className="filters-row">
          <div className="form-group">
            <label className="form-label">Category</label>
            <div className="text-muted">{quiz?.categoryName || 'No Category'}</div>
          </div>
          <div className="form-group">
            <label className="form-label">Difficulty</label>
            <div>
              <span className={getDifficultyBadgeClass(quiz?.difficulty)}>
                {quiz?.difficulty || 'N/A'}
              </span>
            </div>
          </div>
          <div className="form-group">
            <label className="form-label">Time Limit</label>
            <div className="text-muted">
              {quiz?.timeLimit ? `${quiz.timeLimit} minutes` : 'No limit'}
            </div>
          </div>
          <div className="form-group">
            <label className="form-label">Status</label>
            <div>
              <span className={`badge ${quiz?.active ? 'badge-active' : 'badge-inactive'}`}>
                {quiz?.active ? 'Active' : 'Inactive'}
              </span>
            </div>
          </div>
        </div>

        <div className="form-group">
          <label className="form-label">Description</label>
          <div className="text-muted">{quiz?.description}</div>
        </div>

        <div className="filters-row">
          <div className="form-group">
            <label className="form-label">Max Attempts</label>
            <div className="text-muted">{quiz?.maxAttempts || 1}</div>
          </div>
          <div className="form-group">
            <label className="form-label">Randomize Questions</label>
            <div className="text-muted">
              {quiz?.randomizeQuestions ? 'Yes' : 'No'}
            </div>
          </div>
          <div className="form-group">
            <label className="form-label">Show Correct Answers</label>
            <div className="text-muted">
              {quiz?.showCorrectAnswers ? 'Yes' : 'No'}
            </div>
          </div>
          <div className="form-group">
            <label className="form-label">Total Questions</label>
            <div className="text-muted">{totalElements}</div>
          </div>
        </div>
      </div>

      {/* Questions Section */}
      <div className="quiz-header">
        <h2 className="quiz-title">Questions</h2>
        <div className="header-actions">
          <Link
            to={`/quiz/${id}/questions/create`}
            className="btn btn-primary"
          >
            + Add Question
          </Link>
        </div>
      </div>

      {questionsLoading ? (
        <LoadingSpinner />
      ) : questions.length === 0 ? (
        <div className="empty-state">
          <h3>No Questions Yet</h3>
          <p>This quiz doesn't have any questions yet. Add some questions to get started.</p>
          <Link
            to={`/quiz/${id}/questions/create`}
            className="btn btn-primary"
          >
            Add First Question
          </Link>
        </div>
      ) : (
        <>
          <div className="cards-grid">
            {questions.map((question, index) => (
              <div key={question.id} className="card">
                <div className="card-header">
                  <h3 className="card-title">
                    Question {currentPage * pageSize + index + 1}
                  </h3>
                  <div className="card-actions">
                    <Link
                      to={`/quiz/${id}/questions/${question.id}/edit`}
                      className="btn btn-sm btn-warning"
                    >
                      Edit
                    </Link>
                    <button
                      onClick={() => handleDeleteQuestion(question.id)}
                      className="btn btn-sm btn-danger"
                    >
                      Delete
                    </button>
                  </div>
                </div>

                <div className="card-meta">
                  <div className="meta-item">
                    {getQuestionTypeBadge(question.type)}
                  </div>
                  <div className="meta-item">
                    <span>⭐</span>
                    <span>{question.points || 1} points</span>
                  </div>
                </div>

                <div className="card-description">
                  <strong>Question:</strong>
                  <p>{question.questionText}</p>
                </div>

                {question.type === 'MULTIPLE_CHOICE' && question.options && (
                  <div className="question-options">
                    <strong>Options:</strong>
                    <ul style={{ marginLeft: '20px', marginTop: '10px' }}>
                      {question.options.map((option, idx) => (
                        <li key={idx} style={{
                          color: option.correct ? '#56ab2f' : '#6c757d',
                          fontWeight: option.correct ? 'bold' : 'normal'
                        }}>
                          {option.text} {option.correct && '✓'}
                        </li>
                      ))}
                    </ul>
                  </div>
                )}

                {question.type === 'TRUE_FALSE' && (
                  <div className="question-answer">
                    <strong>Correct Answer:</strong>
                    <span style={{ color: '#56ab2f', marginLeft: '10px', fontWeight: 'bold' }}>
                      {question.correctAnswer}
                    </span>
                  </div>
                )}

                {question.explanation && (
                  <div className="question-explanation">
                    <strong>Explanation:</strong>
                    <p style={{ fontStyle: 'italic', color: '#6c757d' }}>
                      {question.explanation}
                    </p>
                  </div>
                )}
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

      {/* Delete Confirmation Modal */}
      <Modal
        isOpen={showDeleteModal}
        onClose={() => setShowDeleteModal(false)}
        title="Confirm Delete"
      >
        <p>Are you sure you want to delete this quiz? This action cannot be undone.</p>
        <p><strong>Quiz:</strong> {quiz?.title}</p>
        <p><strong>Questions:</strong> {totalElements} questions will also be deleted</p>

        <div className="form-actions">
          <button
            onClick={() => setShowDeleteModal(false)}
            className="btn btn-secondary"
          >
            Cancel
          </button>
          <button
            onClick={handleDeleteQuiz}
            className="btn btn-danger"
          >
            Delete Quiz
          </button>
        </div>
      </Modal>
    </div>
  );
};

export default QuizDetails;
