import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

/**
 * TokenInterceptor es un servicio que implementa HttpInterceptor para
 * interceptar todas las peticiones HTTP salientes de la aplicación.
 *
 * Funcionalidad principal:
 * - Añade automáticamente el token de autenticación almacenado en localStorage
 *   en el encabezado Authorization de cada solicitud HTTP.
 * - Maneja errores HTTP, específicamente errores 401 (No autorizado).
 *   En caso de recibir un error 401, elimina el token de localStorage y redirige al usuario
 *   a la página de login.
 */
@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  /**
   * Constructor que inyecta el servicio Router para poder redirigir al usuario.
   * @param router Servicio Router de Angular para navegación programática.
   */
  constructor(private router: Router) {}

  /**
   * Método intercept que intercepta cada solicitud HTTP antes de ser enviada.
   *
   * @param req La solicitud HTTP entrante.
   * @param next El manejador para continuar con la cadena de interceptores o enviar la solicitud.
   * @returns Un Observable que emite los eventos HTTP y maneja errores.
   *
   * Proceso:
   * 1. Obtiene el token almacenado en localStorage bajo la clave 'token'.
   * 2. Si el token existe, clona la solicitud añadiendo el encabezado Authorization con el token Bearer.
   * 3. Envía la solicitud modificada o original.
   * 4. Atrapa errores HTTP con catchError.
   * 5. Si el error es 401, elimina el token y redirige al login.
   * 6. Re-lanza el error para que pueda ser manejado por otros componentes o servicios.
   */
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Recuperar token del localStorage
    const token = localStorage.getItem('token');

    // Si el token existe, añadirlo en el encabezado Authorization
    if (token) {
      req = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
    }

    // Continuar con la solicitud HTTP y manejar posibles errores
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        // Si el error es 401 (Unauthorized), eliminar token y redirigir a login
        if (error.status === 401) {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
        }

        // Re-lanzar el error para que otros puedan manejarlo si es necesario
        return throwError(error);
      })
    );
  }
}
