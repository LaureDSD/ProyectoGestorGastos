/**
 * Componente Angular SectionLeftComponent
 *
 * Este componente presenta una sección dividida en dos partes:
 * 1. Un bloque de texto a la izquierda que contiene un título y un párrafo descriptivo.
 * 2. Un bloque visual a la derecha con una imagen de fondo recortada mediante clip-path para un efecto estilizado.
 *
 * Características:
 * - Usa Flexbox con `flex-wrap: wrap-reverse` para que en pantallas pequeñas la imagen aparezca arriba y el texto abajo.
 * - El bloque de texto tiene padding y margen lateral para separar visualmente el contenido.
 * - La imagen se adapta al área usando estilos dinámicos para el fondo, con recorte angular definido por clip-path.
 * - Los estilos están definidos inline dentro del template del componente.
 *
 * Propiedades de entrada (@Input):
 * - title: string que representa el título mostrado en el bloque textual.
 * - text: string con el contenido descriptivo que aparece debajo del título.
 * - img: string con la URL o ruta de la imagen que se mostrará como fondo en el bloque visual.
 *
 * Uso:
 * <app-section-left [title]="titulo" [text]="descripcion" [img]="rutaImagen"></app-section-left>
 */

import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-section-left',
  standalone: false,
  template: `
    <section style="display: flex; flex-wrap: wrap-reverse; align-items: center;">
      <!-- Bloque de texto alineado a la izquierda con margen y padding -->
      <div style="flex: 1; padding: 2rem; margin-left: 4%; margin-right: -4%;">
        <h2 class="ms-5">{{ title }}</h2>
        <p class="ms-5">{{ text }}</p>
      </div>

      <!-- Bloque de imagen con clip-path para recorte poligonal -->
      <div
        style="flex: 1; clip-path: polygon(15% 0, 100% 0, 100% 100%, 0% 100%); height: 300px;"
        [ngStyle]="{
          'background-image': 'url(' + img + ')',
          'background-position': 'center',
          'background-size': 'cover',
          'background-repeat': 'no-repeat'
        }">
      </div>
    </section>
  `
})
export class SectionLeftComponent {
  /**
   * Título mostrado en el bloque de texto del componente.
   * Tipo: string
   */
  @Input() title = "";

  /**
   * Texto descriptivo mostrado debajo del título.
   * Tipo: string
   */
  @Input() text = "";

  /**
   * URL o ruta de la imagen usada como fondo en el bloque visual.
   * Tipo: string
   */
  @Input() img = "";
}
