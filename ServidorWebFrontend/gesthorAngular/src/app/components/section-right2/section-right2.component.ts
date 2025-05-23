/**
 * Componente Angular SectionRight2Component
 *
 * Este componente presenta una sección dividida en dos partes principales:
 * 1. Un área visual que muestra una imagen de fondo con un recorte especial (clip-path).
 * 2. Un área textual con un título y un párrafo descriptivo.
 *
 * Características:
 * - Usa estilos inline para definir el diseño con Flexbox y clip-path.
 * - Es responsive, ya que usa flex-wrap para ajustar el contenido en pantallas pequeñas.
 * - La imagen se muestra como fondo en la primera sección, con posicionamiento centrado y tamaño cubierto.
 * - El texto se alinea vertical y horizontalmente en el contenedor derecho.
 *
 * Propiedades de entrada (@Input):
 * - title: string que representa el título a mostrar en la parte textual.
 * - text: string que contiene el texto descriptivo debajo del título.
 * - img: string que debe contener la URL o ruta de la imagen que se muestra como fondo.
 *
 * Uso:
 * <app-section-right2 [title]="titulo" [text]="descripcion" [img]="rutaImagen"></app-section-right2>
 */

import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-section-right2',
  standalone: false,
  template: `
    <section style="display: flex; flex-wrap: wrap;">
      <!-- Div para la imagen con clip-path para forma diagonal -->
      <div
        style="flex: 1; min-width: 300px; clip-path: polygon(0 0, 100% 0, 100% 100%, 0 80%); height: 400px;"
        [ngStyle]="{
          'background-image': 'url(' + img + ')',
          'background-position': 'center',
          'background-size': 'cover',
          'background-repeat': 'no-repeat'
        }">
      </div>

      <!-- Div para el contenido textual centrado vertical y horizontalmente -->
      <div style="flex: 1; min-width: 300px; display: flex; align-items: center; justify-content: center; padding: 2rem;">
        <div>
          <!-- Título dinámico con tamaño de fuente y margen inferior -->
          <h2 style="font-size: 2rem; margin-bottom: 1rem;">{{ title }}</h2>
          <!-- Texto dinámico -->
          <p>{{ text }}</p>
        </div>
      </div>
    </section>
  `
})
export class SectionRight2Component {
  /**
   * Título que se mostrará en la sección derecha.
   * Tipo: string
   */
  @Input() title = "";

  /**
   * Texto descriptivo que se mostrará debajo del título.
   * Tipo: string
   */
  @Input() text = "";

  /**
   * URL o ruta de la imagen que se usará como fondo en la sección izquierda.
   * Tipo: string
   */
  @Input() img = "";
}
