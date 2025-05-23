/**
 * Componente Angular SectionCardsComponent
 *
 * Este componente muestra una sección que contiene múltiples tarjetas dispuestas en un contenedor flexible.
 * Cada tarjeta se renderiza usando el componente hijo <app-section-card>, recibiendo título y texto como propiedades.
 *
 * Características:
 * - La sección utiliza Flexbox para alinear las tarjetas en fila, con envoltura (`flex-wrap`) para adaptarse a diferentes tamaños de pantalla.
 * - Se aplica un espaciado uniforme (`gap`) entre las tarjetas.
 * - El contenido dentro de la sección está centrado horizontalmente y con texto alineado al centro.
 * - Las tarjetas se generan dinámicamente a partir de una lista de objetos, cada uno con propiedades `title` y `text`.
 *
 * Propiedades de entrada (@Input):
 * - cardList: arreglo de objetos con la forma { title: string; text: string } que define el contenido de cada tarjeta.
 *
 * Uso:
 * <app-section-cards [cardList]="listaDeTarjetas"></app-section-cards>
 *
 * Donde `listaDeTarjetas` es un array con elementos que contienen `title` y `text`.
 */

import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-section-cards',
  standalone: false,
  template: `
    <section
      style="padding: 2rem; display: flex; justify-content: center; flex-wrap: wrap; gap: 2rem; text-align: center;"
    >
      <app-section-card
        *ngFor="let e of cardList"
        [title]="e.title"
        [text]="e.text"
      >
      </app-section-card>
    </section>
  `
})
export class SectionCardsComponent {
  /**
   * Lista de objetos que representan las tarjetas a mostrar.
   * Cada objeto debe tener las propiedades:
   * - title: string con el título de la tarjeta
   * - text: string con el texto descriptivo de la tarjeta
   */
  @Input() cardList: { title: string; text: string }[] = [];
}
