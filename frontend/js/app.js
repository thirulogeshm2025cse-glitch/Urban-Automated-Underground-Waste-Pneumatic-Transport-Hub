/**
 * Urban Automated Underground Waste Pneumatic Transport Hub
 * Frontend Master Controller & API Bridge
 */

const API_BASE = 'http://localhost:8080/api';

// Live Clock in Top Header
function initClock() {
    const clockEl = document.getElementById('liveClock');
    if (!clockEl) return;
    const update = () => {
        const now = new Date();
        clockEl.textContent = now.toLocaleDateString('en-US', {
            month: 'short', day: '2-digit', year: 'numeric'
        }) + ' | ' + now.toLocaleTimeString('en-US', { hour12: false });
    };
    update();
    setInterval(update, 1000);
}

// Active Nav Highlighter
function highlightActiveNav() {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-item');
    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (currentPath.endsWith(href) || (href === 'dashboard.html' && currentPath.endsWith('/'))) {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
        }
    });
}

// API Request Wrapper
async function apiRequest(endpoint, method = 'GET', body = null) {
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    };
    if (body) {
        options.body = JSON.stringify(body);
    }
    try {
        const res = await fetch(`${API_BASE}${endpoint}`, options);
        if (res.status === 204 || (method === 'DELETE' && res.ok)) {
            return { success: true };
        }
        const data = await res.json().catch(() => null);
        if (!res.ok) {
            let errorMsg = (data && data.message) ? data.message : `HTTP error: ${res.status}`;
            if (data && data.fieldErrors) {
                const fields = Object.entries(data.fieldErrors).map(([k, v]) => `${k}: ${v}`).join(', ');
                errorMsg += ` (${fields})`;
            }
            throw new Error(errorMsg);
        }
        return data;
    } catch (err) {
        console.error(`API Error [${method} ${endpoint}]:`, err);
        throw err;
    }
}

// Toast Notifications
function showToast(message, type = 'success') {
    let container = document.getElementById('toastContainer');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toastContainer';
        container.className = 'toast-container';
        document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    const icon = type === 'success' ? '✓' : type === 'error' ? '✕' : 'ℹ';
    toast.innerHTML = `<span style="font-weight: bold; font-size: 1.1rem;">${icon}</span><span>${message}</span>`;

    container.appendChild(toast);
    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(100%)';
        toast.style.transition = 'all 0.3s ease';
        setTimeout(() => toast.remove(), 300);
    }, 4000);
}

// Modal Handlers
function openModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.add('active');
    }
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.remove('active');
    }
}

// Waste Tag Generator
function getWasteTag(wasteType) {
    const type = (wasteType || 'GENERAL').toUpperCase();
    const cssClass = type.toLowerCase();
    return `<span class="waste-tag ${cssClass}">${type}</span>`;
}

// Status Badge Generator
function getStatusBadge(status) {
    const s = (status || 'NORMAL').toUpperCase();
    let badgeClass = 'badge-inactive';
    if (s === 'NORMAL' || s === 'ACTIVE' || s === 'COMPLETED') {
        badgeClass = 'badge-normal';
    } else if (s === 'WARNING' || s === 'STANDBY' || s === 'IN_TRANSIT' || s === 'IN_PROGRESS') {
        badgeClass = 'badge-warning';
    } else if (s === 'CRITICAL' || s === 'FULL' || s === 'BLOCKED') {
        badgeClass = 'badge-critical';
    }
    return `<span class="badge ${badgeClass}"><span class="status-dot" style="animation:none; width:6px; height:6px;"></span>${s}</span>`;
}

// Fill Progress Bar Generator
function getProgressBar(percentage, current, max, unit = 'kg') {
    const pct = Math.min(100, Math.max(0, percentage || 0));
    let barColor = 'bar-normal';
    if (pct >= 80) {
        barColor = 'bar-critical';
    } else if (pct >= 60) {
        barColor = 'bar-warning';
    }
    return `
        <div class="progress-container">
            <div class="progress-info">
                <span>${current} / ${max} ${unit}</span>
                <span>${pct}%</span>
            </div>
            <div class="progress-track">
                <div class="progress-bar ${barColor}" style="width: ${pct}%"></div>
            </div>
        </div>
    `;
}

// Format Date Time
function formatDateTime(dtStr) {
    if (!dtStr) return '—';
    try {
        const d = new Date(dtStr);
        return d.toLocaleDateString('en-US', { month: 'short', day: '2-digit' }) + ' ' +
               d.toLocaleTimeString('en-US', { hour12: false, hour: '2-digit', minute: '2-digit' });
    } catch {
        return dtStr;
    }
}

// Update Active Alert Count Badge in Sidebar
async function updateSidebarAlertBadge() {
    try {
        const stats = await apiRequest('/dashboard/stats');
        const badge = document.getElementById('sidebarAlertBadge');
        if (badge) {
            if (stats && stats.activeAlerts > 0) {
                badge.textContent = stats.activeAlerts;
                badge.style.display = 'inline-block';
            } else {
                badge.style.display = 'none';
            }
        }
    } catch (e) {
        // silent fallback if server offline
    }
}

document.addEventListener('DOMContentLoaded', () => {
    initClock();
    highlightActiveNav();
    updateSidebarAlertBadge();
});
