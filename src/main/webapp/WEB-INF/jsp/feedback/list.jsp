<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Reviews - Elite Wheel Rentals</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container">
        <a class="navbar-brand" href="/" style="display:flex;align-items:center;gap:8px;padding:0">
            <img src="${pageContext.request.contextPath}/logo.png" alt="Elite Wheel Rentals"
                 style="height:54px;width:auto;object-fit:contain;filter:drop-shadow(0 2px 10px rgba(212,168,67,0.4))">
        </a>
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/" class="nav-link" style="color:var(--gray)">Home</a>
            <c:if test="${not empty user}">
                <a href="${pageContext.request.contextPath}/dashboard" class="nav-link" style="color:var(--gray)">Dashboard</a>
                <a href="${pageContext.request.contextPath}/feedback/add" class="btn btn-gold btn-sm">+ Write Review</a>
            </c:if>
            <c:if test="${empty user}">
                <a href="${pageContext.request.contextPath}/login" class="btn btn-gold btn-sm">Login to Review</a>
            </c:if>
        </div>
    </div>
</nav>

<div class="container py-4">
    <div class="page-header">
        <div class="d-flex justify-content-between align-items-center">
            <h1>Customer <span>Reviews</span></h1>
            <div class="text-center">
                <div style="font-family:'Rajdhani',sans-serif;font-size:2rem;font-weight:700;color:var(--gold)">
                    <fmt:formatNumber value="${avgRating}" pattern="#.0"/>
                </div>
                <div class="stars">★★★★★</div>
                <div style="color:var(--gray);font-size:0.8rem">Average Rating</div>
            </div>
        </div>
    </div>

    <!-- Admin table view -->
    <c:if test="${not empty user and user.userType == 'admin'}">
        <div class="table-dark-custom">
            <table class="table table-hover mb-0">
                <thead>
                    <tr><th>ID</th><th>User</th><th>Vehicle</th><th>Rating</th><th>Comment</th><th>Status</th><th>Actions</th></tr>
                </thead>
                <tbody>
                    <c:forEach var="f" items="${feedbacks}">
                        <tr>
                            <td><code style="color:var(--gold)">${f.feedbackId}</code></td>
                            <td>${f.userName}</td>
                            <td>${f.vehicleInfo}</td>
                            <td><span class="stars">${f.stars}</span></td>
                            <td>${f.comment}</td>
                            <td>
                                <c:if test="${f.status == 'active'}"><span class="status-badge status-completed">Active</span></c:if>
                                <c:if test="${f.status == 'hidden'}"><span class="status-badge status-cancelled">Hidden</span></c:if>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/feedback/toggle/${f.feedbackId}" class="btn btn-outline-gold btn-sm">Toggle</a>
                                <a href="${pageContext.request.contextPath}/feedback/delete/${f.feedbackId}" class="btn btn-danger-dark btn-sm" onclick="return confirm('Delete?')">Del</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>

    <!-- Customer card view -->
    <c:if test="${empty user or user.userType != 'admin'}">
        <div class="row g-4">
            <c:forEach var="f" items="${feedbacks}">
                <div class="col-md-4">
                    <div class="card-dark p-4 h-100">
                        <div class="stars mb-2">${f.stars}</div>
                        <p style="color:var(--gray);font-size:0.95rem">${f.comment}</p>
                        <hr class="divider">
                        <div style="font-weight:600;font-size:0.9rem">${f.userName}</div>
                        <div style="color:var(--gray);font-size:0.8rem">${f.vehicleInfo}</div>
                        <div style="color:var(--gray);font-size:0.75rem;margin-top:4px">${f.createdAt}</div>
                        <c:if test="${not empty user and user.userId == f.userId}">
                            <div class="mt-2">
                                <a href="${pageContext.request.contextPath}/feedback/edit/${f.feedbackId}"
                                   class="btn btn-outline-gold btn-sm" style="font-size:0.78rem">
                                    <i class="fas fa-edit me-1"></i>Edit
                                </a>
                            </div>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty feedbacks}">
                <div class="col-12 text-center py-5">
                    <div style="font-size:3rem">⭐</div>
                    <p style="color:var(--gray)">No reviews yet. Be the first to review!</p>
                    <c:if test="${not empty user}">
                        <a href="${pageContext.request.contextPath}/feedback/add" class="btn btn-gold mt-2">Write Review</a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </c:if>
</div>

<footer><div class="container"><p>© 2025 <span>Elite Wheel Rentals</span></p></div></footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<%@ include file="/WEB-INF/jsp/chatbot.jsp" %>
</body>
</html>
