<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Payments - Elite Wheel Rentals</title>
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
        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-gold btn-sm">Dashboard</a>
    </div>
</nav>

<div class="container py-4">
    <div class="page-header">
        <div class="d-flex justify-content-between align-items-center">
            <h1>Payment <span>Records</span></h1>
            <c:if test="${user.userType == 'admin'}">
                <div class="stat-card" style="padding:12px 24px;text-align:center">
                    <div style="color:var(--gray);font-size:0.8rem">Total Revenue</div>
                    <div style="font-family:'Rajdhani',sans-serif;font-size:1.6rem;color:var(--gold);font-weight:700">
                        Rs. <fmt:formatNumber value="${totalRevenue}" pattern="#,##0.00"/>
                    </div>
                </div>
            </c:if>
        </div>
    </div>

    <!-- Sort controls (admin only) -->
    <c:if test="${user.userType == 'admin'}">
        <div class="d-flex gap-2 mb-3">
            <span style="color:var(--gray);align-self:center">Sort by:</span>
            <a href="${pageContext.request.contextPath}/payments?sortBy=date" class="btn btn-sm ${'date' == sortBy ? 'btn-gold' : 'btn-outline-gold'}">Date</a>
            <a href="${pageContext.request.contextPath}/payments?sortBy=amount" class="btn btn-sm ${'amount' == sortBy ? 'btn-gold' : 'btn-outline-gold'}">Amount</a>
        </div>
    </c:if>

    <!-- Overdue Notifications -->
    <c:if test="${not empty overdueBookings}">
        <div style="background:rgba(220,53,69,0.08);border:1px solid rgba(220,53,69,0.35);border-radius:10px;padding:16px 20px;margin-bottom:20px">
            <div class="d-flex align-items-center gap-2 mb-3">
                <i class="fas fa-exclamation-triangle" style="color:#dc3545"></i>
                <strong style="color:#dc3545">Late Fee Notifications</strong>
                <span style="background:rgba(220,53,69,0.2);color:#dc3545;border-radius:20px;padding:1px 10px;font-size:0.8rem">${overdueBookings.size()} overdue</span>
            </div>
            <c:forEach var="ob" items="${overdueBookings}">
                <div class="d-flex justify-content-between align-items-center" style="padding:8px 0;border-top:1px solid rgba(220,53,69,0.2)">
                    <div>
                        <code style="color:var(--gold)">${ob.bookingId}</code>
                        <span style="color:var(--gray);margin:0 8px">—</span>
                        <span>${ob.vehicleInfo}</span>
                        <span style="color:var(--gray);font-size:0.85rem;margin-left:8px">(due ${ob.endDate})</span>
                    </div>
                    <a href="${pageContext.request.contextPath}/payments/pay/${ob.bookingId}"
                       style="background:rgba(220,53,69,0.15);color:#dc3545;border:1px solid rgba(220,53,69,0.4);border-radius:6px;padding:3px 12px;font-size:0.85rem;text-decoration:none">
                        Pay Now
                    </a>
                </div>
            </c:forEach>
        </div>
    </c:if>

    <div class="table-dark-custom">
        <table class="table table-hover mb-0">
            <thead>
                <tr>
                    <th>Payment ID</th><th>Booking ID</th><th>Customer</th><th>Amount</th>
                    <th>Late Fee</th><th>Total</th><th>Method</th><th>Status</th><th>Date</th>
                    <c:if test="${user.userType == 'admin'}"><th>Action</th></c:if>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="payment" items="${payments}">
                    <tr>
                        <td><code style="color:var(--gold)">${payment.paymentId}</code></td>
                        <td><code style="color:var(--gray)">${payment.bookingId}</code></td>
                        <td>${payment.userName}</td>
                        <td>${payment.amount}</td>
                        <td>${payment.lateFee}</td>
                        <td style="font-weight:600">${payment.totalAmount}</td>
                        <td>
                            <c:if test="${payment.paymentMethod == 'online'}">🌐 Online</c:if>
                            <c:if test="${payment.paymentMethod == 'cash'}">💵 Cash</c:if>
                        </td>
                        <td><span class="status-badge status-${payment.status}">${payment.status}</span></td>
                        <td>${payment.paymentDate}</td>
                        <c:if test="${user.userType == 'admin'}">
                            <td>
                                <a href="${pageContext.request.contextPath}/payments/delete/${payment.paymentId}"
                                   class="btn btn-danger-dark btn-sm"
                                   onclick="return confirm('Void this payment?')">Void</a>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>
                <c:if test="${empty payments}">
                    <tr>
                        <td colspan="10" class="text-center" style="color:var(--gray);padding:32px">No payment records found.</td>
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
