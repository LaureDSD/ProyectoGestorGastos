import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

/**
 * Componente ForgotPasswordComponent
 *
 * Este componente proporciona la funcionalidad para que el usuario pueda solicitar
 * el restablecimiento de su contraseña mediante su correo electrónico.
 * Gestiona un formulario reactivo con validación de email y muestra mensajes
 * de éxito o error según la respuesta del servicio de autenticación.
 */
@Component({
  selector: 'app-forgot-password',
  standalone: false, // No es un componente standalone, depende de un módulo Angular
  templateUrl: './forgot-password.component.html', // Plantilla HTML asociada
  styleUrls: ['./forgot-password.component.css']   // Estilos CSS asociados
})
export class ForgotPasswordComponent implements OnInit {

  // FormGroup que representa el formulario para solicitar restablecimiento de contraseña
  resetPasswordForm: FormGroup;

  // Indica si la solicitud está en proceso para mostrar carga o deshabilitar UI
  isLoading = false;

  // Mensaje de error para mostrar en caso de fallo en la solicitud
  error = '';

  // Mensaje de éxito para mostrar cuando el enlace se envía correctamente
  success = '';

  /**
   * Constructor del componente
   * @param fb FormBuilder para construir el formulario reactivo
   * @param auth Servicio de autenticación para solicitar restablecimiento de contraseña
   * @param router Servicio Router para navegación (no usado explícitamente en este componente)
   */
  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    // Inicialización del formulario con control email, requerido y validación de formato email
    this.resetPasswordForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  /**
   * Método del ciclo de vida OnInit
   * Actualmente no realiza acciones adicionales al iniciar el componente
   */
  ngOnInit(): void {}

  /**
   * Getter para acceder fácilmente al control email del formulario
   */
  get email() {
    return this.resetPasswordForm.get('email');
  }

  /**
   * Método que se ejecuta al enviar el formulario de restablecimiento de contraseña
   * - Valida que el formulario sea válido
   * - Limpia mensajes previos y activa indicador de carga
   * - Llama al servicio auth.forgotPassword con el email ingresado
   * - En caso de éxito, muestra mensaje de éxito y resetea el formulario
   * - En caso de error, muestra mensaje de error y registra el error en consola
   */
  onSubmit() {
    if (this.resetPasswordForm.invalid) return; // No hace nada si el formulario es inválido

    this.isLoading = true;  // Activa indicador de carga
    this.error = '';        // Limpia mensaje de error previo
    this.success = '';      // Limpia mensaje de éxito previo

    // Extrae el email del formulario
    const { email } = this.resetPasswordForm.value;

    // Llama al método para solicitar restablecimiento de contraseña
    this.auth.forgotPassword(email).subscribe({
      next: () => {
        // Mensaje de éxito al enviar el enlace de restablecimiento
        this.success = 'Hemos enviado un enlace para restablecer tu contraseña a tu correo electrónico.';
        this.isLoading = false;  // Desactiva indicador de carga
        this.resetPasswordForm.reset(); // Resetea el formulario para limpiar campos
      },
      error: err => {
        // Mensaje de error en caso de fallo en la solicitud
        this.error = 'Hubo un error al enviar el enlace. Intenta nuevamente más tarde.';
        console.error(err);     // Log del error para desarrollo
        this.isLoading = false; // Desactiva indicador de carga
      }
    });
  }

}
