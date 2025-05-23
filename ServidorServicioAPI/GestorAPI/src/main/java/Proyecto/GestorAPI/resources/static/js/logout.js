document.addEventListener("DOMContentLoaded", function () {
    const logoutButton = document.getElementById('logoutButton');
    if (logoutButton) {
        logoutButton.addEventListener('click', function () {
            localStorage.removeItem('token');
            window.location.href = '/auth/logout';
        });
    }
});

history.pushState(null, null, location.href);
    window.onpopstate = function() {
        history.go(1);
    };