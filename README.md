# Component 2: Authentication & Customer Management

## What This Component Contains
Full standalone Spring Boot application focused on **user authentication and customer management**:

### Auth Features
- **Register** — New user registration with BCrypt hashed password
- **Login** — Supports both Admin and User login via single form
- **Forgot Password** — Email-based password reset
- **Logout** — Session invalidation

### Customer Features
- **List Users** — Admin-only view of all registered customers
- **Edit User** — Update profile (name, email, phone, licence number)
- **Change Password** — Secure password change with current-password verification
- **Delete User** — Admin or self-delete

### Key Files
- `customer/User.java` — User entity
- `customer/UserController.java` — Auth + CRUD routes + all REST API endpoints + Dashboard
- `customer/UserService.java` — Business logic
- `customer/UserRepository.java` — File persistence
- `templates/auth/` — login, register, forgot-password pages
- `templates/customer/` — index, edit-users pages

## How to Run
```bash
cd backend
mvn spring-boot:run
```
Visit: http://localhost:8080/login

**Default Admin:** Check `data/admins.txt` for credentials.
