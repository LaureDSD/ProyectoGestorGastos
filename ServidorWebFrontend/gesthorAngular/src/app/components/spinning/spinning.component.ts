/**
 * Componente Angular SpinningComponent
 *
 * Componente visual que muestra un spinner tipo "grow" (crecimiento) utilizando clases de Bootstrap.
 * Indica que una operación o proceso está en curso.
 *
 * Características:
 * - Spinner con animación "grow"
 * - Accesibilidad: incluye texto oculto para lectores de pantalla ("Loading...")
 */

import { Component } from '@angular/core';

@Component({
  selector: 'app-spinning',       // Selector para usar el componente en templates
  standalone: false,               // No es standalone, debe declararse en un módulo
  template: `
    <div class="spinner-grow" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
  `                             // HTML inline con spinner Bootstrap "grow"
})
export class SpinningComponent {
  // Componente básico sin lógica ni propiedades adicionales
}
