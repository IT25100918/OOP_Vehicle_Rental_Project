<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Review - Elite Wheel Rentals</title>
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
        <a href="${pageContext.request.contextPath}/feedback" class="btn btn-outline-gold btn-sm">← Reviews</a>
    </div>
</nav>

<div class="container" style="max-width:560px;margin-top:48px;padding-bottom:60px">
    <div class="form-dark">
        <div class="mb-4">
            <h2 style="font-family:'Rajdhani',sans-serif;font-weight:700">Edit <span class="text-gold">Your Review</span></h2>
            <p style="color:var(--gray);margin:0">Update your feedback for <span style="color:var(--gold)">${feedback.vehicleInfo}</span></p>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/feedback/edit/${feedback.feedbackId}">
            <div class="row g-3">
                <div class="col-12">
                    <label class="form-label">Rating</label>
                    <select class="form-select" name="rating">
                        <option value="5" ${feedback.rating == 5 ? 'selected' : ''}>★★★★★ — Excellent (5)</option>
                        <option value="4" ${feedback.rating == 4 ? 'selected' : ''}>★★★★☆ — Good (4)</option>
                        <option value="3" ${feedback.rating == 3 ? 'selected' : ''}>★★★☆☆ — Average (3)</option>
                        <option value="2" ${feedback.rating == 2 ? 'selected' : ''}>★★☆☆☆ — Poor (2)</option>
                        <option value="1" ${feedback.rating == 1 ? 'selected' : ''}>★☆☆☆☆ — Terrible (1)</option>
                    </select>
                </div>
                <div class="col-12">
                    <label class="form-label">Review Type</label>
                    <select class="form-select" name="type">
                        <option value="vehicle" ${feedback.type == 'vehicle' ? 'selected' : ''}>Vehicle Review</option>
                        <option value="service" ${feedback.type == 'service' ? 'selected' : ''}>Service Review</option>
                    </select>
                </div>
                <div class="col-12">
                    <label class="form-label">Comment</label>
                    <textarea class="form-control" name="comment" rows="4" required>${feedback.comment}</textarea>
                </div>
                <div class="col-12 mt-2">
                    <button type="submit" class="btn btn-gold w-100">
                        <i class="fas fa-save me-2"></i>Save Changes
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
