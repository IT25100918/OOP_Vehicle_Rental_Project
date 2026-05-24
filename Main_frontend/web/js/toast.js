/**
 * toast.js — lightweight toast notification system.
 * Usage: showToast('Message here', 'success' | 'error' | 'info' | 'warning')
 */

function showToast(message, type = 'info', duration = 4000) {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.style.cssText = `
            position: fixed; top: 1.5rem; right: 1.5rem; z-index: 9999;
            display: flex; flex-direction: column; gap: 0.5rem;
        `;
        document.body.appendChild(container);
    }

    const colours = {
        success: { bg: 'rgba(59,184,107,0.12)', border: '#3bb86b', icon: '✓' },
        error:   { bg: 'rgba(224,90,90,0.12)',  border: '#e05a5a', icon: '✕' },
        warning: { bg: 'rgba(201,160,61,0.12)', border: '#c9a03d', icon: '⚠' },
        info:    { bg: 'rgba(74,158,255,0.12)', border: '#4a9eff', icon: 'ℹ' },
    };
    const c = colours[type] || colours.info;

    const toast = document.createElement('div');
    toast.style.cssText = `
        background: #16161b; border: 1px solid ${c.border}; border-left: 3px solid ${c.border};
        color: #e5e5e5; padding: 0.75rem 1rem; border-radius: 10px;
        font-size: 0.85rem; max-width: 320px; display: flex; align-items: center; gap: 0.6rem;
        box-shadow: 0 4px 20px rgba(0,0,0,0.4);
        animation: slideIn 0.25s ease; cursor: pointer;
    `;
    toast.innerHTML = `<span style="color:${c.border};font-weight:700;">${c.icon}</span><span>${message}</span>`;

    if (!document.getElementById('toast-style')) {
        const style = document.createElement('style');
        style.id = 'toast-style';
        style.textContent = `
            @keyframes slideIn { from { opacity:0; transform:translateX(30px); } to { opacity:1; transform:translateX(0); } }
            @keyframes fadeOut { to { opacity:0; transform:translateX(30px); } }
        `;
        document.head.appendChild(style);
    }

    container.appendChild(toast);
    toast.addEventListener('click', () => removeToast(toast));

    setTimeout(() => removeToast(toast), duration);
}

function removeToast(toast) {
    toast.style.animation = 'fadeOut 0.25s ease forwards';
    setTimeout(() => toast.remove(), 250);
}

// Convenience aliases
const showAlert  = (msg) => showToast(msg, 'info');
const showSuccess = (msg) => showToast(msg, 'success');
const showError  = (msg) => showToast(msg, 'error');
