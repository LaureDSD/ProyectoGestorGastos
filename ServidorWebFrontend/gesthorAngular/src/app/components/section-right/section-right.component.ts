/**
 * Componente Angular SectionRightComponent
 *
 * Este componente muestra una sección dividida en dos partes:
 * 1. Un área visual que contiene una imagen de fondo con una forma recortada usando clip-path.
 * 2. Un área de texto con un título y un párrafo descriptivo.
 *
 * Características:
 * - Usa Flexbox para organizar horizontalmente los dos bloques con flex-wrap para responsividad.
 * - La imagen se presenta con un recorte poligonal para un efecto visual estilizado.
 * - El bloque de texto tiene padding y márgenes laterales para separación visual.
 * - Los estilos son aplicados inline en la plantilla.
 *
 * Propiedades de entrada (@Input):
 * - title: string que define el título mostrado en el área textual.
 * - text: string con el contenido descriptivo mostrado debajo del título.
 * - img: string con la URL o ruta de la imagen usada como fondo en el área visual.
 *
 * Uso:
 * <app-section-right [title]="titulo" [text]="descripcion" [img]="rutaImagen"></app-section-right>
 */

import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-section-right',
  standalone: false,
  template: `
    <section style="display: flex; flex-wrap: wrap; align-items: center;">
      <!-- Div que contiene la imagen con clip-path para recorte angular -->
      <div
        style="flex: 1; clip-path: polygon(0 0, 100% 0, 85% 100%, 0% 100%); height: 300px;"
        [ngStyle]="{
          'background-image': 'url(' + img + ')',
          'background-position': 'center',
          'background-size': 'cover',
          'background-repeat': 'no-repeat'
        }">
      </div>

      <!-- Div para el contenido textual con padding y margen -->
      <div style="flex: 1; padding: 2rem; margin-left: 4%; margin-right: -4%;">
        <!-- Título dinámico -->
        <h2>{{ title }}</h2>
        <!-- Texto descriptivo dinámico -->
        <p>{{ text }}</p>
      </div>
    </section>
  `
})
export class SectionRightComponent {
  /**
   * Título que se mostrará en el área textual del componente.
   * Tipo: string
   */
  @Input() title = "";

  /**
   * Texto descriptivo que se mostrará debajo del título.
   * Tipo: string
   */
  @Input() text = "";

  /**
   * URL o ruta de la imagen que se mostrará como fondo en el área visual.
   * Tipo: string
   */
  @Input() img = "";
}
