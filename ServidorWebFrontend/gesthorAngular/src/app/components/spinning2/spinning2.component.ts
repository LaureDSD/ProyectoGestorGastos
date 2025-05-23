/**
 * Componente Angular Spinning2Component
 *
 * Componente visual que muestra un spinner (indicador de carga) con estilos Bootstrap.
 * Usado para indicar que una operación o carga está en proceso.
 *
 * Características:
 * - Spinner de tamaño fijo (4rem x 4rem)
 * - Color primario (bootstrap "text-primary")
 * - Accesibilidad: incluye texto oculto para lectores de pantalla ("Cargando...")
 */

import { Component } from '@angular/core';

@Component({
  selector: 'app-spinning2',      // Selector para usar el componente en templates
  standalone: false,               // No es standalone, requiere declaración en un módulo
  template: `
      <div class="spinner-border text-primary" role="status" style="width: 4rem; height: 4rem;">
        <span class="visually-hidden">Cargando...</span>
      </div>
  `,                              // HTML inline que define el spinner con Bootstrap
  styles: ``                      // No se definen estilos CSS adicionales
})
export class Spinning2Component {
  // Componente simple sin lógica ni propiedades adicionales
}
