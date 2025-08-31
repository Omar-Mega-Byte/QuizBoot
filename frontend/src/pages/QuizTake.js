import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import quizService from '../services/quizService';
import questionService from '../services/questionService';
import LoadingSpinner from '../components/LoadingSpinner';
import Modal from '../components/Modal';
import '../styles/Quiz.css';

const QuizTake = () => {
  const { quizId } = useParams();
  const navigate = useNavigate();

  const [quiz, setQuiz] = useState(null);
  const [questions, setQuestions] = useState([]);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [answers, setAnswers] = useState({});
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState(null);
  const [showSubmitModal, setShowSubmitModal] = useState(false);
  const [timeLeft, setTimeLeft] = useState(null);
  const [quizStarted, setQuizStarted] = useState(false);

  useEffect(() => {
    loadQuizAndQuestions();
  }, [quizId]);

  useEffect(() => {
    let timer;
    if (quizStarted && timeLeft > 0) {
      timer = setInterval(() => {
        setTimeLeft(prev => {
          if (prev <= 1) {
            handleSubmitQuiz(true); // Auto-submit when time runs out
            return 0;
          }
          return prev - 1;
        });
      }, 1000);
    }
    return () => clearInterval(timer);
  }, [quizStarted, timeLeft]);

  const loadQuizAndQuestions = async () => {
    try {
      setLoading(true);

      // Load quiz details
      const quizData = await quizService.getQuizById(quizId);
      setQuiz(quizData);

      // Set time limit if exists
      if (quizData.timeLimit) {
        setTimeLeft(quizData.timeLimit * 60); // Convert minutes to seconds
      }

      // Load questions for this quiz
      const questionsResponse = await questionService.getAllQuestions({
        quizId: quizId,
        page: 0,
        size: 100 // Get all questions for this quiz
      });

      let quizQuestions = questionsResponse.content || [];

      // Randomize questions if enabled
      if (quizData.randomizeQuestions) {
        quizQuestions = [...quizQuestions].sort(() => Math.random() - 0.5);
      }

      setQuestions(quizQuestions);
      setError(null);
    } catch (err) {
      setError('Failed to load quiz. Please try again.');
      console.error('Error loading quiz:', err);
    } finally {
      setLoading(false);
    }
  };

  const startQuiz = () => {
    setQuizStarted(true);
  };

  const handleAnswerChange = (questionId, answer) => {
    setAnswers(prev => ({
      ...prev,
      [questionId]: answer
    }));
  };

  const getCurrentQuestion = () => {
    return questions[currentQuestionIndex];
  };

  const goToNextQuestion = () => {
    if (currentQuestionIndex < questions.length - 1) {
      setCurrentQuestionIndex(prev => prev + 1);
    }
  };

  const goToPreviousQuestion = () => {
    if (currentQuestionIndex > 0) {
      setCurrentQuestionIndex(prev => prev - 1);
    }
  };

  const goToQuestion = (index) => {
    setCurrentQuestionIndex(index);
  };

  const getAnsweredQuestionsCount = () => {
    return Object.keys(answers).filter(questionId => {
      const answer = answers[questionId];
      return answer !== null && answer !== undefined && answer !== '';
    }).length;
  };

  const handleSubmitQuiz = async (autoSubmit = false) => {
    if (!autoSubmit && getAnsweredQuestionsCount() < questions.length) {
      setShowSubmitModal(true);
      return;
    }

    try {
      setSubmitting(true);

      // Prepare submission data
      const submissionData = {
        quizId: parseInt(quizId),
        answers: questions.map(question => ({
          questionId: question.id,
          answer: answers[question.id] || '',
          selectedOptions: question.type === 'MULTIPLE_CHOICE' ?
            (answers[question.id] ? [answers[question.id]] : []) : []
        }))
      };

      // TODO: Submit to backend when submission endpoint is available
      console.log('Quiz submission:', submissionData);

      // For now, show success and redirect
      alert('Quiz submitted successfully!');
      navigate('/student-dashboard');

    } catch (err) {
      setError('Failed to submit quiz. Please try again.');
      console.error('Error submitting quiz:', err);
    } finally {
      setSubmitting(false);
      setShowSubmitModal(false);
    }
  };

  const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs.toString().padStart(2, '0')}`;
  };

  const getQuestionStatus = (index) => {
    const question = questions[index];
    const hasAnswer = answers[question?.id] !== undefined && answers[question?.id] !== '';

    if (index === currentQuestionIndex) {
      return 'current';
    } else if (hasAnswer) {
      return 'answered';
    } else {
      return 'unanswered';
    }
  };

  if (loading) {
    return <LoadingSpinner />;
  }

  if (error) {
    return (
      <div className="quiz-app">
        <div className="alert alert-danger">
          {error}
        </div>
        <button onClick={() => navigate('/student-dashboard')} className="btn btn-secondary">
          Back to Dashboard
        </button>
      </div>
    );
  }

  if (!quizStarted) {
    return (
      <div className="quiz-app">
        <div className="form-container text-center">
          <h1>{quiz?.title}</h1>
          <p className="mb-3">{quiz?.description}</p>

          <div className="quiz-info mb-3">
            <div className="filters-row">
              <div className="text-center">
                <strong>Questions:</strong> {questions.length}
              </div>
              <div className="text-center">
                <strong>Time Limit:</strong> {quiz?.timeLimit ? `${quiz.timeLimit} minutes` : 'No limit'}
              </div>
              <div className="text-center">
                <strong>Difficulty:</strong> {quiz?.difficulty || 'N/A'}
              </div>
            </div>
          </div>

          <div className="quiz-rules mb-3">
            <h3>Quiz Rules:</h3>
            <ul style={{ textAlign: 'left', maxWidth: '400px', margin: '0 auto' }}>
              <li>Answer all questions to the best of your ability</li>
              <li>You can navigate between questions using the question navigator</li>
              <li>Make sure to submit your quiz before the time limit</li>
              {quiz?.maxAttempts && <li>You have {quiz.maxAttempts} attempt(s) for this quiz</li>}
              {quiz?.showCorrectAnswers && <li>Correct answers will be shown after submission</li>}
            </ul>
          </div>

          <div className="form-actions">
            <button onClick={() => navigate('/student/quizzes')} className="btn btn-secondary">
              Cancel
            </button>
            <button onClick={startQuiz} className="btn btn-primary btn-lg">
              Start Quiz
            </button>
          </div>
        </div>
      </div>
    );
  }

  const currentQuestion = getCurrentQuestion();

  return (
    <div className="quiz-app">
      {/* Quiz Header */}
      <div className="quiz-header">
        <div>
          <h1 className="quiz-title">{quiz?.title}</h1>
          <div className="quiz-progress">
            Question {currentQuestionIndex + 1} of {questions.length}
          </div>
        </div>
        <div className="header-actions">
          {timeLeft !== null && (
            <div className={`quiz-timer ${timeLeft < 300 ? 'danger' : ''}`}>
              ⏰ Time: {formatTime(timeLeft)}
            </div>
          )}
          <div className="quiz-progress">
            Answered: {getAnsweredQuestionsCount()}/{questions.length}
          </div>
        </div>
      </div>

      {error && (
        <div className="alert alert-danger">
          {error}
        </div>
      )}

      <div className="quiz-layout">
        {/* Question Navigator */}
        <div className="question-navigator">
          <h3>Questions</h3>
          <div className="question-grid">
            {questions.map((_, index) => (
              <button
                key={index}
                onClick={() => goToQuestion(index)}
                className={`question-nav-btn ${getQuestionStatus(index)}`}
              >
                {index + 1}
              </button>
            ))}
          </div>
        </div>

        {/* Question Content */}
        <div className="question-content">
          {currentQuestion && (
            <div className="question-card">
              <div className="question-header">
                <h2>Question {currentQuestionIndex + 1}</h2>
                <div className="question-points">
                  {currentQuestion.points || 1} point(s)
                </div>
              </div>

              <div className="question-text">
                {currentQuestion.questionText}
              </div>

              <div className="question-options">
                {currentQuestion.type === 'MULTIPLE_CHOICE' && currentQuestion.options && (
                  <div className="multiple-choice-options">
                    {currentQuestion.options.map((option, index) => (
                      <label key={index} className="option-label">
                        <input
                          type="radio"
                          name={`question-${currentQuestion.id}`}
                          value={option.text}
                          checked={answers[currentQuestion.id] === option.text}
                          onChange={(e) => handleAnswerChange(currentQuestion.id, e.target.value)}
                        />
                        <span className="option-text">{option.text}</span>
                      </label>
                    ))}
                  </div>
                )}

                {currentQuestion.type === 'TRUE_FALSE' && (
                  <div className="true-false-options">
                    <label className="option-label">
                      <input
                        type="radio"
                        name={`question-${currentQuestion.id}`}
                        value="true"
                        checked={answers[currentQuestion.id] === 'true'}
                        onChange={(e) => handleAnswerChange(currentQuestion.id, e.target.value)}
                      />
                      <span className="option-text">True</span>
                    </label>
                    <label className="option-label">
                      <input
                        type="radio"
                        name={`question-${currentQuestion.id}`}
                        value="false"
                        checked={answers[currentQuestion.id] === 'false'}
                        onChange={(e) => handleAnswerChange(currentQuestion.id, e.target.value)}
                      />
                      <span className="option-text">False</span>
                    </label>
                  </div>
                )}

                {currentQuestion.type === 'SHORT_ANSWER' && (
                  <div className="short-answer-input">
                    <textarea
                      className="form-textarea"
                      placeholder="Enter your answer here..."
                      value={answers[currentQuestion.id] || ''}
                      onChange={(e) => handleAnswerChange(currentQuestion.id, e.target.value)}
                      rows={3}
                    />
                  </div>
                )}
              </div>

              <div className="question-navigation">
                <button
                  onClick={goToPreviousQuestion}
                  disabled={currentQuestionIndex === 0}
                  className="btn btn-secondary"
                >
                  ← Previous
                </button>

                {currentQuestionIndex === questions.length - 1 ? (
                  <button
                    onClick={() => handleSubmitQuiz()}
                    className="btn btn-primary"
                    disabled={submitting}
                  >
                    {submitting ? 'Submitting...' : 'Submit Quiz'}
                  </button>
                ) : (
                  <button
                    onClick={goToNextQuestion}
                    className="btn btn-primary"
                  >
                    Next →
                  </button>
                )}
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Submit Confirmation Modal */}
      <Modal
        isOpen={showSubmitModal}
        onClose={() => setShowSubmitModal(false)}
        title="Submit Quiz"
      >
        <p>
          You have answered {getAnsweredQuestionsCount()} out of {questions.length} questions.
        </p>
        <p>
          {getAnsweredQuestionsCount() < questions.length && (
            <strong>Some questions are unanswered. </strong>
          )}
          Are you sure you want to submit your quiz?
        </p>

        <div className="form-actions">
          <button
            onClick={() => setShowSubmitModal(false)}
            className="btn btn-secondary"
          >
            Continue Quiz
          </button>
          <button
            onClick={() => handleSubmitQuiz(true)}
            className="btn btn-primary"
            disabled={submitting}
          >
            {submitting ? 'Submitting...' : 'Submit Quiz'}
          </button>
        </div>
      </Modal>
    </div>
  );
};

export default QuizTake;
