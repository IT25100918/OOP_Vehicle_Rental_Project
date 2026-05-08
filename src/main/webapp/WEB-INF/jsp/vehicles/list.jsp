<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Vehicles - Elite Wheel Rentals</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>
    <style>
        .text-gold { color: #D4A843; }
        .leaflet-popup-content-wrapper { background: #1a1a2e; border: 1px solid rgba(212,168,67,0.4); border-radius: 12px; color: #fff; }
        .leaflet-popup-tip { background: #1a1a2e; }
        .leaflet-popup-content { margin: 12px 16px; }
        .map-popup-name { font-family: 'Rajdhani', sans-serif; font-weight: 700; font-size: 15px; color: #D4A843; }
        .map-popup-status.available { color: #4ade80; }
        .map-popup-status.rented    { color: #f87171; }
        .map-filter-btn {
            background: rgba(255,255,255,0.06); border: 1px solid rgba(255,255,255,0.15);
            color: #aaa; padding: 4px 14px; border-radius: 999px; font-size: 12px;
            cursor: pointer; transition: all 0.15s; font-weight: 600;
        }
        .map-filter-btn:hover { background: rgba(255,255,255,0.12); color: #fff; }
        .map-filter-btn.active { background: rgba(212,168,67,0.2); border-color: #D4A843; color: #D4A843; }
        .map-filter-btn.rented.active  { background: rgba(248,113,113,0.15); border-color: #f87171; color: #f87171; }
        .map-filter-btn.avail.active   { background: rgba(74,222,128,0.15); border-color: #4ade80; color: #4ade80; }
        @keyframes pulse-pin { 0%,100%{transform:scale(1)} 50%{transform:scale(1.35)} }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container">
        <a class="navbar-brand" href="/" style="display:flex;align-items:center;gap:8px;padding:0">
            <img src="${pageContext.request.contextPath}/logo.png" alt="Elite Wheel Rentals"
                 style="height:54px;width:auto;object-fit:contain;filter:drop-shadow(0 2px 10px rgba(212,168,67,0.4))">
        </a>
        <div class="navbar-nav ms-auto d-flex flex-row gap-2">
            <a href="${pageContext.request.contextPath}/" class="nav-link">Home</a>
            <c:if test="${not empty user}">
                <a href="${pageContext.request.contextPath}/dashboard" class="nav-link">Dashboard</a>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-gold btn-sm">Logout</a>
            </c:if>
            <c:if test="${empty user}">
                <a href="${pageContext.request.contextPath}/login" class="nav-link">Login</a>
            </c:if>
        </div>
    </div>
</nav>

<div class="container py-4">
    <div class="page-header">
        <div class="d-flex justify-content-between align-items-center">
            <h1><span>Our</span> Fleet</h1>
            <c:if test="${not empty user and user.userType == 'admin'}">
                <a href="${pageContext.request.contextPath}/vehicles/add" class="btn btn-gold">
                    <i class="fas fa-plus me-2"></i>Add Vehicle
                </a>
            </c:if>
        </div>
    </div>

    <!-- Filters -->
    <div class="d-flex flex-wrap gap-2 mb-3">
        <a href="${pageContext.request.contextPath}/vehicles" class="btn btn-sm ${empty filterType ? 'btn-gold' : 'btn-outline-gold'}">All</a>
        <a href="${pageContext.request.contextPath}/vehicles?type=car" class="btn btn-sm ${'car' == filterType ? 'btn-gold' : 'btn-outline-gold'}">🚗 Cars</a>
        <a href="${pageContext.request.contextPath}/vehicles?type=suv" class="btn btn-sm ${'suv' == filterType ? 'btn-gold' : 'btn-outline-gold'}">🚙 SUVs</a>
        <a href="${pageContext.request.contextPath}/vehicles?type=van" class="btn btn-sm ${'van' == filterType ? 'btn-gold' : 'btn-outline-gold'}">🚐 Vans</a>
        <a href="${pageContext.request.contextPath}/vehicles?type=bike" class="btn btn-sm ${'bike' == filterType ? 'btn-gold' : 'btn-outline-gold'}">🏍️ Bikes</a>
        <a href="${pageContext.request.contextPath}/vehicles?type=threewheeler" class="btn btn-sm ${'threewheeler' == filterType ? 'btn-gold' : 'btn-outline-gold'}">🛺 Three-Wheelers</a>
        <a href="${pageContext.request.contextPath}/vehicles?type=bus" class="btn btn-sm ${'bus' == filterType ? 'btn-gold' : 'btn-outline-gold'}">🚌 Buses</a>
        <a href="${pageContext.request.contextPath}/vehicles?type=lorry" class="btn btn-sm ${'lorry' == filterType ? 'btn-gold' : 'btn-outline-gold'}">🚛 Lorries</a>
    </div>
    <div class="d-flex flex-wrap gap-2 mb-4">
        <span style="color:var(--gray);align-self:center;font-size:0.9rem">Sort:</span>
        <a href="${pageContext.request.contextPath}/vehicles?sortBy=availability" class="btn btn-sm ${'availability' == sortBy ? 'btn-gold' : 'btn-outline-gold'}">Availability</a>
        <a href="${pageContext.request.contextPath}/vehicles?sortBy=price" class="btn btn-sm ${'price' == sortBy ? 'btn-gold' : 'btn-outline-gold'}">Price</a>
        <a href="${pageContext.request.contextPath}/vehicles?sortBy=type" class="btn btn-sm ${'type' == sortBy ? 'btn-gold' : 'btn-outline-gold'}">Type</a>
    </div>

    <!-- Vehicle Map (Admin only) -->
    <c:if test="${not empty user and user.userType == 'admin'}">
    <div class="mb-5" id="vehicle-map-section">
        <!-- Map Header -->
        <div class="d-flex align-items-center justify-content-between mb-3">
            <h5 style="font-family:'Rajdhani',sans-serif;font-weight:700;margin:0">
                <i class="fas fa-map-marker-alt me-2" style="color:#D4A843"></i>Vehicle <span style="color:#D4A843">Location Tracker</span>
            </h5>
            <button class="btn btn-sm btn-outline-gold" onclick="toggleMap()">
                <i class="fas fa-times" id="map-toggle-icon"></i>
                <span id="map-toggle-label">Hide Map</span>
            </button>
        </div>

        <div id="ew-vehicle-map-wrap" style="border-radius:16px;overflow:hidden;border:1px solid rgba(212,168,67,0.25);box-shadow:0 8px 40px rgba(0,0,0,0.4);">
            <!-- Filter bar -->
            <div style="background:#111827;padding:10px 16px;border-bottom:1px solid rgba(212,168,67,0.15);display:flex;align-items:center;gap:10px;flex-wrap:wrap;">
                <span style="font-size:12px;color:#888;font-weight:600;text-transform:uppercase;letter-spacing:1px">Filter:</span>
                <button class="map-filter-btn active" onclick="filterMap('all')"    id="filter-all">All Vehicles</button>
                <button class="map-filter-btn rented"  onclick="filterMap('rented')" id="filter-rented">🔴 Rented</button>
                <button class="map-filter-btn avail"   onclick="filterMap('available')" id="filter-avail">🟢 Available</button>
                <span id="map-count" style="margin-left:auto;font-size:12px;color:#888"></span>
            </div>
            <!-- Map -->
            <div id="ew-vehicle-map" style="width:100%;height:480px;"></div>
            <!-- Legend -->
            <div style="background:#111827;padding:8px 16px;border-top:1px solid rgba(255,255,255,0.05);display:flex;gap:20px;font-size:12px;color:#aaa;">
                <span><span style="color:#f87171;font-size:16px">●</span> Rented — currently out with a customer</span>
                <span><span style="color:#4ade80;font-size:16px">●</span> Available — at pickup location</span>
            </div>
        </div>
    </div>
    </c:if>

    <!-- Vehicle Grid -->
    <div class="row g-4">
        <c:forEach var="vehicle" items="${vehicles}">
            <div class="col-lg-4 col-md-6">
                <div class="vehicle-card">
                    <img src="${vehicle.imageUrl}" alt="${vehicle.brand} ${vehicle.model}"
                         onerror="this.src='https://via.placeholder.com/400x200?text=Vehicle'">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-start mb-2">
                            <div>
                                <span class="vehicle-type">${vehicle.type}</span>
                                <div class="vehicle-name">${vehicle.brand} ${vehicle.model}</div>
                            </div>
                            <c:if test="${vehicle.available}">
                                <span class="badge-available">Available</span>
                            </c:if>
                            <c:if test="${!vehicle.available}">
                                <span class="badge-rented">Rented</span>
                            </c:if>
                        </div>
                        <div class="d-flex justify-content-between align-items-center">
                            <span class="vehicle-price">Rs. ${vehicle.rentPrice}/day</span>
                            <div class="d-flex gap-2 flex-wrap">
                                <c:if test="${not empty user and user.userType == 'admin' and vehicle.hasLocation()}">
                                    <button class="btn btn-sm btn-outline-gold"
                                            onclick="showOnMap(${vehicle.lat}, ${vehicle.lng}, '${vehicle.brand} ${vehicle.model}', '${vehicle.availability}', '${vehicle.type}')"
                                            title="View on Map">
                                        <i class="fas fa-map-marker-alt"></i>
                                    </button>
                                </c:if>
                                <c:if test="${not empty user and user.userType == 'admin'}">
                                    <a href="${pageContext.request.contextPath}/vehicles/edit/${vehicle.vehicleId}" class="btn btn-sm btn-outline-gold">Edit</a>
                                    <a href="${pageContext.request.contextPath}/vehicles/delete/${vehicle.vehicleId}" class="btn btn-sm btn-danger-dark" onclick="return confirm('Delete this vehicle?')">Del</a>
                                </c:if>
                                <c:if test="${vehicle.available and not empty user and user.userType != 'admin'}">
                                    <a href="${pageContext.request.contextPath}/bookings/create/${vehicle.vehicleId}" class="btn btn-gold btn-sm">Book Now</a>
                                </c:if>
                                <c:if test="${vehicle.available and empty user}">
                                    <a href="${pageContext.request.contextPath}/login" class="btn btn-gold btn-sm">Book Now</a>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
        <c:if test="${empty vehicles}">
            <div class="col-12 text-center py-5">
                <div style="font-size:3rem">🔍</div>
                <p style="color:var(--gray)">No vehicles found.</p>
            </div>
        </c:if>
    </div>
</div>

<footer>
    <div class="container"><p>© 2025 <span>Elite Wheel Rentals</span> Vehicle Rental Platform</p></div>
</footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
<script>
// ── Vehicle data from server ──────────────────────────────────────────────────
const VEHICLES_RAW = [
  <c:forEach var="v" items="${vehicles}" varStatus="s">
  {
    lat:    ${v.lat},
    lng:    ${v.lng},
    name:   '${v.brand} ${v.model}',
    type:   '${v.type}',
    price:  'Rs. ${v.rentPrice}/day',
    status: '${v.availability}',
    plate:  '${v.plateNumber}'
  }<c:if test="${!s.last}">,</c:if>
  </c:forEach>
];
const VEHICLES = VEHICLES_RAW.filter(v => v.lat !== 0 && v.lng !== 0);

const TYPE_ICON = { car:'🚗', suv:'🚙', van:'🚐', bike:'🏍️', threewheeler:'🛺', bus:'🚌', lorry:'🚛' };

let map = null;
let markers = []; // {marker, data}
let currentFilter = 'all';

// ── Custom pin icons ──────────────────────────────────────────────────────────
function makeIcon(status) {
  const isRented = status === 'rented';
  const color  = isRented ? '#f87171' : '#4ade80';
  const shadow = isRented ? 'rgba(248,113,113,0.6)' : 'rgba(74,222,128,0.4)';
  const pulse  = isRented ? 'animation:pulse-pin 1.4s infinite' : '';
  return L.divIcon({
    html: `<div style="width:18px;height:18px;border-radius:50%;background:${color};
                border:2.5px solid #fff;box-shadow:0 0 10px ${shadow};${pulse}"></div>`,
    className: '',
    iconSize: [18, 18],
    iconAnchor: [9, 9],
    popupAnchor: [0, -14]
  });
}

// ── Build popup HTML ──────────────────────────────────────────────────────────
function makePopup(v) {
  const icon   = TYPE_ICON[v.type] || '🚗';
  const badge  = v.status === 'rented'
    ? `<span style="background:rgba(248,113,113,0.2);color:#f87171;border:1px solid #f87171;padding:2px 8px;border-radius:999px;font-size:11px;font-weight:700">● RENTED</span>`
    : `<span style="background:rgba(74,222,128,0.15);color:#4ade80;border:1px solid #4ade80;padding:2px 8px;border-radius:999px;font-size:11px;font-weight:700">● AVAILABLE</span>`;
  return `
    <div style="min-width:180px">
      <div class="map-popup-name">${icon} ${v.name}</div>
      <div style="font-size:11px;color:#888;margin:3px 0 6px">${v.plate}</div>
      ${badge}
      <div style="font-size:13px;color:#D4A843;margin-top:8px;font-weight:700">${v.price}</div>
      <div style="font-size:11px;color:#666;margin-top:4px">${v.lat.toFixed(4)}, ${v.lng.toFixed(4)}</div>
    </div>`;
}

// ── Init map (runs once, map is open by default for admin) ────────────────────
function initMap() {
  if (map) return;

  map = L.map('ew-vehicle-map', { zoomControl: true, scrollWheelZoom: true });
  L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png', {
    attribution: '© OpenStreetMap © CARTO', subdomains: 'abcd', maxZoom: 19
  }).addTo(map);

  if (VEHICLES.length === 0) {
    map.setView([7.8731, 80.7718], 7);
    updateCount(0, 0);
    return;
  }

  const bounds = [];
  VEHICLES.forEach(v => {
    const marker = L.marker([v.lat, v.lng], { icon: makeIcon(v.status) })
      .addTo(map)
      .bindPopup(makePopup(v), { maxWidth: 220 });
    markers.push({ marker, data: v });
    bounds.push([v.lat, v.lng]);
  });
  map.fitBounds(bounds, { padding: [50, 50] });
  updateCount(VEHICLES.length, VEHICLES.filter(v => v.status === 'rented').length);
}

// ── Filter ────────────────────────────────────────────────────────────────────
function filterMap(filter) {
  currentFilter = filter;
  // Update button styles
  ['all','rented','avail'].forEach(f => document.getElementById('filter-' + f).classList.remove('active'));
  document.getElementById('filter-' + (filter === 'available' ? 'avail' : filter)).classList.add('active');

  const bounds = [];
  markers.forEach(({ marker, data }) => {
    const show = filter === 'all' || data.status === filter;
    if (show) {
      if (!map.hasLayer(marker)) marker.addTo(map);
      bounds.push([data.lat, data.lng]);
    } else {
      if (map.hasLayer(marker)) map.removeLayer(marker);
    }
  });
  if (bounds.length) map.fitBounds(bounds, { padding: [50, 50] });

  const shown  = markers.filter(({ data }) => filter === 'all' || data.status === filter).length;
  const rented = VEHICLES.filter(v => v.status === 'rented').length;
  updateCount(shown, rented);
}

function updateCount(shown, rented) {
  const el = document.getElementById('map-count');
  if (el) el.textContent = shown + ' vehicle' + (shown !== 1 ? 's' : '') + ' shown · ' + rented + ' rented';
}

// ── Toggle map visibility ─────────────────────────────────────────────────────
function toggleMap() {
  const wrap  = document.getElementById('ew-vehicle-map-wrap');
  const label = document.getElementById('map-toggle-label');
  const icon  = document.getElementById('map-toggle-icon');
  const open  = wrap.style.display === 'none';
  wrap.style.display = open ? 'block' : 'none';
  label.textContent  = open ? 'Hide Map' : 'Show Map';
  icon.className     = open ? 'fas fa-times' : 'fas fa-map';
  if (open) setTimeout(() => { initMap(); map && map.invalidateSize(); }, 150);
}

// ── Jump to vehicle on map (called from 📍 card button) ───────────────────────
function showOnMap(lat, lng, name, status, type) {
  // Make sure map is visible
  const wrap = document.getElementById('ew-vehicle-map-wrap');
  if (wrap.style.display === 'none') {
    wrap.style.display = 'block';
    document.getElementById('map-toggle-label').textContent = 'Hide Map';
    document.getElementById('map-toggle-icon').className = 'fas fa-times';
  }
  // Reset filter to all so the marker is visible
  if (currentFilter !== 'all') filterMap('all');

  setTimeout(() => {
    initMap();
    map.invalidateSize();
    map.setView([lat, lng], 15, { animate: true });
    markers.forEach(({ marker, data }) => {
      if (data.lat === lat && data.lng === lng) marker.openPopup();
    });
    document.getElementById('vehicle-map-section').scrollIntoView({ behavior: 'smooth', block: 'start' });
  }, 200);
}

// ── Auto-open map for admin on page load ──────────────────────────────────────
window.addEventListener('DOMContentLoaded', () => {
  setTimeout(() => { initMap(); map && map.invalidateSize(); }, 300);
});
</script>
<%@ include file="/WEB-INF/jsp/chatbot.jsp" %>
</body>
</html>
