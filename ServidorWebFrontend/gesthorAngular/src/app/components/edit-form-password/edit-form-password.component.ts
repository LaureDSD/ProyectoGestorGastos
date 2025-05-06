import { Component, Input, Output, EventEmitter } from '@angular/core';


@Component({
  selector: 'app-edit-form-password',
  standalone: false,
  templateUrl: './edit-form-password.component.html',
  styleUrl: './edit-form-password.component.css'
})
export class EditFormPasswordComponent {
  @Input() label = 'Contraseña';
  @Input() disabled = false;
  @Output() save = new EventEmitter<{ current: string; newPassword: string }>();

  editando = false;
  actual = '';
  nueva = '';
  confirmar = '';
  error = '';

  activarEdicion() {
    this.editando = true;
    this.actual = '';
    this.nueva = '';
    this.confirmar = '';
    this.error = '';
  }

  cancelar() {
    this.editando = false;
    this.error = '';
  }

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
