/**
 * api.js — centralised fetch helpers for all domain API calls.
 * Import this file in any page that needs live data from the backend.
 */

const API_BASE = '';

async function apiFetch(url, options = {}) {
    try {
        const response = await fetch(API_BASE + url, {
            headers: { 'Content-Type': 'application/json', ...options.headers },
            ...options
        });
        if (!response.ok) throw new Error(`HTTP ${response.status}`);
        return await response.json();
    } catch (err) {
        console.error(`API error [${url}]:`, err);
        return null;
    }
}

// Submits a hidden POST form so mutating actions hit @PostMapping endpoints correctly
function postForm(action) {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = action;
    document.body.appendChild(form);
    form.submit();
}

// ─── Vehicles ────────────────────────────────────────────────────────────────
const VehicleAPI = {
    getAll:       () => apiFetch('/api/admin/vehicles'),
    getAvailable: () => apiFetch('/api/vehicles'),
};

// ─── Bookings ─────────────────────────────────────────────────────────────────
const BookingAPI = {
    getAdminAll:  ()  => apiFetch('/api/admin/bookings'),
    getUserAll:   ()  => apiFetch('/api/user/bookings'),
    cancel:   (id) => postForm(`/bookings/cancel/${id}`),
    complete: (id) => postForm(`/bookings/complete/${id}`),
    delete:   (id) => postForm(`/bookings/delete/${id}`),
};

// ─── Payments ─────────────────────────────────────────────────────────────────
const PaymentAPI = {
    getAdminAll:  ()  => apiFetch('/api/admin/payments'),
    getUserAll:   ()  => apiFetch('/api/user/payments'),
    delete:      (id) => postForm(`/payments/delete/${id}`),
    markOverdue: (id) => postForm(`/payments/overdue/${id}`),
};

// ─── Reviews ──────────────────────────────────────────────────────────────────
const ReviewAPI = {
    getAdminAll: ()  => apiFetch('/api/admin/reviews'),
    getUserAll:  ()  => apiFetch('/api/user/reviews'),
    getStats:    ()  => apiFetch('/api/admin/reviews/stats'),
    getCharts:   ()  => apiFetch('/api/admin/reviews/charts'),
    approve: (id) => apiFetch(`/api/admin/reviews/status/${id}`, { method: 'POST', body: '{}' }),
    delete:  (id) => postForm(`/reviews/delete/${id}`),
};

// ─── Users ────────────────────────────────────────────────────────────────────
const UserAPI = {
    getAll:  ()  => apiFetch('/api/admin/users'),
    delete:  (id) => postForm(`/users/delete/${id}`),
    edit:    (id) => { window.location.href = `/users/edit/${id}`; },
};

// ─── Dashboard ────────────────────────────────────────────────────────────────
const DashboardAPI = {
    getStats:          () => apiFetch('/api/admin/dashboard/stats'),
    getRecentBookings: () => apiFetch('/api/admin/dashboard/recent-bookings'),
    getRecentUsers:    () => apiFetch('/api/admin/dashboard/recent-users'),
    getCharts:         () => apiFetch('/api/admin/dashboard/charts'),
};
