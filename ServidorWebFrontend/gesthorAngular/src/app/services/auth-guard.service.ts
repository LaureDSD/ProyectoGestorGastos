import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

/**
 * Guardia de ruta que protege rutas privadas requiriendo autenticación.
 * Implementa CanActivate para decidir si se permite o no el acceso a una ruta.
 */
@Injectable({
  providedIn: 'root' // Disponible globalmente para usar en el routing module
})
export class AuthGuard implements CanActivate {

  /**
   * Constructor que inyecta el router para redirecciones.
   * @param router Servicio Router para navegación programática.
   */
  constructor(private router: Router) {}

  /**
   * Método que determina si se puede activar una ruta.
   * Verifica si existe un token de autenticación en localStorage.
   * - Si no existe token, redirige al login y bloquea acceso.
   * - Si existe token, permite el acceso.
   * @returns true si la ruta puede activarse; false en caso contrario.
   */
  canActivate(): boolean {
    const token = localStorage.getItem('token');

    // Si no hay token, redirige al login y deniega el acceso
    if (!token) {
      this.router.navigate(['/login']);
      return false;
    }

    // Si hay token, permite el acceso
    return true;
  }
}
