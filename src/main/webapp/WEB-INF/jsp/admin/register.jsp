<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register Admin - Elite Wheel Rentals</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav class="navbar navbar-dark">
    <div class="container">
        <a class="navbar-brand" href="/" style="display:flex;align-items:center;gap:8px;padding:0">
            <img src="${pageContext.request.contextPath}/logo.png" alt="Elite Wheel Rentals"
                 style="height:54px;width:auto;object-fit:contain;filter:drop-shadow(0 2px 10px rgba(212,168,67,0.4))">
        </a>
        <a href="${pageContext.request.contextPath}/admin/manage" class="btn btn-outline-gold btn-sm">← Admin Management</a>
    </div>
</nav>

<div class="container" style="max-width:560px;margin-top:48px;padding-bottom:60px">
    <div class="form-dark">
        <div class="mb-4">
            <h2 style="font-family:'Rajdhani',sans-serif;font-weight:700">Register <span class="text-gold">Admin Account</span></h2>
            <p style="color:var(--gray);margin:0">Create a new administrator with specific role and permissions.</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger-dark mb-3">${error}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert alert-success-dark mb-3">${success}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/admin/register">
            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label">Full Name</label>
                    <input type="text" class="form-control" name="name" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">NIC Number</label>
                    <input type="text" class="form-control" name="nic" required>
                </div>
                <div class="col-12">
                    <label class="form-label">Email Address</label>
                    <input type="email" class="form-control" name="email" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Contact</label>
                    <input type="text" class="form-control" name="contact" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Password</label>
                    <input type="password" class="form-control" name="password" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Role</label>
                    <select class="form-select" name="role">
                        <option value="SuperAdmin">Super Admin</option>
                        <option value="FleetManager">Fleet Manager</option>
                        <option value="BookingManager">Booking Manager</option>
                        <option value="SupportStaff">Support Staff</option>
                    </select>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Permissions</label>
                    <select class="form-select" name="permissions">
                        <option value="ALL">All Permissions</option>
                        <option value="VEHICLES,BOOKINGS">Vehicles &amp; Bookings</option>
                        <option value="BOOKINGS,PAYMENTS">Bookings &amp; Payments</option>
                        <option value="FEEDBACK">Feedback Only</option>
                    </select>
                </div>
                <div class="col-12 mt-2">
                    <button type="submit" class="btn btn-gold w-100">
                        <i class="fas fa-user-shield me-2"></i>Create Admin Account
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<%@ include file="/WEB-INF/jsp/chatbot.jsp" %>
</body>
</html>
