# JWT Authentication API Documentation

This document describes the JWT authentication endpoints implemented in the Quiz Boot application.

## Endpoints

### 1. User Registration

**POST** `/api/auth/register`

Register a new user and receive a JWT token.

**Request Body:**
```json
{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "SecurePassword123",
    "firstName": "John",
    "lastName": "Doe"
}
```

**Response:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "user": {
        "id": 1,
        "username": "john_doe",
        "email": "john@example.com",
        "firstName": "John",
        "lastName": "Doe",
        "active": true,
        "createdAt": "2023-08-24T10:30:00Z",
        "updatedAt": "2023-08-24T10:30:00Z"
    }
}
```

**Status Codes:**
- `201 Created` - User successfully registered
- `400 Bad Request` - Validation errors or username/email already exists
- `500 Internal Server Error` - Database or server error

### 2. User Login

**POST** `/api/auth/login`

Authenticate user and receive a JWT token.

**Request Body:**
```json
{
    "username": "john_doe",
    "password": "SecurePassword123"
}
```

**Response:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "user": {
        "id": 1,
        "username": "john_doe",
        "email": "john@example.com",
        "firstName": "John",
        "lastName": "Doe",
        "active": true,
        "createdAt": "2023-08-24T10:30:00Z",
        "updatedAt": "2023-08-24T10:30:00Z"
    }
}
```

**Status Codes:**
- `200 OK` - User successfully authenticated
- `400 Bad Request` - Invalid credentials or validation errors
- `500 Internal Server Error` - Database or server error

## JWT Token Usage

### Making Authenticated Requests

Include the JWT token in the `Authorization` header:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Token Expiration

- Default expiration: 24 hours (86400000 ms)
- Configurable via `app.jwt.expirationMs` property

## Configuration

Add the following properties to your `application.yml`:

```yaml
app:
  jwt:
    secret: ${JWT_SECRET:mySecretKeyForJWTTokenGenerationThatShouldBeAtLeast256BitsLong}
    expirationMs: ${JWT_EXPIRATION_MS:86400000} # 24 hours
```

## Security Features

1. **Password Hashing**: Passwords are hashed using BCrypt
2. **JWT Signing**: Tokens are signed with HMAC SHA-256
3. **Input Validation**: All inputs are validated before processing
4. **Audit Logging**: All authentication attempts are logged
5. **Stateless Authentication**: No server-side sessions required

## Error Responses

All error responses follow this format:

```json
{
    "timestamp": "2023-08-24T10:30:00Z",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed",
    "path": "/api/auth/register"
}
```

## Examples

### Using cURL

**Register a new user:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "SecurePassword123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "SecurePassword123"
  }'
```

**Making authenticated requests:**
```bash
curl -X GET http://localhost:8080/api/protected-endpoint \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```
