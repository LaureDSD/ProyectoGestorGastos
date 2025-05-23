import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';

/**
 * Componente Angular encargado de gestionar la redirección
 * tras un proceso de autenticación OAuth2 externo.
 *
 * Muestra un spinner mientras se procesa la redirección.
 * Obtiene el token de autenticación desde los parámetros de la URL,
 * lo almacena en localStorage y redirige al usuario a la página principal.
 * Si no recibe token, redirige al login.
 */
@Component({
  selector: 'app-oauth2-redirect',
  standalone: false,
  template: `
    <div class="text-center mt-5">
      <div class="spinner-border" role="status"></div>
      <p>Redirigiendo…</p>
    </div>
  `
})
export class OAuth2RedirectComponent implements OnInit {

  /**
   * Constructor que inyecta ActivatedRoute para acceder a parámetros de la URL
   * y Router para realizar navegación programática.
   * @param route ActivatedRoute para acceder a parámetros query de la URL
   * @param router Router para redirigir al usuario
   */
  constructor(private route: ActivatedRoute, private router: Router) {}

  /**
   * Método del ciclo de vida OnInit.
   * Al inicializar el componente:
   * - Se obtiene el parámetro 'token' de la URL.
   * - Si existe token, se almacena en localStorage y se redirige a '/home'.
   * - Si no existe token, se redirige a '/login'.
   */
  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token');
    if (token) {
      localStorage.setItem('token', token);
      this.router.navigate(['/home']);
    } else {
      this.router.navigate(['/login']);
    }
  }
}
