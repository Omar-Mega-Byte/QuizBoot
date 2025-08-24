import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import authService from '../services/authService';
import './Auth.css';

const Register = () => {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
    firstName: '',
    lastName: '',
    role: 'STUDENT',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [passwordValidation, setPasswordValidation] = useState({
    length: false,
    uppercase: false,
    lowercase: false,
    number: false,
    special: false,
    match: false
  });
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const navigate = useNavigate();

  // Real-time password validation
  useEffect(() => {
    const password = formData.password;
    const confirmPassword = formData.confirmPassword;

    setPasswordValidation({
      length: password.length >= 8,
      uppercase: /[A-Z]/.test(password),
      lowercase: /[a-z]/.test(password),
      number: /\d/.test(password),
      special: /[@$!%*?&_]/.test(password),
      match: password === confirmPassword && password.length > 0 && confirmPassword.length > 0
    });
  }, [formData.password, formData.confirmPassword]);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
    setError('');
  };

  const getPasswordStrength = () => {
    const validCount = Object.values(passwordValidation).filter(Boolean).length - 1; // Exclude match
    if (validCount <= 1) return { strength: 'weak', width: '20%', color: '#ff4757' };
    if (validCount <= 3) return { strength: 'medium', width: '60%', color: '#ffa502' };
    if (validCount === 4) return { strength: 'strong', width: '80%', color: '#2ed573' };
    return { strength: 'very-strong', width: '100%', color: '#5352ed' };
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    // Basic validation
    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match');
      setLoading(false);
      return;
    }

    // Validate password complexity (matching backend requirements)
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&_])[A-Za-z\d@$!%*?&_]{8,}$/;
    if (!passwordRegex.test(formData.password)) {
      setError('Password must meet all requirements');
      setLoading(false);
      return;
    }

    try {
      const registrationData = {
        username: formData.username,
        email: formData.email,
        password: formData.password,
        firstName: formData.firstName,
        lastName: formData.lastName,
        role: formData.role,
      };

      const response = await authService.register(registrationData);

      // Redirect based on user role
      if (authService.hasRole('ADMIN') || authService.hasRole('TEACHER')) {
        navigate('/teacher-dashboard');
      } else {
        navigate('/student-dashboard');
      }
    } catch (error) {
      setError(error);
    } finally {
      setLoading(false);
    }
  };

  const passwordStrength = getPasswordStrength();

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-header">
          <h1>Quiz Boot</h1>
          <h2>Register</h2>
        </div>

        <form onSubmit={handleSubmit} className="auth-form">
          {error && <div className="error-message">{error}</div>}

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="firstName">First Name</label>
              <input
                type="text"
                id="firstName"
                name="firstName"
                value={formData.firstName}
                onChange={handleChange}
                required
                placeholder="Enter your first name"
              />
            </div>

            <div className="form-group">
              <label htmlFor="lastName">Last Name</label>
              <input
                type="text"
                id="lastName"
                name="lastName"
                value={formData.lastName}
                onChange={handleChange}
                required
                placeholder="Enter your last name"
              />
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="username">Username</label>
            <input
              type="text"
              id="username"
              name="username"
              value={formData.username}
              onChange={handleChange}
              required
              placeholder="Choose a username"
            />
          </div>

          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
              placeholder="Enter your email"
            />
          </div>

          <div className="form-group">
            <label htmlFor="role">Role</label>
            <select
              id="role"
              name="role"
              value={formData.role}
              onChange={handleChange}
              required
              className="role-select"
            >
              <option value="STUDENT">Student</option>
              <option value="TEACHER">Teacher</option>
              <option value="ADMIN">Admin</option>
            </select>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="password">Password</label>
              <div className="password-input-container">
                <input
                  type={showPassword ? "text" : "password"}
                  id="password"
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  required
                  placeholder="Create a password"
                  className="password-input"
                />
                <button
                  type="button"
                  className="password-toggle"
                  onClick={() => setShowPassword(!showPassword)}
                >
                  {showPassword ? '👁️' : '👁️‍🗨️'}
                </button>
              </div>
              
              {/* Password Strength Indicator */}
              {formData.password && (
                <div className="password-strength-container">
                  <div className="password-strength-bar">
                    <div 
                      className="password-strength-fill"
                      style={{ 
                        width: passwordStrength.width, 
                        backgroundColor: passwordStrength.color,
                        transition: 'all 0.3s ease'
                      }}
                    ></div>
                  </div>
                  <span className="password-strength-text" style={{ color: passwordStrength.color }}>
                    {passwordStrength.strength.replace('-', ' ').toUpperCase()}
                  </span>
                </div>
              )}

              {/* Interactive Password Requirements */}
              {formData.password && (
                <div className="password-requirements-interactive">
                  <h4>Password Requirements:</h4>
                  <div className="requirement-grid">
                    <div className={`requirement-item ${passwordValidation.length ? 'valid' : 'invalid'}`}>
                      <span className="requirement-icon">
                        {passwordValidation.length ? '✅' : '❌'}
                      </span>
                      <span className="requirement-text">At least 8 characters</span>
                    </div>
                    
                    <div className={`requirement-item ${passwordValidation.uppercase ? 'valid' : 'invalid'}`}>
                      <span className="requirement-icon">
                        {passwordValidation.uppercase ? '✅' : '❌'}
                      </span>
                      <span className="requirement-text">One uppercase letter (A-Z)</span>
                    </div>
                    
                    <div className={`requirement-item ${passwordValidation.lowercase ? 'valid' : 'invalid'}`}>
                      <span className="requirement-icon">
                        {passwordValidation.lowercase ? '✅' : '❌'}
                      </span>
                      <span className="requirement-text">One lowercase letter (a-z)</span>
                    </div>
                    
                    <div className={`requirement-item ${passwordValidation.number ? 'valid' : 'invalid'}`}>
                      <span className="requirement-icon">
                        {passwordValidation.number ? '✅' : '❌'}
                      </span>
                      <span className="requirement-text">One number (0-9)</span>
                    </div>
                    
                    <div className={`requirement-item ${passwordValidation.special ? 'valid' : 'invalid'}`}>
                      <span className="requirement-icon">
                        {passwordValidation.special ? '✅' : '❌'}
                      </span>
                      <span className="requirement-text">One special character (@$!%*?&_)</span>
                    </div>
                  </div>
                </div>
              )}
            </div>

            <div className="form-group">
              <label htmlFor="confirmPassword">Confirm Password</label>
              <div className="password-input-container">
                <input
                  type={showConfirmPassword ? "text" : "password"}
                  id="confirmPassword"
                  name="confirmPassword"
                  value={formData.confirmPassword}
                  onChange={handleChange}
                  required
                  placeholder="Confirm your password"
                  className="password-input"
                />
                <button
                  type="button"
                  className="password-toggle"
                  onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                >
                  {showConfirmPassword ? '👁️' : '👁️‍🗨️'}
                </button>
              </div>
              
              {/* Password Match Indicator */}
              {formData.confirmPassword && (
                <div className={`password-match ${passwordValidation.match ? 'match' : 'no-match'}`}>
                  <span className="match-icon">
                    {passwordValidation.match ? '✅' : '❌'}
                  </span>
                  <span className="match-text">
                    {passwordValidation.match ? 'Passwords match!' : 'Passwords do not match'}
                  </span>
                </div>
              )}
            </div>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="auth-button"
          >
            {loading ? (
              <span className="loading-content">
                <span className="loading-spinner">⏳</span>
                Creating Account...
              </span>
            ) : (
              'Create Account'
            )}
          </button>
        </form>

        <div className="auth-footer">
          <p>
            Already have an account?{' '}
            <Link to="/login" className="auth-link">
              Login here
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Register;
