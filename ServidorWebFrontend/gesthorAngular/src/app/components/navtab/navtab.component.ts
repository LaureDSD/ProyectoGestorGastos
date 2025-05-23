/**
 * Componente Angular NavtabComponent
 *
 * Este componente renderiza una barra de navegación con pestañas (tabs) que actúan como filtros.
 * Permite seleccionar un filtro y notificar al componente padre mediante un evento.
 *
 * Funcionalidades principales:
 * - Muestra una pestaña fija llamada "Todo" y una pestaña por cada filtro recibido.
 * - Emite un evento con el nombre del filtro seleccionado cuando se hace clic en alguna pestaña.
 * - Utiliza estilos Bootstrap para presentación oscura y estilo de pestañas.
 *
 * Propiedades:
 * @Input() filtros: any[] - Arreglo de filtros donde cada filtro es un arreglo cuyo primer elemento es el nombre del filtro.
 * @Output() buttonClicked: EventEmitter<string> - Evento que emite el nombre del filtro seleccionado.
 *
 * Métodos:
 * - onButtonClick(buttonName: string): emite el nombre del filtro seleccionado mediante el EventEmitter.
 *
 * Uso en plantilla:
 * - La pestaña "Todo" siempre está visible.
 * - Se genera dinámicamente una pestaña por cada filtro recibido, mostrando su nombre capitalizado.
 */

import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navtab',
  standalone: false,
  template: `
    <ul class="nav bg-dark nav-tabs pt-2 text-decoration-none text-reset" data-bs-theme="dark">

      <!-- Pestaña fija 'Todo' -->
      <li class="nav-item" (click)="onButtonClick('todo')">
        <a class="nav-link" [routerLinkActive]="'active'" aria-current="page" href="#">Todo</a>
      </li>

      <!-- Pestañas dinámicas según filtros -->
      <li *ngFor="let f of filtros" class="nav-item" (click)="onButtonClick(f[0])">
        <a class="nav-link" href="#">{{ f[0] | titlecase }}</a>
      </li>

    </ul>
  `
})
export class NavtabComponent {

  /** Arreglo de filtros para crear pestañas; cada filtro debe ser un arreglo con el nombre en la posición 0 */
  @Input() filtros: any[] = [];

  /** Evento emitido con el nombre del filtro seleccionado */
  @Output() buttonClicked = new EventEmitter<string>();

  /**
   * Constructor con inyección del servicio Router (no usado actualmente en el código)
   */
  constructor(private router: Router) {}

  /**
   * Método invocado al hacer clic en una pestaña, emite el nombre del filtro seleccionado.
   * @param buttonName Nombre del filtro (string) emitido al componente padre.
   */
  onButtonClick(buttonName: string) {
    this.buttonClicked.emit(buttonName);
  }
}
