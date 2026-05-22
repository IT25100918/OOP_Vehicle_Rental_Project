<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Dashboard - Elite Wheel Rentals</title>
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
        <div class="d-flex gap-3">
            <a href="${pageContext.request.contextPath}/" class="nav-link" style="color:var(--gray)">Home</a>
            <a href="${pageContext.request.contextPath}/vehicles" class="nav-link" style="color:var(--gray)">Vehicles</a>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-gold btn-sm">Logout</a>
        </div>
    </div>
</nav>

<div class="d-flex">
    <div class="sidebar">
        <ul class="nav flex-column">
            <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/dashboard"><i class="fas fa-home me-2"></i>Dashboard</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/vehicles"><i class="fas fa-car me-2"></i>Browse Vehicles</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/bookings"><i class="fas fa-calendar me-2"></i>My Bookings</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/payments"><i class="fas fa-receipt me-2"></i>My Payments</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/feedback"><i class="fas fa-star me-2"></i>Reviews</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/users/profile"><i class="fas fa-user me-2"></i>My Profile</a></li>
        </ul>
    </div>

    <div class="main-content">
        <div class="page-header">
            <h1>My <span>Dashboard</span></h1>
            <p style="color:var(--gray)">Welcome, <span class="text-gold">${user.name}</span></p>
        </div>

        <div class="row g-4 mb-4">
            <div class="col-md-4">
                <div class="stat-card">
                    <div class="stat-icon">📋</div>
                    <div class="stat-value">${totalBookings}</div>
                    <div class="stat-label">My Bookings</div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="stat-card">
                    <div class="stat-icon">🔄</div>
                    <div class="stat-value">${activeBookings}</div>
                    <div class="stat-label">Active Bookings</div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="stat-card">
                    <div class="stat-icon">🚗</div>
                    <div class="stat-value">${availableVehicles}</div>
                    <div class="stat-label">Available Vehicles</div>
                </div>
            </div>
        </div>

        <div class="row g-3 mb-4">
            <div class="col-auto"><a href="${pageContext.request.contextPath}/vehicles" class="btn btn-gold"><i class="fas fa-search me-2"></i>Browse &amp; Book</a></div>
            <div class="col-auto"><a href="${pageContext.request.contextPath}/bookings" class="btn btn-outline-gold"><i class="fas fa-list me-2"></i>My Bookings</a></div>
            <div class="col-auto"><a href="${pageContext.request.contextPath}/feedback/add" class="btn btn-outline-gold"><i class="fas fa-star me-2"></i>Leave Review</a></div>
        </div>

        <div class="table-dark-custom">
            <div class="px-4 py-3" style="border-bottom:1px solid var(--border)">
                <strong style="font-family:'Rajdhani',sans-serif;font-size:1.1rem">Recent Bookings</strong>
            </div>
            <table class="table table-hover mb-0">
                <thead>
                    <tr><th>Booking ID</th><th>Vehicle</th><th>Dates</th><th>Cost</th><th>Status</th></tr>
                </thead>
                <tbody>
                    <c:forEach var="booking" items="${recentBookings}">
                        <tr>
                            <td><code style="color:var(--gold)">${booking.bookingId}</code></td>
                            <td>${booking.vehicleInfo}</td>
                            <td>${booking.startDate} → ${booking.endDate}</td>
                            <td>Rs. ${booking.totalCost}</td>
                            <td><span class="status-badge status-${booking.status}">${booking.status}</span></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty recentBookings}">
                        <tr>
                            <td colspan="5" class="text-center" style="color:var(--gray);padding:24px">
                                No bookings yet. <a href="${pageContext.request.contextPath}/vehicles" class="text-gold">Book a vehicle!</a>
                            </td>
                        </tr>
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
