import { Component } from '@angular/core';
import { ApiserviceService } from '../../../services/apiservice.service';

@Component({
  selector: 'app-form',
  standalone: false,
  templateUrl: './form.component.html',
  styleUrl: './form.component.css'
})
export class FormComponent {

  contacto: Contacto = { nombre: '', correo: '', asunto: '', mensaje: '' };
  enviado = false;
  error = '';

  constructor(private apiService : ApiserviceService) {}

  enviarFormulario() {
    this.apiService.enviarMensaje(this.contacto).subscribe({
      next: () => {
        this.enviado = true;
        this.contacto = { nombre: '', correo: '', asunto: '', mensaje: '' };
      },
      error: (err) => {
        this.error = 'Error al enviar el mensaje. Intenta más tarde.';
        console.error(err);
      }
    });
  }
  
}
export interface Contacto {
  nombre: string;
  correo: string;
  asunto: string;
  mensaje: string;
}
