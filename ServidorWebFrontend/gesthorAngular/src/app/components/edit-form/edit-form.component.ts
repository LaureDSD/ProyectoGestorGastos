import { Component, EventEmitter, Inject, Input, Output } from '@angular/core';

@Component({
  selector: 'app-edit-form',
  standalone: false,
  templateUrl: './edit-form.component.html',
  styleUrl: './edit-form.component.css'
})
export class EditFormComponent {

  @Input() label = '';
  @Input() value = '';
  @Input() field = '';
  @Input() disabled = false;

  @Output() save = new EventEmitter<{ field: string; value: string }>();

  editando = false;
  valorTemporal = '';

  activarEdicion() {
    this.editando = true;
    this.valorTemporal = this.value;
  }

  cancelar() {
    this.editando = false;
  }

  guardar() {
    this.save.emit({ field: this.field, value: this.valorTemporal });
    this.editando = false;
  }


}
