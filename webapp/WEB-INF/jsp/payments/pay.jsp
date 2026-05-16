<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Process Payment - Elite Wheel Rentals</title>
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
        <a href="${pageContext.request.contextPath}/bookings" class="btn btn-outline-gold btn-sm">← Back</a>
    </div>
</nav>

<div class="container" style="max-width:520px;margin-top:60px;padding-bottom:60px">
    <div class="form-dark">
        <div class="mb-4">
            <h2 style="font-family:'Rajdhani',sans-serif;font-weight:700">Process <span class="text-gold">Payment</span></h2>
        </div>

        <div class="p-3 mb-4" style="background:var(--dark-4);border-radius:8px;border:1px solid var(--border)">
            <div class="d-flex justify-content-between mb-2">
                <span style="color:var(--gray)">Booking ID:</span>
                <code style="color:var(--gold)">${booking.bookingId}</code>
            </div>
            <div class="d-flex justify-content-between mb-2">
                <span style="color:var(--gray)">Vehicle:</span>
                <span>${booking.vehicleInfo}</span>
            </div>
            <div class="d-flex justify-content-between mb-2">
                <span style="color:var(--gray)">Duration:</span>
                <span>${booking.totalDays} day(s)</span>
            </div>
            <hr class="divider">
            <div class="d-flex justify-content-between">
                <span style="color:var(--gold);font-weight:600">Booking Amount:</span>
                <span style="color:var(--gold);font-weight:700">Rs. ${booking.totalCost}</span>
            </div>
        </div>

        <form method="post" action="${pageContext.request.contextPath}/payments/pay">
            <input type="hidden" name="bookingId" value="${booking.bookingId}">
            <div class="row g-3">
                <c:if test="${isOverdue}">
                    <div class="col-12">
                        <div class="p-3" style="background:rgba(220,53,69,0.1);border:1px solid rgba(220,53,69,0.35);border-radius:8px">
                            <div class="d-flex align-items-start gap-2">
                                <i class="fas fa-exclamation-triangle" style="color:#dc3545;margin-top:2px"></i>
                                <div>
                                    <div style="color:#dc3545;font-weight:600;font-size:0.95rem">Overdue Return Detected</div>
                                    <div style="color:var(--gray);font-size:0.85rem;margin-top:4px">
                                        This vehicle was due back on <strong>${booking.endDate}</strong>.
                                        A late fee of <strong style="color:#dc3545">Rs. ${suggestedLateFee}</strong> has been calculated (20% of daily rate per overdue day).
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
                <div class="col-12">
                    <label class="form-label">Payment Method</label>
                    <select class="form-select" name="paymentMethod">
                        <option value="cash">💵 Cash</option>
                        <option value="online">🌐 Online Transfer</option>
                    </select>
                </div>
                <div class="col-12">
                    <label class="form-label">Late Return Fee (Rs.) <span style="color:var(--gray)">(auto-calculated if overdue)</span></label>
                    <input type="number" class="form-control" name="lateFee" value="${suggestedLateFee}" min="0" step="100">
                </div>
                <div class="col-12 mt-2">
                    <button type="submit" class="btn btn-gold w-100">
                        <i class="fas fa-credit-card me-2"></i>Process Payment
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
