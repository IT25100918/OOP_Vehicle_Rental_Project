<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Bookings - Elite Wheel Rentals</title>
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
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/dashboard" class="nav-link" style="color:var(--gray)">Dashboard</a>
            <a href="${pageContext.request.contextPath}/vehicles" class="btn btn-gold btn-sm">Browse Vehicles</a>
        </div>
    </div>
</nav>

<div class="container py-4">
    <div class="page-header">
        <h1><c:if test="${user.userType == 'admin'}">All </c:if>Bookings <c:if test="${user.userType != 'admin'}">History</c:if></h1>
    </div>

    <div class="table-dark-custom">
        <table class="table table-hover mb-0">
            <thead>
                <tr>
                    <th>Booking ID</th><th>Customer</th><th>Vehicle</th><th>Start</th><th>End</th>
                    <th>Days</th><th>Total (Rs.)</th><th>Type</th><th>Status</th><th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="booking" items="${bookings}">
                    <tr>
                        <td><code style="color:var(--gold)">${booking.bookingId}</code></td>
                        <td>${booking.userName}</td>
                        <td>${booking.vehicleInfo}</td>
                        <td>${booking.startDate}</td>
                        <td>${booking.endDate}</td>
                        <td>${booking.totalDays}</td>
                        <td>${booking.totalCost}</td>
                        <td style="text-transform:capitalize">${booking.bookingType}</td>
                        <td><span class="status-badge status-${booking.status}">${booking.status}</span></td>
                        <td>
                            <div class="d-flex gap-1">
                                <c:if test="${booking.status == 'confirmed'}">
                                    <a href="${pageContext.request.contextPath}/bookings/cancel/${booking.bookingId}"
                                       class="btn btn-danger-dark btn-sm"
                                       onclick="return confirm('Cancel this booking?')">Cancel</a>
                                </c:if>
                                <c:if test="${booking.status == 'confirmed' and user.userType == 'admin'}">
                                    <a href="${pageContext.request.contextPath}/bookings/complete/${booking.bookingId}"
                                       class="btn btn-gold btn-sm">Complete</a>
                                </c:if>
                                <c:if test="${booking.status == 'completed'}">
                                    <a href="${pageContext.request.contextPath}/payments/pay/${booking.bookingId}"
                                       class="btn btn-outline-gold btn-sm">Pay</a>
                                </c:if>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty bookings}">
                    <tr>
                        <td colspan="10" class="text-center" style="color:var(--gray);padding:32px">
                            No bookings found. <a href="${pageContext.request.contextPath}/vehicles" class="text-gold">Browse vehicles</a>
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<footer><div class="container"><p>© 2025 <span>Elite Wheel Rentals</span></p></div></footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<%@ include file="/WEB-INF/jsp/chatbot.jsp" %>
</body>
</html>
