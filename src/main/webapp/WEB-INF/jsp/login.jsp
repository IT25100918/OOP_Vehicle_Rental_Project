<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login - Elite Wheel Rentals</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav class="navbar">
    <div class="container">
        <a class="navbar-brand" href="/" style="display:flex;align-items:center;gap:8px;padding:0">
            <img src="${pageContext.request.contextPath}/logo.png" alt="Elite Wheel Rentals"
                 style="height:54px;width:auto;object-fit:contain;filter:drop-shadow(0 2px 10px rgba(212,168,67,0.4))">
        </a>
    </div>
</nav>

<div class="container" style="max-width:440px;margin-top:80px">
    <div class="form-dark">
        <div class="text-center mb-4">
            <div style="font-size:2.5rem">🔑</div>
            <h2 style="font-family:'Rajdhani',sans-serif;font-weight:700">Welcome Back</h2>
            <p style="color:var(--gray);font-size:0.9rem">Sign in to your account</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger-dark mb-3">${error}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert alert-success-dark mb-3">${success}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login">
            <div class="mb-3">
                <label class="form-label">Email Address</label>
                <input type="email" class="form-control" name="email" placeholder="you@example.com" required>
            </div>
            <div class="mb-4">
                <label class="form-label">Password</label>
                <input type="password" class="form-control" name="password" placeholder="••••••••" required>
            </div>
            <button type="submit" class="btn btn-gold w-100">
                <i class="fas fa-sign-in-alt me-2"></i>Login
            </button>
        </form>

        <hr class="divider">
        <div class="text-center">
            <span style="color:var(--gray);font-size:0.9rem">Don't have an account?</span>
            <a href="${pageContext.request.contextPath}/register" class="text-gold ms-1" style="font-weight:600">Register</a>
        </div>
        <div class="mt-3 p-3" style="background:var(--dark-4);border-radius:8px;font-size:0.82rem;color:var(--gray)">
            <strong style="color:var(--gold)">Demo Admin:</strong> admin@vrental.com / admin123
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
