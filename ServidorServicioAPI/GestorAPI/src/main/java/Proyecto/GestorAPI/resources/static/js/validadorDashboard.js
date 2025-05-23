document.addEventListener("DOMContentLoaded", function () {
    const token = localStorage.getItem('token');

    if (!token) {
        window.location.href = '/auth/login';
        return;
    }

    fetch('/api/users/data', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('No autorizado o token inválido');
            }
            return response.json();
        })
        .then(data => {
            // Mostrar nombre de usuario
            document.getElementById('username').textContent = data.username;

            // Mostrar roles como texto
            const roles = data.rol.map(r => r.authority);
            document.getElementById('roles').textContent = roles.join(', ');

            // Mostrar contenido condicional
            if (roles.includes("ADMIN")) {
                document.getElementById('adminSection').style.display = 'block';
            } else {
                document.getElementById('noAccessSection').style.display = 'block';
            }

            // (Opcional) mostrar mensaje en consola o añadirlo al DOM
            console.log(data.message);
        })
        .catch(error => {
            console.error('Error:', error);
            localStorage.removeItem('token');
            window.location.href = '/auth/login';
        });

    // Cerrar sesión
    const logoutButton = document.getElementById('logoutButton');
    if (logoutButton) {
        logoutButton.addEventListener('click', function () {
            localStorage.removeItem('token');
            window.location.href = '/auth/logout';
        });
    }
});
