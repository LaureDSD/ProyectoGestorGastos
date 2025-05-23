/**
 * Componente Angular ToolGroupComponent
 *
 * Este componente representa un grupo de herramientas que se pueden mostrar en la interfaz.
 * Cada herramienta contiene un nombre, ícono, descripción, acción y ruta de navegación.
 *
 * Funcionalidad principal:
 * - Recibe un arreglo de herramientas mediante la propiedad @Input() 'herramientas'.
 * - Muestra las herramientas con su información visual y permite navegar a la ruta especificada.
 * - Utiliza el servicio Router para realizar la navegación interna en la aplicación.
 */

import { Router } from '@angular/router';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-tool-group',                // Selector HTML para usar este componente
  standalone: false,                         // No es standalone, declarado en módulos
  template: `
  <div class="row row-cols-1 row-cols-md-2 g-3">
    <div *ngFor="let herramienta of herramientas" class="col">

      <div class="card h-100 shadow-sm bg-dark text-white d-flex flex-row align-items-center p-3">
        <img [src]="herramienta.icono" alt="{{ herramienta.nombre }}"
             class="me-3" style="width: 60px; height: 60px;" />

        <div class="flex-grow-1">
          <h5 class="mb-1">{{ herramienta.nombre }}</h5>
          <small class="text-secondary d-none d-md-inline">{{ herramienta.descripcion }}</small>
        </div>

        <button class="btn btn-outline-light ms-3"
                (click)="navegar(herramienta.ruta)">
          {{ herramienta.accion }}
        </button>
      </div>

    </div>
  </div>

  `})
export class ToolGroupComponent {

  /**
   * Propiedad de entrada que recibe un arreglo de objetos con información de herramientas
   * Cada herramienta debe tener las siguientes propiedades:
   * - nombre: string con el nombre visible de la herramienta
   * - icono: string con la ruta al icono representativo de la herramienta
   * - descripcion: string con una breve descripción de la herramienta
   * - accion: string que describe la acción que realiza la herramienta (texto visual)
   * - ruta: string con la ruta de navegación a la que se dirigirá al activarla
   */
  @Input() herramientas = [
    {
      nombre: 'Nombre',
      icono: '/icon/icons8-pencil.gif',
      descripcion: 'Descripcion',
      accion: 'Cargar',
      ruta: '/ruta'
    }
  ];

  /**
   * Constructor del componente
   *
   * @param router Servicio Router para controlar la navegación programática
   */
  constructor(private router: Router) {}

  /**
   * Método que navega a una ruta interna de la aplicación
   *
   * @param ruta Ruta destino como string (debe ser una ruta válida definida en el Router)
   */
  navegar(ruta: string) {
    this.router.navigate([ruta]);
  }
}
