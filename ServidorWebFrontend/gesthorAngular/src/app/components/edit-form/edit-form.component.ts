/**
 * Componente Angular: EditFormComponent
 * -------------------------------------
 * Este componente permite la edición inline de un valor de texto asociado a una etiqueta.
 * Presenta dos estados: modo visualización y modo edición.
 *
 * Selector: 'app-edit-form'
 *
 * Funcionalidad principal:
 * - Mostrar un valor junto con su etiqueta en modo visualización.
 * - Permitir activar el modo edición para modificar el valor.
 * - Confirmar o cancelar la edición.
 * - Emitir un evento con el campo y el nuevo valor para que sea manejado externamente.
 * - Controlar la deshabilitación del botón "Editar".
 *
 * Entradas (@Input):
 * - label: Etiqueta que se muestra junto al valor.
 * - value: Valor actual que se muestra y puede ser editado.
 * - field: Nombre del campo asociado para identificar el dato en el evento.
 * - disabled: Booleano que indica si el botón "Editar" está deshabilitado (por defecto false).
 *
 * Salidas (@Output):
 * - save: Evento emitido al confirmar la edición, con un objeto que contiene el campo y el nuevo valor.
 *
 * Propiedades internas:
 * - editando: Controla si el formulario está en modo edición o no.
 * - valorTemporal: Modelo ligado al input para editar el valor temporalmente antes de guardar.
 *
 * Métodos:
 * - activarEdicion(): Cambia al modo edición y copia el valor actual al valor temporal editable.
 * - cancelar(): Sale del modo edición sin guardar cambios.
 * - guardar(): Emite el evento 'save' con el campo y el valor actualizado, y vuelve al modo visualización.
 *
 * Plantilla HTML:
 * - Muestra el valor y etiqueta cuando no está en edición.
 * - Botón "Editar" para activar el modo edición, deshabilitado si corresponde.
 * - En modo edición muestra un input para modificar el valor, junto con botones "Confirmar" y "Cancelar".
 */

import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-edit-form',
  standalone: false,
  template: `
  <li class="list-group-item bg-dark text-light border-secondary">
    <div class="d-flex justify-content-between align-items-center" *ngIf="!editando; else editTemplate">
      <span>{{ label }}: {{ value }}</span>
      <button class="btn btn-outline-light btn-sm" (click)="activarEdicion()" [disabled]="disabled">Editar</button>
    </div>

    <ng-template #editTemplate>
      <div class="d-flex flex-column flex-md-row align-items-center gap-2 mt-2 row" style="padding-right: 5%; padding-left: 5%;">
        <span class="col-12">{{ label }}:</span>
        <input [(ngModel)]="valorTemporal" class="form-control col-10" />
        <div class="d-flex gap-2">
          <button class="btn btn-success btn-sm" (click)="guardar()">Confirmar</button>
          <button class="btn btn-secondary btn-sm" (click)="cancelar()">Cancelar</button>
        </div>
      </div>
    </ng-template>
  </li>
  `
})
export class EditFormComponent {
  /** Etiqueta descriptiva del campo */
  @Input() label = '';

  /** Valor actual que se muestra y puede editarse */
  @Input() value = '';

  /** Nombre del campo para identificar el dato al emitir el evento */
  @Input() field = '';

  /** Controla si el botón "Editar" está deshabilitado */
  @Input() disabled = false;

  /** Evento emitido al confirmar edición con el campo y el nuevo valor */
  @Output() save = new EventEmitter<{ field: string; value: string }>();

  /** Indica si está en modo edición */
  editando = false;

  /** Valor temporal para editar antes de confirmar */
  valorTemporal = '';

  /**
   * Activa el modo edición y copia el valor actual a valor temporal editable.
   */
  activarEdicion() {
    this.editando = true;
    this.valorTemporal = this.value;
  }

  /**
   * Cancela la edición y vuelve a modo visualización sin guardar cambios.
   */
  cancelar() {
    this.editando = false;
  }

  /**
   * Emite el evento 'save' con el campo y el valor actualizado, y desactiva el modo edición.
   */
  guardar() {
    this.save.emit({ field: this.field, value: this.valorTemporal });
    this.editando = false;
  }
}
