function filtrarTabla() {
        const filtro = document.getElementById('buscador').value.toLowerCase();
        const filas = document.querySelectorAll('tbody tr');
        filas.forEach(fila => {
            const texto = fila.textContent.toLowerCase();
            fila.style.display = texto.includes(filtro) ? '' : 'none';
        });
    }