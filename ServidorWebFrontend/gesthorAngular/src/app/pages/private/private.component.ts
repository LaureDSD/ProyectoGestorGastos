import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

/**
 * Componente PrivateComponent
 *
 * Este componente representa una vista privada dentro de la aplicación, accesible solo para usuarios
 * autenticados o autorizados. Su selector es 'app-private' y utiliza un archivo de plantilla externo.
 *
 * Funcionalidad:
 * - Implementa la interfaz OnInit para ejecutar lógica al inicializar el componente.
 * - Actualmente, el método ngOnInit contiene una línea comentada que redirigiría al usuario a la ruta '/login'.
 *   Esto sugiere que, en un futuro o bajo ciertas condiciones, se podría implementar una verificación
 *   de autenticación que redirija a usuarios no autorizados hacia el login.
 *
 * Dependencias:
 * - Router de Angular para navegación programática dentro de la app.
 */
@Component({
  selector: 'app-private',              // Selector para usar el componente en templates
  standalone: false,                    // No es un componente standalone, depende de un módulo
  templateUrl: './private.component.html'  // Ruta del archivo HTML asociado a la vista
})
export class PrivateComponent implements OnInit {

  /**
   * Constructor del componente.
   *
   * @param router Inyección del servicio Router para controlar la navegación
   */
  constructor(private router: Router) { }

  /**
   * Método ngOnInit
   *
   * Se ejecuta automáticamente al inicializar el componente.
   * Actualmente la línea para redirigir al login está comentada, indicando posible futura implementación.
   */
  ngOnInit(): void {
    // this.router.navigate(['/login']);
  }

}
