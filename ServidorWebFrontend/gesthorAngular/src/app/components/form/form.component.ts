/**
 * FormComponent
 *
 * Componente Angular que representa un formulario de contacto para que el usuario
 * pueda enviar un mensaje con su nombre, correo, asunto y mensaje.
 *
 * Funcionalidades:
 * - Formulario reactivo con validación básica (campos requeridos).
 * - Envío de datos mediante servicio API.
 * - Indicadores visuales de éxito o error al enviar el formulario.
 *
 * Uso:
 * <app-form></app-form>
 *
 * Este componente utiliza:
 * - ApiserviceService para el envío de mensajes al backend.
 * - Modelo Contacto para tipar los datos del formulario.
 */

import { Component } from '@angular/core';
import { ApiserviceService } from '../../services/apiservice.service';
import { Contacto } from '../../models/models/models.component';

@Component({
  selector: 'app-form',
  standalone: false,
  template: `
  <!-- Contenedor principal centrado con un ancho máximo para el formulario -->
  <div class="container">
    <form
      id="formContacto"
      class="mx-auto"
      style="max-width: 600px;"
      (ngSubmit)="enviarFormulario()"
      #form="ngForm"
    >
      <!-- Campo Nombre completo -->
      <div class="mb-3">
        <label for="nombre" class="form-label">Nombre completo</label>
        <input
          type="text"
          class="form-control"
          id="nombre"
          name="nombre"
          [(ngModel)]="contacto.nombre"
          placeholder="Introduce tu nombre"
          required
        />
      </div>

      <!-- Campo Correo electrónico -->
      <div class="mb-3">
        <label for="correo" class="form-label">Correo electrónico</label>
        <input
          type="email"
          class="form-control"
          id="correo"
          name="correo"
          [(ngModel)]="contacto.correo"
          placeholder="tucorreo@example.com"
          required
        />
      </div>

      <!-- Campo Asunto -->
      <div class="mb-3">
        <label for="asunto" class="form-label">Asunto</label>
        <input
          type="text"
          class="form-control"
          id="asunto"
          name="asunto"
          [(ngModel)]="contacto.asunto"
          placeholder="Motivo de tu consulta"
          required
        />
      </div>

      <!-- Campo Mensaje -->
      <div class="mb-3">
        <label for="mensaje" class="form-label">Mensaje</label>
        <textarea
          class="form-control"
          id="mensaje"
          name="mensaje"
          [(ngModel)]="contacto.mensaje"
          rows="5"
          placeholder="Cuéntanos en qué podemos ayudarte..."
          required
        ></textarea>
      </div>

      <!-- Botón para enviar el formulario -->
      <button type="submit" class="btn btn-outline-light w-100">Enviar mensaje</button>

      <!-- Mensaje de éxito al enviar -->
      <div *ngIf="enviado" class="alert alert-success mt-3">
        ¡Mensaje enviado correctamente!
      </div>

      <!-- Mensaje de error si ocurre algún problema -->
      <div *ngIf="error" class="alert alert-danger mt-3">
        {{ error }}
      </div>
    </form>
  </div>
  `
})
export class FormComponent {
  /**
   * Modelo que contiene los datos del formulario de contacto.
   * Inicializado con valores vacíos.
   */
  contacto: Contacto = { nombre: '', correo: '', asunto: '', mensaje: '' };

  /**
   * Bandera booleana que indica si el mensaje fue enviado correctamente.
   */
  enviado = false;

  /**
   * Mensaje de error en caso de fallo en el envío.
   */
  error = '';

  /**
   * Constructor que inyecta el servicio API para enviar mensajes.
   * @param apiService Servicio que maneja la comunicación con el backend.
   */
  constructor(private apiService: ApiserviceService) {}

  /**
   * Método que se ejecuta al enviar el formulario.
   * Llama al servicio API para enviar el mensaje.
   * Muestra mensajes de éxito o error según el resultado.
   */
  enviarFormulario() {
    this.apiService.enviarMensaje(this.contacto).subscribe({
      next: () => {
        // Envío exitoso: se resetea el formulario y se muestra mensaje
        this.enviado = true;
        this.error = '';
        this.contacto = { nombre: '', correo: '', asunto: '', mensaje: '' };
      },
      error: (err) => {
        // Error en envío: se muestra mensaje de error y se registra en consola
        this.error = 'Error al enviar el mensaje. Intenta más tarde.';
        this.enviado = false;
        console.error(err);
      }
    });
  }
}
