/**
 * FooterComponent
 *
 * Componente Angular que representa el pie de página de la aplicación.
 * Muestra un conjunto de enlaces de navegación y un mensaje de derechos reservados.
 *
 * Propiedades de entrada (@Input):
 * - comania: Nombre de la compañía o entidad que se muestra en el pie de página.
 * - enlaces: Array bidimensional con pares [texto, ruta] para los enlaces del menú.
 *
 * Uso:
 * <app-footer [comania]="'NombreCompañía'" [enlaces]="[['Texto','/ruta'], ...]"></app-footer>
 *
 * Ejemplo de enlaces por defecto:
 * [
 *   ["Privacidad", "/public/privacity"],
 *   ["Contacto y soporte", "/public/contact"],
 *   ["Login", "/login"]
 * ]
 */

import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-footer',
  standalone: false,
  template: `
    <!-- Pie de página con fondo negro y texto claro -->
    <footer class="footer mt-auto py-4 text-center text-light" style="background-color: #000;">

      <!-- Menú de navegación con enlaces centrados y borde inferior -->
      <ul class="nav justify-content-center mb-3" style="border-bottom: 1px solid #e7e6e1;">
        <!-- Itera sobre el arreglo de enlaces para crear un item por cada enlace -->
        <li *ngFor="let e of enlaces" class="nav-item">
          <!-- Enlace con routerLink dinámico y estilo de color con transición -->
          <a
            [routerLink]="[e[1]]"
            class="nav-link px-3"
            style="color: #e6e4db; transition: color 0.3s;"
          >
            <!-- Muestra el texto del enlace con capitalización de título -->
            {{ e[0] | titlecase }}
          </a>
        </li>
      </ul>

      <!-- Mensaje de copyright con año dinámico y nombre de la compañía capitalizado -->
      <p style="color: #888;">
        © {{ fecha }} {{ comania | titlecase }} · Todos los derechos reservados.
      </p>
    </footer>
  `
})
export class FooterComponent {
  /**
   * Año actual que se muestra en el pie de página.
   */
  fecha: number = new Date().getFullYear();

  /**
   * Nombre de la compañía que se mostrará en el pie de página.
   * Se puede modificar desde el componente padre.
   */
  @Input() comania: string = "laureano.SL";

  /**
   * Lista de enlaces para el menú de navegación del footer.
   * Cada enlace es un arreglo con dos strings: [texto, ruta].
   * Se puede modificar desde el componente padre.
   */
  @Input() enlaces: string[][] = [
    ["Privacidad", "/public/privacity"],
    ["Contacto y soporte", "/public/contact"],
    ["Login", "/login"]
  ];

  /**
   * Constructor vacío, sin lógica adicional.
   */
  constructor() {}
}
