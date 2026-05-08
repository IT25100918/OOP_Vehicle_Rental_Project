// ── Confirm Delete ────────────────────────────
function confirmDelete(id, name) {
  if (confirm(`Remove "${name}" from inventory?\n\nThis action cannot be undone.`)) {
    document.getElementById('delete-form-' + id).submit();
  }
}

// ── Auto-dismiss alerts ───────────────────────
document.addEventListener('DOMContentLoaded', () => {
  const alerts = document.querySelectorAll('.alert');
  alerts.forEach(alert => {
    setTimeout(() => {
      alert.style.transition = 'opacity 0.5s';
      alert.style.opacity = '0';
      setTimeout(() => alert.remove(), 500);
    }, 4000);
  });

  // Highlight active nav link
  const path = window.location.pathname;
  document.querySelectorAll('.navbar-links a').forEach(link => {
    if (link.getAttribute('href') === path ||
        (path.startsWith(link.getAttribute('href')) && link.getAttribute('href') !== '/')) {
      link.classList.add('active');
    }
  });
});
