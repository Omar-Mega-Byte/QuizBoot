import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import StudentDashboard from './pages/StudentDashboard';
import TeacherDashboard from './pages/TeacherDashboard';
import QuizList from './pages/QuizList';
import QuizForm from './pages/QuizForm';
import QuizDetails from './pages/QuizDetails';
import CategoryList from './pages/CategoryList';
import QuestionForm from './pages/QuestionForm';
import StudentQuizList from './pages/StudentQuizList';
import QuizTake from './pages/QuizTake';
import ProtectedRoute from './components/ProtectedRoute';
import authService from './services/authService';
import './App.css';

// Landing page component
const Landing = () => {
  return <Home />;
};

// Unauthorized page component
const Unauthorized = () => {
  return (
    <div className="error-container">
      <h2>Unauthorized Access</h2>
      <p>You don't have permission to access this page.</p>
      <a href="/login" className="landing-btn primary">Go to Login</a>
    </div>
  );
};

// Auto redirect component based on role
const AutoRedirect = () => {
  const isAuthenticated = authService.isAuthenticated();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (authService.hasRole('ADMIN') || authService.hasRole('TEACHER')) {
    return <Navigate to="/teacher-dashboard" replace />;
  } else {
    return <Navigate to="/student-dashboard" replace />;
  }
};

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          {/* Public routes */}
          <Route path="/" element={<Landing />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/unauthorized" element={<Unauthorized />} />

          {/* Auto redirect route */}
          <Route path="/dashboard" element={<AutoRedirect />} />

          {/* Protected routes */}
          <Route
            path="/student-dashboard"
            element={
              <ProtectedRoute requiredRole="STUDENT">
                <StudentDashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/teacher-dashboard"
            element={
              <ProtectedRoute>
                <TeacherDashboard />
              </ProtectedRoute>
            }
          />

          {/* Quiz Management Routes - Protected for Teachers/Admins */}
          <Route
            path="/quiz"
            element={
              <ProtectedRoute requiredRole="TEACHER">
                <QuizList />
              </ProtectedRoute>
            }
          />
          <Route
            path="/quiz/create"
            element={
              <ProtectedRoute requiredRole="TEACHER">
                <QuizForm />
              </ProtectedRoute>
            }
          />
          <Route
            path="/quiz/:id"
            element={
              <ProtectedRoute requiredRole="TEACHER">
                <QuizDetails />
              </ProtectedRoute>
            }
          />
          <Route
            path="/quiz/:id/edit"
            element={
              <ProtectedRoute requiredRole="TEACHER">
                <QuizForm />
              </ProtectedRoute>
            }
          />
          <Route
            path="/quiz/:quizId/questions/create"
            element={
              <ProtectedRoute requiredRole="TEACHER">
                <QuestionForm />
              </ProtectedRoute>
            }
          />
          <Route
            path="/quiz/:quizId/questions/:questionId/edit"
            element={
              <ProtectedRoute requiredRole="TEACHER">
                <QuestionForm />
              </ProtectedRoute>
            }
          />

          {/* Category Management Routes - Protected for Teachers/Admins */}
          <Route
            path="/categories"
            element={
              <ProtectedRoute requiredRole="TEACHER">
                <CategoryList />
              </ProtectedRoute>
            }
          />

          {/* Student Quiz Routes - Protected for Students */}
          <Route
            path="/student/quizzes"
            element={
              <ProtectedRoute requiredRole="STUDENT">
                <StudentQuizList />
              </ProtectedRoute>
            }
          />
          <Route
            path="/student/quiz/:quizId/take"
            element={
              <ProtectedRoute requiredRole="STUDENT">
                <QuizTake />
              </ProtectedRoute>
            }
          />

          {/* Catch all route */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
