<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard - Elite Wheel Rentals</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav class="navbar navbar-dark" style="position:sticky;top:0;z-index:999">
    <div class="container-fluid px-4">
        <a class="navbar-brand" href="/" style="display:flex;align-items:center;gap:8px;padding:0">
            <img src="${pageContext.request.contextPath}/logo.png" alt="Elite Wheel Rentals"
                 style="height:54px;width:auto;object-fit:contain;filter:drop-shadow(0 2px 10px rgba(212,168,67,0.4))">
        </a>
        <div class="d-flex align-items-center gap-3">
            <span style="color:var(--gray);font-size:0.9rem"><i class="fas fa-shield-alt me-1" style="color:var(--gold)"></i>Admin Panel</span>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-gold btn-sm"><i class="fas fa-sign-out-alt me-1"></i>Logout</a>
        </div>
    </div>
</nav>

<div class="d-flex">
    <div class="sidebar">
        <div class="px-4 mb-3"><div style="font-size:0.75rem;text-transform:uppercase;letter-spacing:1px;color:var(--gray);font-weight:600">Main Menu</div></div>
        <ul class="nav flex-column">
            <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/dashboard"><i class="fas fa-tachometer-alt me-2"></i>Dashboard</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/vehicles"><i class="fas fa-car me-2"></i>Vehicles</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/bookings"><i class="fas fa-calendar-check me-2"></i>Bookings</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/payments"><i class="fas fa-credit-card me-2"></i>Payments</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/users"><i class="fas fa-users me-2"></i>Users</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/feedback"><i class="fas fa-star me-2"></i>Reviews</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/manage"><i class="fas fa-user-shield me-2"></i>Admin Accounts</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/log"><i class="fas fa-history me-2"></i>Activity Log</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/data"><i class="fas fa-database me-2"></i>Data Viewer</a></li>
        </ul>
        <div class="px-4 mt-4"><div style="font-size:0.75rem;text-transform:uppercase;letter-spacing:1px;color:var(--gray);font-weight:600">Account</div></div>
        <ul class="nav flex-column mt-1">
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/users/profile"><i class="fas fa-user-edit me-2"></i>Profile</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt me-2"></i>Logout</a></li>
        </ul>
    </div>

    <div class="main-content">
        <div class="page-header">
            <h1>Admin <span>Dashboard</span></h1>
            <p style="color:var(--gray);margin-top:4px">Welcome back, <span class="text-gold">${user.name}</span></p>
        </div>

        <div class="row g-4 mb-4">
            <div class="col-xl-2 col-md-4 col-6"><div class="stat-card"><div class="stat-icon">🚗</div><div class="stat-value">${totalVehicles}</div><div class="stat-label">Total Vehicles</div></div></div>
            <div class="col-xl-2 col-md-4 col-6"><div class="stat-card"><div class="stat-icon">✅</div><div class="stat-value">${availableVehicles}</div><div class="stat-label">Available</div></div></div>
            <div class="col-xl-2 col-md-4 col-6"><div class="stat-card"><div class="stat-icon">📋</div><div class="stat-value">${totalBookings}</div><div class="stat-label">Total Bookings</div></div></div>
            <div class="col-xl-2 col-md-4 col-6"><div class="stat-card"><div class="stat-icon">🔄</div><div class="stat-value">${activeBookings}</div><div class="stat-label">Active Bookings</div></div></div>
            <div class="col-xl-2 col-md-4 col-6"><div class="stat-card"><div class="stat-icon">👥</div><div class="stat-value">${totalUsers}</div><div class="stat-label">Total Users</div></div></div>
            <div class="col-xl-2 col-md-4 col-6"><div class="stat-card"><div class="stat-icon">💰</div><div class="stat-value" style="font-size:1.3rem">Rs.<fmt:formatNumber value="${totalRevenue}" pattern="#,##0"/></div><div class="stat-label">Revenue</div></div></div>
        </div>

        <div class="row g-3 mb-4">
            <div class="col-auto"><a href="${pageContext.request.contextPath}/vehicles/add" class="btn btn-gold"><i class="fas fa-plus me-2"></i>Add Vehicle</a></div>
            <div class="col-auto"><a href="${pageContext.request.contextPath}/bookings" class="btn btn-outline-gold"><i class="fas fa-list me-2"></i>All Bookings</a></div>
            <div class="col-auto"><a href="${pageContext.request.contextPath}/payments" class="btn btn-outline-gold"><i class="fas fa-money-bill me-2"></i>Payments</a></div>
            <div class="col-auto"><a href="${pageContext.request.contextPath}/users" class="btn btn-outline-gold"><i class="fas fa-users me-2"></i>Manage Users</a></div>
            <div class="col-auto"><a href="${pageContext.request.contextPath}/admin/manage" class="btn btn-outline-gold"><i class="fas fa-user-shield me-2"></i>Admin Accounts</a></div>
            <div class="col-auto"><a href="${pageContext.request.contextPath}/admin/log" class="btn btn-outline-gold"><i class="fas fa-history me-2"></i>Activity Log</a></div>
            <div class="col-auto"><a href="${pageContext.request.contextPath}/admin/data" class="btn btn-outline-gold"><i class="fas fa-database me-2"></i>Data Viewer</a></div>
        </div>

        <div class="table-dark-custom">
            <div class="px-4 py-3" style="border-bottom:1px solid var(--border)">
                <strong style="font-family:'Rajdhani',sans-serif;font-size:1.1rem">Recent Bookings</strong>
            </div>
            <table class="table table-hover mb-0">
                <thead>
                    <tr><th>Booking ID</th><th>Customer</th><th>Vehicle</th><th>Dates</th><th>Cost</th><th>Status</th><th>Action</th></tr>
                </thead>
                <tbody>
                    <c:forEach var="booking" items="${recentBookings}">
                        <tr>
                            <td><code style="color:var(--gold)">${booking.bookingId}</code></td>
                            <td>${booking.userName}</td>
                            <td>${booking.vehicleInfo}</td>
                            <td>${booking.startDate} → ${booking.endDate}</td>
                            <td>Rs. ${booking.totalCost}</td>
                            <td><span class="status-badge status-${booking.status}">${booking.status}</span></td>
                            <td>
                                <c:if test="${booking.status == 'confirmed'}">
                                    <a href="${pageContext.request.contextPath}/bookings/complete/${booking.bookingId}" class="btn btn-gold btn-sm">Complete</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty recentBookings}">
                        <tr><td colspan="7" class="text-center" style="color:var(--gray);padding:24px">No bookings yet</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<%@ include file="/WEB-INF/jsp/chatbot.jsp" %>
</body>
</html>
