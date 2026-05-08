<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register - Elite Wheel Rentals</title>
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

<div class="container" style="max-width:520px;margin-top:60px;padding-bottom:60px">
    <div class="form-dark">
        <div class="text-center mb-4">
            <div style="font-size:2.5rem">🚗</div>
            <h2 style="font-family:'Rajdhani',sans-serif;font-weight:700">Create Account</h2>
            <p style="color:var(--gray);font-size:0.9rem">Join Elite Wheel Rentals today</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger-dark mb-3">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/register">
            <div class="row g-3">
                <div class="col-12">
                    <label class="form-label">Full Name</label>
                    <input type="text" class="form-control" name="name" placeholder="John Doe" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">NIC Number</label>
                    <input type="text" class="form-control" name="nic" placeholder="000000000V" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Contact</label>
                    <input type="text" class="form-control" name="contact" placeholder="077XXXXXXX" required>
                </div>
                <div class="col-12">
                    <label class="form-label">Email Address</label>
                    <input type="email" class="form-control" name="email" placeholder="you@example.com" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Password</label>
                    <input type="password" class="form-control" name="password" placeholder="••••••••" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Driving Licence No.</label>
                    <input type="text" class="form-control" name="licenceNo" placeholder="LIC-XXXXX" required>
                </div>
                <div class="col-12 mt-2">
                    <button type="submit" class="btn btn-gold w-100">
                        <i class="fas fa-user-plus me-2"></i>Create Account
                    </button>
                </div>
            </div>
        </form>

        <hr class="divider">
        <div class="text-center">
            <span style="color:var(--gray);font-size:0.9rem">Already have an account?</span>
            <a href="${pageContext.request.contextPath}/login" class="text-gold ms-1" style="font-weight:600">Login</a>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
