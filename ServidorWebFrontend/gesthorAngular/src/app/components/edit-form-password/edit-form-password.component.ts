/**
 * Componente Angular: EditFormPasswordComponent
 * ------------------------------------------------
 * Este componente permite editar y cambiar una contraseña mediante un formulario.
 * Presenta dos estados: vista de solo lectura y modo edición.
 *
 * Selector: 'app-edit-form-password'
 *
 * Funcionalidad principal:
 * - Mostrar la contraseña actual como texto (solo etiqueta).
 * - Permitir activar el modo edición para ingresar la contraseña actual, nueva y confirmarla.
 * - Validar que todos los campos estén completos.
 * - Validar que la nueva contraseña y la confirmación coincidan.
 * - Validar longitud mínima de la nueva contraseña.
 * - Emitir un evento con los datos de las contraseñas para ser manejado externamente.
 * - Permitir cancelar la edición, limpiando datos y errores.
 * - Controlar deshabilitación del botón para activar la edición.
 *
 * Entradas (@Input):
 * - label: Etiqueta que se muestra en la vista previa (por defecto 'Contraseña').
 * - disabled: Booleano que indica si el botón de cambio está deshabilitado (por defecto false).
 *
 * Salidas (@Output):
 * - save: Evento emitido cuando se confirma el cambio de contraseña.
 *         Envía un objeto con la contraseña actual y la nueva contraseña.
 *
 * Propiedades internas:
 * - editando: Controla si el formulario está en modo edición o no.
 * - actual: Modelo ligado a la contraseña actual ingresada.
 * - nueva: Modelo ligado a la nueva contraseña ingresada.
 * - confirmar: Modelo ligado a la confirmación de la nueva contraseña.
 * - error: Mensaje de error para mostrar al usuario si la validación falla.
 *
 * Métodos:
 * - activarEdicion(): Cambia el modo a edición y limpia los campos y errores.
 * - cancelar(): Sale del modo edición y limpia errores.
 * - guardar(): Valida los campos, muestra errores si aplica y emite el evento save con las contraseñas.
 *
 * Plantilla HTML:
 * - Muestra un botón "Cambiar" para activar la edición (deshabilitado según propiedad disabled).
 * - En modo edición muestra inputs para actual, nueva y confirmar la contraseña.
 * - Botones para Confirmar y Cancelar la edición.
 * - Mensaje de error visible solo si existe algún error.
 */

import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-edit-form-password',
  standalone: false,
  template: `
  <li class="list-group-item bg-dark text-light border-secondary">
    <div class="d-flex justify-content-between align-items-center" *ngIf="!editando; else editPassword">
      <span>{{ label }}</span>
      <button class="btn btn-outline-light btn-sm" (click)="activarEdicion()" [disabled]="disabled">Cambiar</button>
    </div>

    <ng-template #editPassword>
      <div class="d-flex flex-column gap-2 mt-2 px-3">
        <div>
          <label class="form-label">Contraseña actual:</label>
          <input type="password" [(ngModel)]="actual" class="form-control" />
        </div>
        <div>
          <label class="form-label">Nueva contraseña:</label>
          <input type="password" [(ngModel)]="nueva" class="form-control" />
        </div>
        <div>
          <label class="form-label">Confirmar nueva:</label>
          <input type="password" [(ngModel)]="confirmar" class="form-control" />
        </div>

        <div class="d-flex gap-2 mt-2">
          <button class="btn btn-success btn-sm" (click)="guardar()">Confirmar</button>
          <button class="btn btn-secondary btn-sm" (click)="cancelar()">Cancelar</button>
        </div>

        <small class="text-danger" *ngIf="error">{{ error }}</small>
      </div>
    </ng-template>
  </li>
  `
})
export class EditFormPasswordComponent {
  /** Etiqueta visible cuando no se está editando */
  @Input() label = 'Contraseña';

  /** Controla si el botón 'Cambiar' está deshabilitado */
  @Input() disabled = false;

  /** Evento emitido al confirmar el cambio de contraseña */
  @Output() save = new EventEmitter<{ current: string; newPassword: string }>();

  /** Indica si el formulario está en modo edición */
  editando = false;

  /** Modelo para la contraseña actual */
  actual = '';

  /** Modelo para la nueva contraseña */
  nueva = '';

  /** Modelo para la confirmación de la nueva contraseña */
  confirmar = '';

  /** Mensaje de error para mostrar validaciones al usuario */
  error = '';

  /**
   * Activa el modo edición y limpia los campos y errores previos.
   */
  activarEdicion() {
    this.editando = true;
    this.actual = '';
    this.nueva = '';
    this.confirmar = '';
    this.error = '';
  }

  /**
   * Cancela la edición, vuelve al modo solo lectura y limpia errores.
   */
  cancelar() {
    this.editando = false;
    this.error = '';
  }

  /**
   * Valida los campos y, si son correctos, emite el evento 'save' con las contraseñas.
   * Muestra mensajes de error específicos si alguna validación falla.
   */
  guardar() {
    if (!this.actual || !this.nueva || !this.confirmar) {
      this.error = 'Completa todos los campos.';
      return;
    }

    if (this.nueva !== this.confirmar) {
      this.error = 'Las nuevas contraseñas no coinciden.';
      return;
    }

    if (this.nueva.length < 4) {
      this.error = 'La nueva contraseña debe tener al menos 8 caracteres.';
      return;
    }

    this.save.emit({ current: this.actual, newPassword: this.nueva });
    this.editando = false;
  }
}
