<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Write Review - Elite Wheel Rentals</title>
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
        <a href="${pageContext.request.contextPath}/feedback" class="btn btn-outline-gold btn-sm">← Back to Reviews</a>
    </div>
</nav>

<div class="container" style="max-width:540px;margin-top:60px;padding-bottom:60px">
    <div class="form-dark">
        <div class="mb-4">
            <h2 style="font-family:'Rajdhani',sans-serif;font-weight:700">Write a <span class="text-gold">Review</span></h2>
            <p style="color:var(--gray);font-size:0.9rem">Share your experience with other renters</p>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/feedback/add">
            <div class="row g-3">
                <div class="col-12">
                    <label class="form-label">Vehicle</label>
                    <select class="form-select" name="vehicleId" required>
                        <option value="">-- Select Vehicle --</option>
                        <c:forEach var="v" items="${vehicles}">
                            <option value="${v.vehicleId}">${v.brand} ${v.model} (${v.plateNumber})</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Rating</label>
                    <select class="form-select" name="rating" required>
                        <option value="5">⭐⭐⭐⭐⭐ Excellent</option>
                        <option value="4">⭐⭐⭐⭐ Good</option>
                        <option value="3">⭐⭐⭐ Average</option>
                        <option value="2">⭐⭐ Poor</option>
                        <option value="1">⭐ Terrible</option>
                    </select>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Review Type</label>
                    <select class="form-select" name="type">
                        <option value="vehicle">Vehicle Review</option>
                        <option value="service">Service Review</option>
                    </select>
                </div>
                <div class="col-12">
                    <label class="form-label">Your Comment</label>
                    <textarea class="form-control" name="comment" rows="4" placeholder="Tell us about your experience..." required></textarea>
                </div>
                <div class="col-12 mt-2">
                    <button type="submit" class="btn btn-gold w-100">
                        <i class="fas fa-star me-2"></i>Submit Review
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
