<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Management - Elite Wheel Rentals</title>
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
            <a href="${pageContext.request.contextPath}/admin/register" class="btn btn-gold btn-sm">
                <i class="fas fa-plus me-1"></i>Add Admin
            </a>
        </div>
    </div>
</nav>

<div class="container" style="margin-top:40px;padding-bottom:60px">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 style="font-family:'Rajdhani',sans-serif;font-weight:700">Admin <span class="text-gold">Management</span></h2>
        <a href="${pageContext.request.contextPath}/admin/log" class="btn btn-outline-gold btn-sm">
            <i class="fas fa-history me-1"></i>Activity Log
        </a>
    </div>

    <div class="card-dark">
        <div class="table-responsive">
            <table class="table table-dark table-hover" style="margin:0">
                <thead>
                    <tr style="color:var(--gold);border-bottom:1px solid var(--border)">
                        <th>ID</th><th>Name</th><th>Email</th><th>Role</th><th>Permissions</th><th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="admin" items="${admins}">
                        <tr>
                            <td style="color:var(--gray);font-size:0.85rem">${admin.userId}</td>
                            <td>${admin.name}</td>
                            <td style="color:var(--gray)">${admin.email}</td>
                            <td>
                                <span style="background:rgba(212,168,67,0.15);color:var(--gold);padding:2px 10px;border-radius:20px;font-size:0.8rem">
                                    ${admin.role}
                                </span>
                            </td>
                            <td style="color:var(--gray);font-size:0.85rem">${admin.permissions}</td>
                            <td>
                                <c:if test="${admin.userId != user.userId}">
                                    <a href="${pageContext.request.contextPath}/admin/delete/${admin.userId}"
                                       class="btn btn-sm"
                                       style="background:rgba(220,53,69,0.15);color:#dc3545;border:1px solid rgba(220,53,69,0.3)"
                                       onclick="return confirm('Remove this admin account?')">
                                        <i class="fas fa-trash"></i>
                                    </a>
                                </c:if>
                                <c:if test="${admin.userId == user.userId}">
                                    <span style="color:var(--gray);font-size:0.8rem">You</span>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty admins}">
                        <tr>
                            <td colspan="6" style="text-align:center;color:var(--gray);padding:2rem">No admin accounts found.</td>
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
