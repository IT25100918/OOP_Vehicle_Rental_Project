/**
 * auth.js — client-side session role helpers.
 * The server manages actual session state; these helpers read the
 * Thymeleaf-rendered user object embedded in the page.
 */

function getCurrentUserRole() {
    const el = document.getElementById('current-user-role');
    return el ? el.dataset.role : null;
}

function isAdmin() {
    const role = getCurrentUserRole();
    return role === 'ADMIN' || role === 'SUPER_ADMIN';
}

function isSuperAdmin() {
    return getCurrentUserRole() === 'SUPER_ADMIN';
}

function isUser() {
    return getCurrentUserRole() === 'USER';
}

/** Redirect to login if the hidden role element is missing (unauthenticated). */
function requireAuth() {
    if (!document.getElementById('current-user-role')) {
        window.location.href = '/login';
        return false;
    }
    return true;
}

/** Hide elements that should only show to admins. */
function applyRoleVisibility() {
    document.querySelectorAll('[data-admin-only]').forEach(el => {
        el.style.display = isAdmin() ? '' : 'none';
    });
    document.querySelectorAll('[data-user-only]').forEach(el => {
        el.style.display = isUser() ? '' : 'none';
    });
}

document.addEventListener('DOMContentLoaded', applyRoleVisibility);
