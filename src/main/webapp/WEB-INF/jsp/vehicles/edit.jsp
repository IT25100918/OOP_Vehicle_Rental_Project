<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Vehicle - Elite Wheel Rentals</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>
    <style>
      #edit-map { width:100%; height:280px; border-radius:12px; border:1px solid rgba(212,168,67,0.3); }
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
        <a href="${pageContext.request.contextPath}/vehicles" class="btn btn-outline-gold btn-sm">← Back to Vehicles</a>
    </div>
</nav>

<div class="container" style="max-width:620px;margin-top:48px;padding-bottom:60px">
    <div class="form-dark">
        <div class="mb-4">
            <h2 style="font-family:'Rajdhani',sans-serif;font-weight:700">Edit <span class="text-gold">Vehicle</span></h2>
            <p style="color:var(--gray)">ID: ${vehicle.vehicleId}</p>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/vehicles/edit/${vehicle.vehicleId}">
            <input type="hidden" name="plateNumber" value="${vehicle.plateNumber}">
            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label">Vehicle Type</label>
                    <select class="form-select" name="type">
                        <option value="car" ${vehicle.type == 'car' ? 'selected' : ''}>🚗 Car</option>
                        <option value="suv" ${vehicle.type == 'suv' ? 'selected' : ''}>🚙 SUV</option>
                        <option value="van" ${vehicle.type == 'van' ? 'selected' : ''}>🚐 Van</option>
                        <option value="bike" ${vehicle.type == 'bike' ? 'selected' : ''}>🏍️ Bike</option>
                        <option value="threewheeler" ${vehicle.type == 'threewheeler' ? 'selected' : ''}>🛺 Three-Wheeler</option>
                        <option value="bus" ${vehicle.type == 'bus' ? 'selected' : ''}>🚌 Bus</option>
                        <option value="lorry" ${vehicle.type == 'lorry' ? 'selected' : ''}>🚛 Lorry</option>
                    </select>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Brand</label>
                    <input type="text" class="form-control" name="brand" value="${vehicle.brand}" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Model</label>
                    <input type="text" class="form-control" name="vehicleModel" value="${vehicle.model}" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Rent Price (Rs./day)</label>
                    <input type="number" class="form-control" name="rentPrice" value="${vehicle.rentPrice}" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Availability</label>
                    <select class="form-select" name="availability">
                        <option value="available" ${vehicle.availability == 'available' ? 'selected' : ''}>✅ Available</option>
                        <option value="rented" ${vehicle.availability == 'rented' ? 'selected' : ''}>🔴 Rented</option>
                    </select>
                </div>
                <div class="col-12">
                    <label class="form-label">Image URL</label>
                    <input type="url" class="form-control" name="imageUrl" value="${vehicle.imageUrl}">
                </div>
                <div class="col-12">
                    <label class="form-label"><i class="fas fa-map-marker-alt me-1" style="color:#D4A843"></i>Vehicle Location <span style="color:var(--gray)">(click map to change pin)</span></label>
                    <input type="hidden" name="location" id="locationInput" value="${vehicle.location}">
                    <div id="edit-map"></div>
                    <small style="color:var(--gray)" id="locationText">
                        <c:choose>
                            <c:when test="${vehicle.hasLocation()}">📍 Current: ${vehicle.lat}, ${vehicle.lng} — click to move</c:when>
                            <c:otherwise>No location set — click on the map to set pickup point</c:otherwise>
                        </c:choose>
                    </small>
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
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
<script>
(function() {
    // Existing location from server (may be empty)
    const existingLat = parseFloat('${vehicle.lat}') || 0;
    const existingLng = parseFloat('${vehicle.lng}') || 0;
    const hasExisting = existingLat !== 0;

    const initCenter = hasExisting ? [existingLat, existingLng] : [7.8731, 80.7718];
    const initZoom   = hasExisting ? 13 : 7;

    const editMap = L.map('edit-map').setView(initCenter, initZoom);
    L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png', {
        attribution: '© OpenStreetMap © CARTO', subdomains: 'abcd', maxZoom: 19
    }).addTo(editMap);

    let pinMarker = null;

    // Pre-place existing marker if location is set
    if (hasExisting) {
        pinMarker = L.marker([existingLat, existingLng])
            .addTo(editMap)
            .bindPopup('Current location — click map to move')
            .openPopup();
    }

    editMap.on('click', function(e) {
        const { lat, lng } = e.latlng;
        const locStr = lat.toFixed(6) + ',' + lng.toFixed(6);
        document.getElementById('locationInput').value = locStr;
        document.getElementById('locationText').textContent =
            '📍 Location updated: ' + lat.toFixed(5) + ', ' + lng.toFixed(5);
        if (pinMarker) editMap.removeLayer(pinMarker);
        pinMarker = L.marker([lat, lng]).addTo(editMap)
            .bindPopup('Vehicle location').openPopup();
    });
})();
</script>
<%@ include file="/WEB-INF/jsp/chatbot.jsp" %>
</body>
</html>
