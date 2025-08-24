import React from 'react';
import { useNavigate } from 'react-router-dom';
import authService from '../services/authService';
import './Dashboard.css';

const StudentDashboard = () => {
  const navigate = useNavigate();
  const user = authService.getCurrentUser();

  const handleLogout = () => {
    authService.logout();
    navigate('/login');
  };

  return (
    <div className="dashboard-container">
      <nav className="dashboard-nav">
        <div className="nav-brand">
          <h1>Quiz Boot</h1>
        </div>
        <div className="nav-user">
          <span className="user-welcome">
            Welcome, {user?.firstName} {user?.lastName}
          </span>
          <span className="user-role student-role">Student</span>
          <button onClick={handleLogout} className="logout-btn">
            Logout
          </button>
        </div>
      </nav>

      <div className="dashboard-content">
        <div className="dashboard-header">
          <h2>Student Dashboard</h2>
          <p>Take quizzes, view your progress, and improve your knowledge!</p>
        </div>

        <div className="dashboard-grid">
          <div className="dashboard-card">
            <div className="card-icon">📝</div>
            <h3>Available Quizzes</h3>
            <p>Browse and take quizzes created by your teachers</p>
            <button className="card-button">View Quizzes</button>
          </div>

          <div className="dashboard-card">
            <div className="card-icon">📊</div>
            <h3>My Results</h3>
            <p>Check your quiz scores and performance history</p>
            <button className="card-button">View Results</button>
          </div>

          <div className="dashboard-card">
            <div className="card-icon">🏆</div>
            <h3>Achievements</h3>
            <p>Track your learning milestones and achievements</p>
            <button className="card-button">View Achievements</button>
          </div>

          <div className="dashboard-card">
            <div className="card-icon">👤</div>
            <h3>Profile</h3>
            <p>Update your personal information and preferences</p>
            <button className="card-button">Edit Profile</button>
          </div>
        </div>

        <div className="recent-activity">
          <h3>Recent Activity</h3>
          <div className="activity-list">
            <div className="activity-item">
              <div className="activity-info">
                <span className="activity-title">Math Quiz #1</span>
                <span className="activity-date">Completed 2 days ago</span>
              </div>
              <span className="activity-score">85%</span>
            </div>
            <div className="activity-item">
              <div className="activity-info">
                <span className="activity-title">Science Quiz #3</span>
                <span className="activity-date">Completed 5 days ago</span>
              </div>
              <span className="activity-score">92%</span>
            </div>
            <div className="activity-item">
              <div className="activity-info">
                <span className="activity-title">History Quiz #2</span>
                <span className="activity-date">Completed 1 week ago</span>
              </div>
              <span className="activity-score">78%</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default StudentDashboard;
