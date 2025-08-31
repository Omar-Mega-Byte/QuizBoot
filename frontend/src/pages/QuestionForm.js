import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import questionService from '../services/questionService';
import quizService from '../services/quizService';
import LoadingSpinner from '../components/LoadingSpinner';
import '../styles/Quiz.css';

const QuestionForm = () => {
  const { quizId, questionId } = useParams();
  const navigate = useNavigate();
  const isEditing = Boolean(questionId);

  const [quiz, setQuiz] = useState(null);
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState(null);

  const [formData, setFormData] = useState({
    questionText: '',
    type: 'MULTIPLE_CHOICE',
    points: 1,
    explanation: '',
    correctAnswer: '',
    options: [
      { text: '', correct: false },
      { text: '', correct: false },
      { text: '', correct: false },
      { text: '', correct: false }
    ]
  });

  const [errors, setErrors] = useState({});

  useEffect(() => {
    loadQuiz();
    if (isEditing) {
      loadQuestion();
    }
  }, [quizId, questionId, isEditing]);

  const loadQuiz = async () => {
    try {
      const quizData = await quizService.getQuizById(quizId);
      setQuiz(quizData);
    } catch (err) {
      setError('Failed to load quiz information.');
      console.error('Error loading quiz:', err);
    }
  };

  const loadQuestion = async () => {
    try {
      setLoading(true);
      const question = await questionService.getQuestionById(questionId);

      setFormData({
        questionText: question.questionText || '',
        type: question.type || 'MULTIPLE_CHOICE',
        points: question.points || 1,
        explanation: question.explanation || '',
        correctAnswer: question.correctAnswer || '',
        options: question.options?.length ? question.options : [
          { text: '', correct: false },
          { text: '', correct: false },
          { text: '', correct: false },
          { text: '', correct: false }
        ]
      });
      setError(null);
    } catch (err) {
      setError('Failed to load question. Please try again.');
      console.error('Error loading question:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));

    // Clear error for this field
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: null }));
    }
  };

  const handleOptionChange = (index, field, value) => {
    setFormData(prev => ({
      ...prev,
      options: prev.options.map((option, i) =>
        i === index ? { ...option, [field]: value } : option
      )
    }));
  };

  const handleCorrectOptionChange = (index) => {
    setFormData(prev => ({
      ...prev,
      options: prev.options.map((option, i) => ({
        ...option,
        correct: i === index
      }))
    }));
  };

  const addOption = () => {
    if (formData.options.length < 6) {
      setFormData(prev => ({
        ...prev,
        options: [...prev.options, { text: '', correct: false }]
      }));
    }
  };

  const removeOption = (index) => {
    if (formData.options.length > 2) {
      setFormData(prev => ({
        ...prev,
        options: prev.options.filter((_, i) => i !== index)
      }));
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.questionText.trim()) {
      newErrors.questionText = 'Question text is required';
    } else if (formData.questionText.length < 10) {
      newErrors.questionText = 'Question text must be at least 10 characters';
    } else if (formData.questionText.length > 500) {
      newErrors.questionText = 'Question text must not exceed 500 characters';
    }

    if (!formData.points || formData.points <= 0) {
      newErrors.points = 'Points must be a positive number';
    }

    if (formData.type === 'MULTIPLE_CHOICE') {
      const validOptions = formData.options.filter(opt => opt.text.trim());
      if (validOptions.length < 2) {
        newErrors.options = 'At least 2 options are required';
      }

      const correctOptions = formData.options.filter(opt => opt.correct && opt.text.trim());
      if (correctOptions.length === 0) {
        newErrors.correctOption = 'Please select a correct answer';
      }
    }

    if (formData.type === 'TRUE_FALSE') {
      if (!formData.correctAnswer) {
        newErrors.correctAnswer = 'Please select the correct answer';
      }
    }

    if (formData.type === 'SHORT_ANSWER') {
      if (!formData.correctAnswer.trim()) {
        newErrors.correctAnswer = 'Correct answer is required for short answer questions';
      }
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

      // Get the next question order (count existing questions + 1)
      const questionOrder = quiz?.questions?.length ? quiz.questions.length + 1 : 1;

      const submitData = {
        questionText: formData.questionText,
        quizId: parseInt(quizId),
        questionType: formData.type, // Map 'type' to 'questionType'
        questionOrder: questionOrder, // Add required questionOrder
        points: parseInt(formData.points),
        explanation: formData.explanation || null,
        isRequired: true
      };

      // Clean up and map options for multiple choice
      if (formData.type === 'MULTIPLE_CHOICE') {
        submitData.options = formData.options
          .filter(opt => opt.text.trim()) // Only include non-empty options
          .map((opt, index) => ({
            optionText: opt.text.trim(), // Map 'text' to 'optionText'
            isCorrect: opt.correct, // Map 'correct' to 'isCorrect'
            optionOrder: index + 1, // Add required optionOrder
            explanation: null
          }));
      } else {
        delete submitData.options;
      }

      console.log('Submitting question data:', submitData); // Debug log

      if (isEditing) {
        await questionService.updateQuestion(questionId, submitData);
      } else {
        await questionService.createQuestion(submitData);
      }

      navigate(`/quiz/${quizId}`);
    } catch (err) {
      console.error('Error submitting question:', err);

      // Handle validation errors from backend
      if (err.response?.status === 400 && err.response?.data) {
        const validationErrors = err.response.data;

        // If it's a validation error object with field-specific errors
        if (typeof validationErrors === 'object' && !validationErrors.message) {
          setErrors(validationErrors);
          setError('Please correct the validation errors below.');
        } else {
          // Generic error message
          setError(validationErrors.message || 'Validation failed. Please check your input.');
        }
      } else {
        setError(
          err.response?.data?.message ||
          `Failed to ${isEditing ? 'update' : 'create'} question. Please try again.`
        );
      }
    } finally {
      setSubmitting(false);
    }
  };

  const handleCancel = () => {
    navigate(`/quiz/${quizId}`);
  };

  if (loading) {
    return <LoadingSpinner />;
  }

  return (
    <div className="quiz-app">
      <div className="quiz-header">
        <div>
          <button onClick={handleCancel} className="btn btn-secondary mb-2">
            ← Back to Quiz
          </button>
          <h1 className="quiz-title">
            {isEditing ? 'Edit Question' : 'Create New Question'}
          </h1>
          {quiz && (
            <div className="text-muted">
              Quiz: {quiz.title}
            </div>
          )}
        </div>
      </div>

      {error && (
        <div className="alert alert-danger mb-3">
          {error}
        </div>
      )}

      <div className="form-container">
        <form onSubmit={handleSubmit}>
          {/* Question Text */}
          <div className="form-group">
            <label htmlFor="questionText" className="form-label">
              Question Text *
            </label>
            <textarea
              id="questionText"
              name="questionText"
              className={`form-textarea ${errors.questionText ? 'error' : ''}`}
              value={formData.questionText}
              onChange={handleInputChange}
              placeholder="Enter your question here (minimum 10 characters)..."
              rows={4}
              maxLength={500}
            />
            {errors.questionText && <div className="form-error">{errors.questionText}</div>}
            <div className="form-help">
              {formData.questionText.length}/500 characters (minimum 10 required)
            </div>
          </div>

          {/* Question Type and Points */}
          <div className="filters-row">
            <div className="form-group">
              <label htmlFor="type" className="form-label">
                Question Type
              </label>
              <select
                id="type"
                name="type"
                className="form-select"
                value={formData.type}
                onChange={handleInputChange}
              >
                <option value="MULTIPLE_CHOICE">Multiple Choice</option>
                <option value="TRUE_FALSE">True/False</option>
                <option value="SHORT_ANSWER">Short Answer</option>
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="points" className="form-label">
                Points *
              </label>
              <input
                type="number"
                id="points"
                name="points"
                className={`form-input ${errors.points ? 'error' : ''}`}
                value={formData.points}
                onChange={handleInputChange}
                min="1"
                max="100"
              />
              {errors.points && <div className="form-error">{errors.points}</div>}
            </div>
          </div>

          {/* Multiple Choice Options */}
          {formData.type === 'MULTIPLE_CHOICE' && (
            <div className="form-group">
              <label className="form-label">Answer Options *</label>
              {errors.options && <div className="form-error">{errors.options}</div>}
              {errors.correctOption && <div className="form-error">{errors.correctOption}</div>}

              {formData.options.map((option, index) => (
                <div key={index} className="d-flex gap-1 mb-2 align-items-center">
                  <input
                    type="radio"
                    name="correctOption"
                    checked={option.correct}
                    onChange={() => handleCorrectOptionChange(index)}
                    style={{ marginRight: '10px' }}
                  />
                  <input
                    type="text"
                    className="form-input"
                    placeholder={`Option ${index + 1}`}
                    value={option.text}
                    onChange={(e) => handleOptionChange(index, 'text', e.target.value)}
                    maxLength={200}
                    style={{ flex: 1 }}
                  />
                  {formData.options.length > 2 && (
                    <button
                      type="button"
                      onClick={() => removeOption(index)}
                      className="btn btn-sm btn-danger"
                    >
                      Remove
                    </button>
                  )}
                </div>
              ))}

              {formData.options.length < 6 && (
                <button
                  type="button"
                  onClick={addOption}
                  className="btn btn-sm btn-secondary"
                >
                  + Add Option
                </button>
              )}
            </div>
          )}

          {/* True/False */}
          {formData.type === 'TRUE_FALSE' && (
            <div className="form-group">
              <label className="form-label">Correct Answer *</label>
              {errors.correctAnswer && <div className="form-error">{errors.correctAnswer}</div>}
              <div className="d-flex gap-2">
                <label className="d-flex align-items-center">
                  <input
                    type="radio"
                    name="correctAnswer"
                    value="true"
                    checked={formData.correctAnswer === 'true'}
                    onChange={handleInputChange}
                    style={{ marginRight: '8px' }}
                  />
                  True
                </label>
                <label className="d-flex align-items-center">
                  <input
                    type="radio"
                    name="correctAnswer"
                    value="false"
                    checked={formData.correctAnswer === 'false'}
                    onChange={handleInputChange}
                    style={{ marginRight: '8px' }}
                  />
                  False
                </label>
              </div>
            </div>
          )}

          {/* Short Answer */}
          {formData.type === 'SHORT_ANSWER' && (
            <div className="form-group">
              <label htmlFor="correctAnswer" className="form-label">
                Correct Answer *
              </label>
              <input
                type="text"
                id="correctAnswer"
                name="correctAnswer"
                className={`form-input ${errors.correctAnswer ? 'error' : ''}`}
                value={formData.correctAnswer}
                onChange={handleInputChange}
                placeholder="Enter the correct answer"
                maxLength={500}
              />
              {errors.correctAnswer && <div className="form-error">{errors.correctAnswer}</div>}
            </div>
          )}

          {/* Explanation */}
          <div className="form-group">
            <label htmlFor="explanation" className="form-label">
              Explanation (Optional)
            </label>
            <textarea
              id="explanation"
              name="explanation"
              className="form-textarea"
              value={formData.explanation}
              onChange={handleInputChange}
              placeholder="Provide an explanation for the correct answer..."
              rows={3}
              maxLength={500}
            />
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
                isEditing ? 'Update Question' : 'Create Question'
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default QuestionForm;
