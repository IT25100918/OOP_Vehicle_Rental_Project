<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Booking Confirmed - Elite Wheel Rentals</title>
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
        <a href="${pageContext.request.contextPath}/bookings" class="btn btn-outline-gold btn-sm">My Bookings</a>
    </div>
</nav>

<div class="container" style="max-width:640px;margin-top:60px;padding-bottom:60px;text-align:center">
    <div style="width:80px;height:80px;border-radius:50%;background:rgba(40,167,69,0.15);border:2px solid #28a745;display:flex;align-items:center;justify-content:center;margin:0 auto 24px">
        <i class="fas fa-check" style="font-size:2rem;color:#28a745"></i>
    </div>
    <h1 style="font-family:'Rajdhani',sans-serif;font-weight:700;margin-bottom:8px">
        Booking <span class="text-gold">Confirmed!</span>
    </h1>
    <p style="color:var(--gray);margin-bottom:32px">Your reservation has been successfully recorded.</p>

    <div class="card-dark text-start mb-4">
        <div class="d-flex align-items-center gap-3 mb-3">
            <i class="fas fa-receipt text-gold" style="font-size:1.25rem"></i>
            <h5 style="margin:0;font-family:'Rajdhani',sans-serif;font-weight:600">Booking Summary</h5>
        </div>
        <hr class="divider">
        <table style="width:100%;font-size:0.95rem">
            <tr><td style="color:var(--gray);padding:6px 0">Booking ID</td><td style="text-align:right;font-weight:600">${booking.bookingId}</td></tr>
            <tr><td style="color:var(--gray);padding:6px 0">Vehicle</td><td style="text-align:right;font-weight:600">${booking.vehicleInfo}</td></tr>
            <tr><td style="color:var(--gray);padding:6px 0">Customer</td><td style="text-align:right">${booking.userName}</td></tr>
            <tr><td style="color:var(--gray);padding:6px 0">Start Date</td><td style="text-align:right">${booking.startDate}</td></tr>
            <tr><td style="color:var(--gray);padding:6px 0">End Date</td><td style="text-align:right">${booking.endDate}</td></tr>
            <tr><td style="color:var(--gray);padding:6px 0">Duration</td><td style="text-align:right">${booking.totalDays} day(s)</td></tr>
            <tr><td style="color:var(--gray);padding:6px 0">Booking Type</td><td style="text-align:right;text-transform:capitalize">${booking.bookingType}</td></tr>
            <tr><td style="color:var(--gray);padding:6px 0">Status</td>
                <td style="text-align:right">
                    <span style="background:rgba(40,167,69,0.15);color:#28a745;padding:2px 10px;border-radius:20px;font-size:0.85rem;font-weight:600">Confirmed</span>
                </td>
            </tr>
            <tr>
                <td style="color:var(--gold);font-weight:700;padding:10px 0 4px">Total Cost</td>
                <td style="text-align:right;color:var(--gold);font-weight:700;font-size:1.15rem">
                    Rs. <fmt:formatNumber value="${booking.totalCost}" pattern="#,##0.00"/>
                </td>
            </tr>
        </table>
    </div>

    <div class="d-flex gap-3 justify-content-center flex-wrap">
        <a href="${pageContext.request.contextPath}/payments/pay/${booking.bookingId}" class="btn btn-gold">
            <i class="fas fa-credit-card me-2"></i>Pay Now
        </a>
        <a href="${pageContext.request.contextPath}/bookings" class="btn btn-outline-gold">
            <i class="fas fa-list me-2"></i>View All Bookings
        </a>
        <a href="${pageContext.request.contextPath}/vehicles" class="btn btn-outline-gold">
            <i class="fas fa-car me-2"></i>Browse More Vehicles
        </a>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<%@ include file="/WEB-INF/jsp/chatbot.jsp" %>
</body>
</html>
