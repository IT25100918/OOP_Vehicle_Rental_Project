<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Book Vehicle - Elite Wheel Rentals</title>
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
        <a href="${pageContext.request.contextPath}/vehicles" class="btn btn-outline-gold btn-sm">← Back</a>
    </div>
</nav>

<div class="container" style="max-width:640px;margin-top:48px;padding-bottom:60px">
    <!-- Vehicle Preview -->
    <div class="card-dark mb-4">
        <div class="row g-0">
            <div class="col-4">
                <img src="${vehicle.imageUrl}" alt="${vehicle.brand}"
                     style="width:100%;height:160px;object-fit:cover;border-radius:12px 0 0 12px"
                     onerror="this.src='https://via.placeholder.com/200x160?text=Vehicle'">
            </div>
            <div class="col-8 card-body">
                <div class="vehicle-type">${vehicle.type}</div>
                <div class="vehicle-name">${vehicle.brand} ${vehicle.model}</div>
                <div class="mt-2">
                    <span class="vehicle-price">Rs. ${vehicle.rentPrice}/day</span>
                </div>
            </div>
        </div>
    </div>

    <div class="form-dark">
        <div class="mb-4">
            <h2 style="font-family:'Rajdhani',sans-serif;font-weight:700">Book <span class="text-gold">This Vehicle</span></h2>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger-dark mb-3">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/bookings/create">
            <input type="hidden" name="vehicleId" value="${vehicle.vehicleId}">
            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label">Start Date</label>
                    <input type="date" class="form-control" name="startDate" id="startDate" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">End Date</label>
                    <input type="date" class="form-control" name="endDate" id="endDate" required>
                </div>
                <div class="col-12">
                    <label class="form-label">Booking Type</label>
                    <select class="form-select" name="bookingType">
                        <option value="online">🌐 Online Booking</option>
                        <option value="walkin">🚶 Walk-in</option>
                    </select>
                </div>

                <!-- Cost Preview -->
                <div class="col-12" id="costPreview" style="display:none">
                    <div class="p-3" style="background:var(--dark-4);border-radius:8px;border:1px solid var(--border)">
                        <div class="d-flex justify-content-between">
                            <span style="color:var(--gray)">Duration:</span>
                            <span id="totalDays" style="font-weight:600">0 days</span>
                        </div>
                        <div class="d-flex justify-content-between mt-2">
                            <span style="color:var(--gray)">Price per day:</span>
                            <span>Rs. ${vehicle.rentPrice}</span>
                        </div>
                        <hr class="divider">
                        <div class="d-flex justify-content-between">
                            <span style="color:var(--gold);font-weight:600">Estimated Total:</span>
                            <span style="color:var(--gold);font-weight:700;font-size:1.1rem">Rs. <span id="totalCost">0</span></span>
                        </div>
                    </div>
                </div>

                <div class="col-12 mt-2">
                    <button type="submit" class="btn btn-gold w-100">
                        <i class="fas fa-check me-2"></i>Confirm Booking
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    const pricePerDay = parseFloat('${vehicle.rentPrice}');
    const startInput = document.getElementById('startDate');
    const endInput   = document.getElementById('endDate');
    const preview    = document.getElementById('costPreview');

    function calculate() {
        if (!startInput.value || !endInput.value) return;
        const [sy, sm, sd] = startInput.value.split('-').map(Number);
        const [ey, em, ed] = endInput.value.split('-').map(Number);
        const start = new Date(sy, sm - 1, sd);
        const end   = new Date(ey, em - 1, ed);
        const days  = Math.round((end - start) / (1000 * 60 * 60 * 24));
        if (days >= 1) {
            document.getElementById('totalDays').textContent = days + ' day(s)';
            document.getElementById('totalCost').textContent = (days * pricePerDay).toLocaleString('en-LK', {minimumFractionDigits:2});
            preview.style.display = 'block';
        } else {
            preview.style.display = 'none';
        }
    }

    const today = new Date().toISOString().split('T')[0];
    startInput.min = today;
    startInput.addEventListener('change', () => { endInput.min = startInput.value; calculate(); });
    endInput.addEventListener('change', calculate);
</script>
<%@ include file="/WEB-INF/jsp/chatbot.jsp" %>
</body>
</html>
