<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Users - Elite Wheel Rentals</title>
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
        <h1>User <span>Management</span></h1>
        <p style="color:var(--gray);margin-top:4px">All registered users sorted by name (Selection Sort)</p>
    </div>

    <div class="table-dark-custom">
        <table class="table table-hover mb-0">
            <thead>
                <tr>
                    <th>User ID</th><th>Name</th><th>NIC</th><th>Contact</th><th>Email</th><th>Type</th><th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${users}">
                    <tr>
                        <td><code style="color:var(--gold)">${u.userId}</code></td>
                        <td>${u.name}</td>
                        <td>${u.nic}</td>
                        <td>${u.contact}</td>
                        <td>${u.email}</td>
                        <td>
                            <c:if test="${u.userType == 'admin'}">
                                <span class="status-badge status-confirmed">Admin</span>
                            </c:if>
                            <c:if test="${u.userType == 'customer'}">
                                <span class="status-badge status-completed">Customer</span>
                            </c:if>
                        </td>
                        <td>
                            <c:if test="${u.userType != 'admin'}">
                                <a href="${pageContext.request.contextPath}/users/delete/${u.userId}"
                                   class="btn btn-danger-dark btn-sm"
                                   onclick="return confirm('Delete this user?')">Delete</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<footer><div class="container"><p>© 2025 <span>Elite Wheel Rentals</span></p></div></footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<%@ include file="/WEB-INF/jsp/chatbot.jsp" %>
</body>
</html>
