import React from 'react';
import { useNavigate } from 'react-router-dom';
import authService from '../services/authService';
import './Dashboard.css';

const TeacherDashboard = () => {
  const navigate = useNavigate();
  const user = authService.getCurrentUser();
  const isAdmin = authService.hasRole('ADMIN');

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
          <span className={`user-role ${isAdmin ? 'admin-role' : 'teacher-role'}`}>
            {isAdmin ? 'Admin' : 'Teacher'}
          </span>
          <button onClick={handleLogout} className="logout-btn">
            Logout
          </button>
        </div>
      </nav>

      <div className="dashboard-content">
        <div className="dashboard-header">
          <h2>{isAdmin ? 'Admin' : 'Teacher'} Dashboard</h2>
          <p>
            {isAdmin 
              ? 'Manage the entire quiz platform and oversee all activities'
              : 'Create and manage quizzes, track student progress!'
            }
          </p>
        </div>

        <div className="dashboard-grid">
          <div className="dashboard-card">
            <div className="card-icon">➕</div>
            <h3>Create Quiz</h3>
            <p>Design new quizzes with questions and answers</p>
            <button className="card-button">Create New Quiz</button>
          </div>

          <div className="dashboard-card">
            <div className="card-icon">📋</div>
            <h3>My Quizzes</h3>
            <p>View and edit your existing quizzes</p>
            <button className="card-button">Manage Quizzes</button>
          </div>

          <div className="dashboard-card">
            <div className="card-icon">📈</div>
            <h3>Student Results</h3>
            <p>Review student performance and analytics</p>
            <button className="card-button">View Results</button>
          </div>

          <div className="dashboard-card">
            <div className="card-icon">👥</div>
            <h3>My Students</h3>
            <p>Manage your students and their progress</p>
            <button className="card-button">View Students</button>
          </div>

          {isAdmin && (
            <>
              <div className="dashboard-card admin-card">
                <div className="card-icon">⚙️</div>
                <h3>System Settings</h3>
                <p>Configure platform settings and preferences</p>
                <button className="card-button">Settings</button>
              </div>

              <div className="dashboard-card admin-card">
                <div className="card-icon">👑</div>
                <h3>User Management</h3>
                <p>Manage all users, teachers, and students</p>
                <button className="card-button">Manage Users</button>
              </div>
            </>
          )}
        </div>

        <div className="stats-section">
          <h3>Quick Stats</h3>
          <div className="stats-grid">
            <div className="stat-card">
              <h4>12</h4>
              <p>Total Quizzes</p>
            </div>
            <div className="stat-card">
              <h4>45</h4>
              <p>Active Students</p>
            </div>
            <div className="stat-card">
              <h4>157</h4>
              <p>Quiz Attempts</p>
            </div>
            <div className="stat-card">
              <h4>82%</h4>
              <p>Average Score</p>
            </div>
          </div>
        </div>

        <div className="recent-activity">
          <h3>Recent Activity</h3>
          <div className="activity-list">
            <div className="activity-item">
              <div className="activity-info">
                <span className="activity-title">John Doe completed "Math Quiz #1"</span>
                <span className="activity-date">2 hours ago</span>
              </div>
              <span className="activity-score">85%</span>
            </div>
            <div className="activity-item">
              <div className="activity-info">
                <span className="activity-title">Sarah Smith started "Science Quiz #3"</span>
                <span className="activity-date">4 hours ago</span>
              </div>
              <span className="activity-score">In Progress</span>
            </div>
            <div className="activity-item">
              <div className="activity-info">
                <span className="activity-title">New student registered: Mike Johnson</span>
                <span className="activity-date">1 day ago</span>
              </div>
              <span className="activity-score">New</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TeacherDashboard;
