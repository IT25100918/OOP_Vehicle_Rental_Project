<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Vehicle - Elite Wheel Rentals</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>
    <style>
      #add-map { width:100%; height:280px; border-radius:12px; border:1px solid rgba(212,168,67,0.3); }
      .leaflet-container { background: #1a1a2e; }
    </style>
</head>
<body>
<nav class="navbar navbar-dark">
    <div class="container">
        <a class="navbar-brand" href="/" style="display:flex;align-items:center;gap:8px;padding:0">
            <img src="${pageContext.request.contextPath}/logo.png" alt="Elite Wheel Rentals"
                 style="height:54px;width:auto;object-fit:contain;filter:drop-shadow(0 2px 10px rgba(212,168,67,0.4))">
        </a>
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/vehicles" class="btn btn-outline-gold btn-sm">← Back to Vehicles</a>
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-gold btn-sm">Dashboard</a>
        </div>
    </div>
</nav>

<div class="container" style="max-width:620px;margin-top:48px;padding-bottom:60px">
    <div class="form-dark">
        <div class="mb-4">
            <h2 style="font-family:'Rajdhani',sans-serif;font-weight:700">Add New <span class="text-gold">Vehicle</span></h2>
            <p style="color:var(--gray);font-size:0.9rem">Register a vehicle to the fleet</p>
        </div>
        <c:if test="${not empty error}"><div class="alert alert-danger-dark mb-3">${error}</div></c:if>
        <c:if test="${not empty success}"><div class="alert alert-success-dark mb-3">${success}</div></c:if>

        <form method="post" action="${pageContext.request.contextPath}/vehicles/add">
            <input type="hidden" name="plateNumber" id="plateNumber">
            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label">Vehicle Type</label>
                    <select class="form-select" name="type" required>
                        <option value="">-- Select Type --</option>
                        <option value="car">🚗 Car</option>
                        <option value="suv">🚙 SUV</option>
                        <option value="van">🚐 Van</option>
                        <option value="bike">🏍️ Bike</option>
                        <option value="threewheeler">🛺 Three-Wheeler</option>
                        <option value="bus">🚌 Bus</option>
                        <option value="lorry">🚛 Lorry</option>
                    </select>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Brand</label>
                    <input type="text" class="form-control" name="brand" placeholder="Toyota, Honda..." required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Model</label>
                    <input type="text" class="form-control" name="model" placeholder="Corolla, Civic..." required>
                </div>
                <div class="col-12">
                    <label class="form-label">Rent Price (Rs./day)</label>
                    <input type="number" class="form-control" name="rentPrice" placeholder="5000" step="100" min="0" required>
                </div>
                <div class="col-12">
                    <label class="form-label">Image URL <span style="color:var(--gray)">(optional)</span></label>
                    <input type="url" class="form-control" name="imageUrl" placeholder="https://...">
                    <small style="color:var(--gray)">Leave blank for default image</small>
                </div>
                <div class="col-12">
                    <label class="form-label"><i class="fas fa-map-marker-alt me-1" style="color:#D4A843"></i>Vehicle Location <span style="color:var(--gray)">(click map to pin)</span></label>
                    <input type="hidden" name="location" id="locationInput">
                    <div id="add-map"></div>
                    <small style="color:var(--gray)" id="locationText">No location set — click on the map to set pickup point</small>
                </div>
                <div class="col-12 mt-2">
                    <button type="submit" class="btn btn-gold w-100">
                        <i class="fas fa-plus me-2"></i>Add Vehicle
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
<script>
    const rand = Math.random().toString(36).substring(2, 7).toUpperCase();
    document.getElementById('plateNumber').value = 'VH-' + Date.now().toString().slice(-5) + rand;

    // Location picker map
    const addMap = L.map('add-map').setView([7.8731, 80.7718], 7);
    L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png', {
        attribution: '© OpenStreetMap © CARTO', subdomains: 'abcd', maxZoom: 19
    }).addTo(addMap);

    let pinMarker = null;
    addMap.on('click', function(e) {
        const { lat, lng } = e.latlng;
        const locStr = lat.toFixed(6) + ',' + lng.toFixed(6);
        document.getElementById('locationInput').value = locStr;
        document.getElementById('locationText').textContent = '📍 Location set: ' + lat.toFixed(5) + ', ' + lng.toFixed(5);
        if (pinMarker) addMap.removeLayer(pinMarker);
        pinMarker = L.marker([lat, lng]).addTo(addMap).bindPopup('Vehicle location').openPopup();
    });
</script>
<%@ include file="/WEB-INF/jsp/chatbot.jsp" %>
</body>
</html>
