import React from 'react';
import { Link } from 'react-router-dom';
import './Home.css';

const Home = () => {
  return (
    <div className="home-container">
      {/* Navigation Header */}
      <nav className="home-nav">
        <div className="nav-brand">
          <h2>QuizBoot</h2>
        </div>
        <div className="nav-links">
          <Link to="/login" className="nav-link">Login</Link>
          <Link to="/register" className="nav-link nav-link-primary">Get Started</Link>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="hero-section">
        <div className="hero-content">
          <div className="hero-text">
            <h1 className="hero-title">
              Transform Learning with 
              <span className="hero-highlight"> Interactive Quizzes</span>
            </h1>
            <p className="hero-description">
              Create, manage, and take quizzes effortlessly. Perfect for educators, 
              students, and organizations looking to enhance learning experiences.
            </p>
            <div className="hero-buttons">
              <Link to="/register" className="btn btn-primary btn-large">
                Start Learning Today
              </Link>
              <Link to="/login" className="btn btn-secondary btn-large">
                Sign In
              </Link>
            </div>
          </div>
          <div className="hero-image">
            <div className="hero-card">
              <div className="card-header">
                <div className="card-dots">
                  <span></span>
                  <span></span>
                  <span></span>
                </div>
              </div>
              <div className="card-content">
                <div className="quiz-preview">
                  <h3>Sample Quiz</h3>
                  <div className="question-preview">
                    <p>What is 2 + 2?</p>
                    <div className="options">
                      <div className="option">A) 3</div>
                      <div className="option correct">B) 4</div>
                      <div className="option">C) 5</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="features-section">
        <div className="container">
          <h2 className="section-title">Why Choose QuizBoot?</h2>
          <div className="features-grid">
            <div className="feature-card">
              <div className="feature-icon">📚</div>
              <h3>Easy Quiz Creation</h3>
              <p>Create quizzes in minutes with our intuitive interface. Add questions, set timers, and customize settings.</p>
            </div>
            <div className="feature-card">
              <div className="feature-icon">⚡</div>
              <h3>Real-time Results</h3>
              <p>Get instant feedback and detailed analytics to track performance and improve learning outcomes.</p>
            </div>
            <div className="feature-card">
              <div className="feature-icon">👥</div>
              <h3>Multi-user Support</h3>
              <p>Support for students, teachers, and administrators with role-based access control.</p>
            </div>
            <div className="feature-card">
              <div className="feature-icon">📱</div>
              <h3>Responsive Design</h3>
              <p>Works seamlessly on desktop, tablet, and mobile devices for learning anywhere, anytime.</p>
            </div>
            <div className="feature-card">
              <div className="feature-icon">🔒</div>
              <h3>Secure & Private</h3>
              <p>Your data is protected with enterprise-grade security and privacy measures.</p>
            </div>
            <div className="feature-card">
              <div className="feature-icon">📊</div>
              <h3>Advanced Analytics</h3>
              <p>Comprehensive reports and insights to understand learning patterns and progress.</p>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="cta-section">
        <div className="container">
          <div className="cta-content">
            <h2>Ready to Transform Your Learning Experience?</h2>
            <p>Join thousands of educators and students already using QuizBoot</p>
            <Link to="/register" className="btn btn-primary btn-large">
              Get Started for Free
            </Link>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="home-footer">
        <div className="container">
          <div className="footer-content">
            <div className="footer-brand">
              <h3>QuizBoot</h3>
              <p>Making learning interactive and engaging</p>
            </div>
            <div className="footer-links">
              <div className="footer-column">
                <h4>Platform</h4>
                <Link to="/register">Get Started</Link>
                <Link to="/login">Sign In</Link>
              </div>
              <div className="footer-column">
                <h4>Support</h4>
                <a href="#help">Help Center</a>
                <a href="#contact">Contact Us</a>
              </div>
            </div>
          </div>
          <div className="footer-bottom">
            <p>&copy; 2025 QuizBoot. All rights reserved.</p>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default Home;
