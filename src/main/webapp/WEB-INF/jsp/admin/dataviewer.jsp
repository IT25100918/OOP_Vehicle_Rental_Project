<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Data Viewer - Elite Wheel Rentals</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .dv-tabs { display:flex; gap:6px; flex-wrap:wrap; margin-bottom:0; padding:20px 24px 0; border-bottom:1px solid var(--border); background:rgba(255,255,255,0.02); }
        .dv-tab {
            padding:8px 18px; border-radius:10px 10px 0 0; font-size:13px; font-weight:600;
            color:var(--gray); background:transparent; border:1px solid transparent;
            border-bottom:none; cursor:pointer; text-decoration:none; transition:all 0.15s;
            display:flex; align-items:center; gap:6px;
        }
        .dv-tab:hover { color:#fff; background:rgba(255,255,255,0.05); }
        .dv-tab.active { color:#D4A843; background:rgba(212,168,67,0.08); border-color:rgba(212,168,67,0.3); }
        .dv-badge { background:rgba(212,168,67,0.15); color:#D4A843; border-radius:999px; padding:1px 7px; font-size:11px; }
        .dv-tab.active .dv-badge { background:rgba(212,168,67,0.25); }

        .dv-table-wrap { overflow-x:auto; }
        .dv-table { width:100%; border-collapse:collapse; font-size:13px; }
        .dv-table thead tr { background:rgba(212,168,67,0.07); border-bottom:1px solid rgba(212,168,67,0.2); }
        .dv-table th { padding:10px 14px; color:#D4A843; font-weight:700; font-family:'Rajdhani',sans-serif; font-size:12px; text-transform:uppercase; letter-spacing:0.5px; white-space:nowrap; }
        .dv-table td { padding:10px 14px; border-bottom:1px solid rgba(255,255,255,0.04); color:#d1d5db; vertical-align:middle; }
        .dv-table tbody tr:hover { background:rgba(255,255,255,0.03); }
        .dv-table td code { color:#D4A843; font-size:12px; background:rgba(212,168,67,0.08); padding:2px 6px; border-radius:4px; }

        .dv-status { display:inline-block; padding:2px 10px; border-radius:999px; font-size:11px; font-weight:700; text-transform:uppercase; }
        .dv-status.available  { background:rgba(74,222,128,0.15);  color:#4ade80; }
        .dv-status.rented     { background:rgba(248,113,113,0.15); color:#f87171; }
        .dv-status.confirmed  { background:rgba(96,165,250,0.15);  color:#60a5fa; }
        .dv-status.completed  { background:rgba(74,222,128,0.15);  color:#4ade80; }
        .dv-status.cancelled  { background:rgba(248,113,113,0.15); color:#f87171; }
        .dv-status.pending    { background:rgba(251,191,36,0.15);  color:#fbbf24; }
        .dv-status.paid       { background:rgba(74,222,128,0.15);  color:#4ade80; }
        .dv-status.active     { background:rgba(74,222,128,0.15);  color:#4ade80; }
        .dv-status.hidden     { background:rgba(156,163,175,0.15); color:#9ca3af; }
        .dv-status.admin      { background:rgba(167,139,250,0.15); color:#a78bfa; }
        .dv-status.customer   { background:rgba(96,165,250,0.15);  color:#60a5fa; }

        .dv-toolbar { display:flex; align-items:center; gap:12px; padding:14px 24px; border-bottom:1px solid var(--border); flex-wrap:wrap; }
        .dv-search { background:rgba(255,255,255,0.05); border:1px solid rgba(255,255,255,0.1); border-radius:8px; padding:7px 14px; color:#fff; font-size:13px; width:260px; outline:none; }
        .dv-search:focus { border-color:rgba(212,168,67,0.5); }
        .dv-search::placeholder { color:#555; }
        .dv-count { margin-left:auto; font-size:12px; color:var(--gray); }

        .log-line { font-family:'Courier New',monospace; font-size:12px; padding:6px 14px; border-bottom:1px solid rgba(255,255,255,0.03); color:#9ca3af; }
        .log-line:hover { background:rgba(255,255,255,0.03); }
        .log-ts   { color:#555; margin-right:10px; }
        .log-id   { color:#D4A843; margin-right:10px; }
        .log-action { color:#d1d5db; }

        .empty-state { text-align:center; padding:60px 20px; color:var(--gray); }
        .empty-state .icon { font-size:3rem; margin-bottom:12px; }

        .stars { color:#D4A843; letter-spacing:1px; }
    </style>
</head>
<body>

<!-- Navbar -->
<nav class="navbar navbar-dark" style="position:sticky;top:0;z-index:999">
    <div class="container-fluid px-4">
        <a class="navbar-brand" href="/" style="display:flex;align-items:center;gap:8px;padding:0">
            <img src="${pageContext.request.contextPath}/logo.png" alt="Elite Wheel Rentals"
                 style="height:54px;width:auto;object-fit:contain;filter:drop-shadow(0 2px 10px rgba(212,168,67,0.4))">
        </a>
        <div class="d-flex align-items-center gap-3">
            <span style="color:var(--gray);font-size:0.9rem"><i class="fas fa-database me-1" style="color:var(--gold)"></i>Data Viewer</span>
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-gold btn-sm"><i class="fas fa-arrow-left me-1"></i>Dashboard</a>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-gold btn-sm"><i class="fas fa-sign-out-alt me-1"></i>Logout</a>
        </div>
    </div>
</nav>

<div class="d-flex">
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="px-4 mb-3"><div style="font-size:0.75rem;text-transform:uppercase;letter-spacing:1px;color:var(--gray);font-weight:600">Main Menu</div></div>
        <ul class="nav flex-column">
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/dashboard"><i class="fas fa-tachometer-alt me-2"></i>Dashboard</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/vehicles"><i class="fas fa-car me-2"></i>Vehicles</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/bookings"><i class="fas fa-calendar-check me-2"></i>Bookings</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/payments"><i class="fas fa-credit-card me-2"></i>Payments</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/users"><i class="fas fa-users me-2"></i>Users</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/feedback"><i class="fas fa-star me-2"></i>Reviews</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/manage"><i class="fas fa-user-shield me-2"></i>Admin Accounts</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/log"><i class="fas fa-history me-2"></i>Activity Log</a></li>
            <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/admin/data"><i class="fas fa-database me-2"></i>Data Viewer</a></li>
        </ul>
    </div>

    <!-- Main content -->
    <div class="main-content" style="padding:0;overflow:hidden;">

        <div style="padding:24px 24px 0">
            <h1 style="font-family:'Rajdhani',sans-serif;font-weight:700;margin-bottom:4px">
                <i class="fas fa-database me-2" style="color:#D4A843;font-size:1.4rem"></i>Live <span style="color:#D4A843">Data Viewer</span>
            </h1>
            <p style="color:var(--gray);font-size:0.9rem;margin-bottom:0">Real-time view of all stored data — read from flat files</p>
        </div>

        <!-- Tabs -->
        <div class="dv-tabs">
            <a class="dv-tab ${activeTab == 'vehicles'  ? 'active' : ''}" href="?tab=vehicles">
                <i class="fas fa-car"></i> Vehicles <span class="dv-badge">${vehicles.size()}</span>
            </a>
            <a class="dv-tab ${activeTab == 'bookings'  ? 'active' : ''}" href="?tab=bookings">
                <i class="fas fa-calendar-check"></i> Bookings <span class="dv-badge">${bookings.size()}</span>
            </a>
            <a class="dv-tab ${activeTab == 'payments'  ? 'active' : ''}" href="?tab=payments">
                <i class="fas fa-credit-card"></i> Payments <span class="dv-badge">${payments.size()}</span>
            </a>
            <a class="dv-tab ${activeTab == 'users'     ? 'active' : ''}" href="?tab=users">
                <i class="fas fa-users"></i> Users <span class="dv-badge">${users.size()}</span>
            </a>
            <a class="dv-tab ${activeTab == 'admins'    ? 'active' : ''}" href="?tab=admins">
                <i class="fas fa-user-shield"></i> Admins <span class="dv-badge">${admins.size()}</span>
            </a>
            <a class="dv-tab ${activeTab == 'reviews'   ? 'active' : ''}" href="?tab=reviews">
                <i class="fas fa-star"></i> Reviews <span class="dv-badge">${feedbacks.size()}</span>
            </a>
            <a class="dv-tab ${activeTab == 'log'       ? 'active' : ''}" href="?tab=log">
                <i class="fas fa-history"></i> Activity Log <span class="dv-badge">${logs.size()}</span>
            </a>
        </div>

        <!-- Toolbar -->
        <div class="dv-toolbar">
            <i class="fas fa-search" style="color:#555"></i>
            <input class="dv-search" id="dvSearch" type="text" placeholder="Search in table..." oninput="filterTable()">
            <button class="btn btn-sm btn-outline-gold" onclick="location.reload()" title="Refresh">
                <i class="fas fa-sync-alt"></i> Refresh
            </button>
            <span class="dv-count" id="dvCount"></span>
        </div>

        <!-- ── VEHICLES ─────────────────────────────────────────── -->
        <c:if test="${activeTab == 'vehicles'}">
        <div class="dv-table-wrap">
            <table class="dv-table" id="dvTable">
                <thead><tr>
                    <th>ID</th><th>Type</th><th>Brand</th><th>Model</th>
                    <th>Plate</th><th>Price/day</th><th>Status</th><th>Location</th>
                </tr></thead>
                <tbody>
                <c:forEach var="v" items="${vehicles}">
                    <tr>
                        <td><code>${v.vehicleId}</code></td>
                        <td>${v.type}</td>
                        <td>${v.brand}</td>
                        <td>${v.model}</td>
                        <td><code>${v.plateNumber}</code></td>
                        <td>Rs. <fmt:formatNumber value="${v.rentPrice}" pattern="#,##0"/></td>
                        <td><span class="dv-status ${v.availability}">${v.availability}</span></td>
                        <td>
                            <c:choose>
                                <c:when test="${v.hasLocation()}">
                                    <span style="color:#60a5fa;font-size:12px">
                                        <i class="fas fa-map-marker-alt me-1"></i>${v.lat}, ${v.lng}
                                    </span>
                                </c:when>
                                <c:otherwise><span style="color:#444">—</span></c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty vehicles}"><tr><td colspan="8"><div class="empty-state"><div class="icon">🚗</div>No vehicles</div></td></tr></c:if>
                </tbody>
            </table>
        </div>
        </c:if>

        <!-- ── BOOKINGS ─────────────────────────────────────────── -->
        <c:if test="${activeTab == 'bookings'}">
        <div class="dv-table-wrap">
            <table class="dv-table" id="dvTable">
                <thead><tr>
                    <th>ID</th><th>User</th><th>Vehicle</th><th>Start</th><th>End</th>
                    <th>Days</th><th>Total Cost</th><th>Type</th><th>Status</th><th>Created</th>
                </tr></thead>
                <tbody>
                <c:forEach var="b" items="${bookings}">
                    <tr>
                        <td><code>${b.bookingId}</code></td>
                        <td>${b.userName}<br><small style="color:#555">${b.userId}</small></td>
                        <td>${b.vehicleInfo}<br><small style="color:#555">${b.vehicleId}</small></td>
                        <td>${b.startDate}</td>
                        <td>${b.endDate}</td>
                        <td>${b.totalDays}</td>
                        <td>Rs. <fmt:formatNumber value="${b.totalCost}" pattern="#,##0"/></td>
                        <td>${b.bookingType}</td>
                        <td><span class="dv-status ${b.status}">${b.status}</span></td>
                        <td>${b.createdAt}</td>
                    </tr>
                </c:forEach>
                <c:if test="${empty bookings}"><tr><td colspan="10"><div class="empty-state"><div class="icon">📋</div>No bookings</div></td></tr></c:if>
                </tbody>
            </table>
        </div>
        </c:if>

        <!-- ── PAYMENTS ─────────────────────────────────────────── -->
        <c:if test="${activeTab == 'payments'}">
        <div class="dv-table-wrap">
            <table class="dv-table" id="dvTable">
                <thead><tr>
                    <th>ID</th><th>Booking ID</th><th>User</th><th>Amount</th>
                    <th>Late Fee</th><th>Total</th><th>Method</th><th>Status</th><th>Date</th>
                </tr></thead>
                <tbody>
                <c:forEach var="p" items="${payments}">
                    <tr>
                        <td><code>${p.paymentId}</code></td>
                        <td><code>${p.bookingId}</code></td>
                        <td>${p.userName}<br><small style="color:#555">${p.userId}</small></td>
                        <td>Rs. <fmt:formatNumber value="${p.amount}" pattern="#,##0"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${p.lateFee > 0}"><span style="color:#f87171">Rs. <fmt:formatNumber value="${p.lateFee}" pattern="#,##0"/></span></c:when>
                                <c:otherwise><span style="color:#444">—</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td style="font-weight:700;color:#D4A843">Rs. <fmt:formatNumber value="${p.totalAmount}" pattern="#,##0"/></td>
                        <td>${p.paymentMethod}</td>
                        <td><span class="dv-status ${p.status}">${p.status}</span></td>
                        <td>${p.paymentDate}</td>
                    </tr>
                </c:forEach>
                <c:if test="${empty payments}"><tr><td colspan="9"><div class="empty-state"><div class="icon">💳</div>No payments</div></td></tr></c:if>
                </tbody>
            </table>
        </div>
        </c:if>

        <!-- ── USERS ────────────────────────────────────────────── -->
        <c:if test="${activeTab == 'users'}">
        <div class="dv-table-wrap">
            <table class="dv-table" id="dvTable">
                <thead><tr>
                    <th>ID</th><th>Name</th><th>NIC</th><th>Contact</th><th>Email</th><th>Role</th>
                </tr></thead>
                <tbody>
                <c:forEach var="u" items="${users}">
                    <tr>
                        <td><code>${u.userId}</code></td>
                        <td>${u.name}</td>
                        <td>${u.nic}</td>
                        <td>${u.contact}</td>
                        <td>${u.email}</td>
                        <td><span class="dv-status ${u.userType}">${u.userType}</span></td>
                    </tr>
                </c:forEach>
                <c:if test="${empty users}"><tr><td colspan="6"><div class="empty-state"><div class="icon">👥</div>No users</div></td></tr></c:if>
                </tbody>
            </table>
        </div>
        </c:if>

        <!-- ── ADMINS ────────────────────────────────────────────── -->
        <c:if test="${activeTab == 'admins'}">
        <div class="dv-table-wrap">
            <table class="dv-table" id="dvTable">
                <thead><tr>
                    <th>ID</th><th>Name</th><th>NIC</th><th>Contact</th><th>Email</th><th>Role</th><th>Permissions</th>
                </tr></thead>
                <tbody>
                <c:forEach var="a" items="${admins}">
                    <tr>
                        <td><code>${a.userId}</code></td>
                        <td>${a.name}</td>
                        <td>${a.nic}</td>
                        <td>${a.contact}</td>
                        <td>${a.email}</td>
                        <td><span class="dv-status admin">${a.role}</span></td>
                        <td><span style="color:#888;font-size:12px">${a.permissions}</span></td>
                    </tr>
                </c:forEach>
                <c:if test="${empty admins}"><tr><td colspan="7"><div class="empty-state"><div class="icon">🛡️</div>No admins</div></td></tr></c:if>
                </tbody>
            </table>
        </div>
        </c:if>

        <!-- ── REVIEWS ───────────────────────────────────────────── -->
        <c:if test="${activeTab == 'reviews'}">
        <div class="dv-table-wrap">
            <table class="dv-table" id="dvTable">
                <thead><tr>
                    <th>ID</th><th>User</th><th>Vehicle</th><th>Rating</th><th>Comment</th><th>Type</th><th>Status</th><th>Date</th>
                </tr></thead>
                <tbody>
                <c:forEach var="f" items="${feedbacks}">
                    <tr>
                        <td><code>${f.feedbackId}</code></td>
                        <td>${f.userName}<br><small style="color:#555">${f.userId}</small></td>
                        <td>${f.vehicleInfo}<br><small style="color:#555">${f.vehicleId}</small></td>
                        <td>
                            <span class="stars">
                                <c:forEach begin="1" end="${f.rating}">★</c:forEach>
                            </span>
                            <span style="color:#888;font-size:12px"> ${f.rating}/5</span>
                        </td>
                        <td style="max-width:220px;white-space:normal">${f.comment}</td>
                        <td>${f.type}</td>
                        <td><span class="dv-status ${f.status}">${f.status}</span></td>
                        <td>${f.createdAt}</td>
                    </tr>
                </c:forEach>
                <c:if test="${empty feedbacks}"><tr><td colspan="8"><div class="empty-state"><div class="icon">⭐</div>No reviews</div></td></tr></c:if>
                </tbody>
            </table>
        </div>
        </c:if>

        <!-- ── ACTIVITY LOG ──────────────────────────────────────── -->
        <c:if test="${activeTab == 'log'}">
        <div id="dvTable">
            <c:forEach var="line" items="${logs}" varStatus="s">
                <c:set var="parts" value="${fn:split(line, ',')}" />
                <div class="log-line">
                    <span class="log-ts">${line.substring(0, line.indexOf(',') >= 0 ? line.indexOf(',') : line.length())}</span>
                    <span>${line}</span>
                </div>
            </c:forEach>
            <c:if test="${empty logs}">
                <div class="empty-state"><div class="icon">📜</div>No activity logged yet</div>
            </c:if>
        </div>
        </c:if>

    </div><!-- /main-content -->
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
// Live search filter
function filterTable() {
    const q = document.getElementById('dvSearch').value.toLowerCase();
    const table = document.getElementById('dvTable');
    if (!table) return;

    // Handle both table rows and log divs
    const rows = table.tagName === 'TABLE'
        ? [...table.querySelectorAll('tbody tr')]
        : [...table.querySelectorAll('.log-line')];

    let visible = 0;
    rows.forEach(row => {
        const match = row.textContent.toLowerCase().includes(q);
        row.style.display = match ? '' : 'none';
        if (match) visible++;
    });
    const countEl = document.getElementById('dvCount');
    if (countEl) countEl.textContent = visible + ' of ' + rows.length + ' rows';
}

// Init count on load
window.addEventListener('DOMContentLoaded', () => {
    filterTable();
    // Pre-fill search if ?q= param present
    const params = new URLSearchParams(window.location.search);
    if (params.get('q')) {
        document.getElementById('dvSearch').value = params.get('q');
        filterTable();
    }
});
</script>
<%@ include file="/WEB-INF/jsp/chatbot.jsp" %>
</body>
</html>
