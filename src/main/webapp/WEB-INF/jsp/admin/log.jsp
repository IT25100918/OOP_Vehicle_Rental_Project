<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Activity Log - Elite Wheel Rentals</title>
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
            <a href="${pageContext.request.contextPath}/admin/manage" class="nav-link" style="color:var(--gray)">Admin Management</a>
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-gold btn-sm">Dashboard</a>
        </div>
    </div>
</nav>

<div class="container" style="margin-top:40px;padding-bottom:60px">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 style="font-family:'Rajdhani',sans-serif;font-weight:700">Activity <span class="text-gold">Log</span></h2>
        <span style="color:var(--gray);font-size:0.9rem">
            <i class="fas fa-history me-1"></i>${fn:length(logs)} entries
        </span>
    </div>

    <div class="card-dark">
        <c:if test="${empty logs}">
            <div style="text-align:center;padding:2rem;color:var(--gray)">
                <i class="fas fa-clipboard-list" style="font-size:2rem;margin-bottom:12px;display:block"></i>
                No activity recorded yet.
            </div>
        </c:if>
        <c:if test="${not empty logs}">
            <div class="table-responsive">
                <table class="table table-dark table-hover" style="margin:0;font-size:0.9rem">
                    <thead>
                        <tr style="color:var(--gold);border-bottom:1px solid var(--border)">
                            <th style="width:200px">Timestamp</th>
                            <th style="width:130px">Admin ID</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%-- logs are "timestamp,adminId,action" CSV lines, newest first --%>
                        <c:forEach var="log" items="${logs}" varStatus="s">
                            <c:set var="parts" value="${fn:split(log, ',')}"/>
                            <tr>
                                <td style="color:var(--gray);font-family:monospace;font-size:0.82rem">
                                    ${fn:length(parts) > 0 ? parts[0] : ''}
                                </td>
                                <td>
                                    <span style="background:rgba(212,168,67,0.12);color:var(--gold);padding:1px 8px;border-radius:20px;font-size:0.78rem">
                                        ${fn:length(parts) > 1 ? parts[1] : ''}
                                    </span>
                                </td>
                                <td>${fn:length(parts) > 2 ? parts[2] : ''}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<%@ include file="/WEB-INF/jsp/chatbot.jsp" %>
</body>
</html>
