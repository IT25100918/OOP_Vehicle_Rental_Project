<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Profile - Elite Wheel Rentals</title>
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

<div class="container" style="max-width:540px;margin-top:60px;padding-bottom:60px">
    <div class="form-dark">
        <div class="text-center mb-4">
            <div style="width:72px;height:72px;background:var(--dark-4);border-radius:50%;display:flex;align-items:center;justify-content:center;margin:0 auto;font-size:2rem;border:2px solid var(--gold)">👤</div>
            <h2 style="font-family:'Rajdhani',sans-serif;font-weight:700;margin-top:12px">${user.name}</h2>
            <p style="color:var(--gold);font-size:0.85rem;text-transform:capitalize">${user.userType}</p>
        </div>

        <c:if test="${not empty success}">
            <div class="alert alert-success-dark mb-3">${success}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/users/profile">
            <div class="row g-3">
                <div class="col-12">
                    <label class="form-label">Full Name</label>
                    <input type="text" class="form-control" name="name" value="${user.name}" required>
                </div>
                <div class="col-12">
                    <label class="form-label">Email</label>
                    <input type="email" class="form-control" name="email" value="${user.email}" required>
                </div>
                <div class="col-12">
                    <label class="form-label">Contact</label>
                    <input type="text" class="form-control" name="contact" value="${user.contact}">
                </div>
                <div class="col-12">
                    <label class="form-label">New Password <span style="color:var(--gray)">(leave blank to keep current)</span></label>
                    <input type="password" class="form-control" name="password" placeholder="••••••••">
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
