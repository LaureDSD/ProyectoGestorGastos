import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';

/**
 * Componente LoginComponent
 *
 * Este componente maneja el formulario de inicio de sesión de la aplicación.
 * Provee la funcionalidad para que el usuario ingrese su nombre de usuario y contraseña,
 * realice la autenticación y navegue a la ruta protegida tras un inicio exitoso.
 * También permite iniciar sesión mediante OAuth2 con Google o GitHub.
 */
@Component({
  selector: 'app-login',
  standalone: false, // No es un componente standalone, depende de un módulo Angular
  templateUrl: './login.component.html', // Archivo HTML asociado con la plantilla del login
  styleUrl: './login.component.css'      // Archivo CSS asociado con los estilos del login
})
export class LoginComponent implements OnInit {

  // FormGroup que representa el formulario reactivo para el login
  loginForm: FormGroup;

  // Variable booleana para indicar si el login está en proceso (mostrar spinner, deshabilitar botones, etc)
  isLoading = false;

  // Mensaje de error para mostrar en caso de credenciales inválidas
  error = '';

  // Variable que controla la visibilidad del campo password (mostrar/ocultar)
  passwordVisible: boolean = false;

  /**
   * Constructor del componente
   * @param fb FormBuilder para construir formularios reactivos
   * @param auth Servicio de autenticación para llamar al método login
   * @param router Servicio de Angular Router para navegación entre rutas
   */
  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    // Inicialización del formulario con controles para username y password, ambos requeridos
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  /**
   * Método del ciclo de vida OnInit
   * Actualmente no realiza ninguna acción al inicializar el componente
   */
  ngOnInit(): void {}

  /**
   * Método que se ejecuta al enviar el formulario de login
   * - Valida que el formulario sea válido
   * - Marca el estado de carga para bloquear la UI
   * - Extrae los valores username y password del formulario
   * - Llama al servicio de autenticación para hacer login
   * - En caso de éxito, almacena el token en localStorage y navega a la ruta protegida
   * - En caso de error, muestra mensaje de error y detiene el estado de carga
   */
  onSubmit() {
    if (this.loginForm.invalid) return; // Si el formulario no es válido, no hacer nada

    this.isLoading = true; // Indicar que el login está en proceso

    // Desestructurar valores del formulario
    const { username, password } = this.loginForm.value;

    // Llamar al método login del servicio de autenticación
    this.auth.login(username, password).subscribe({
      next: res => {
        // Guardar token recibido en localStorage para mantener sesión
        localStorage.setItem('token', res.token);
        // Navegar a la ruta protegida /protected/home tras login exitoso
        this.router.navigate(['/protected/home']);
      },
      error: err => {
        // Mostrar mensaje de error en caso de credenciales inválidas
        this.error = 'Credenciales no validas';
        // Desactivar indicador de carga
        this.isLoading = false;
      }
    });
  }

  /**
   * Método para alternar la visibilidad del campo de contraseña
   * Cambia el estado booleano passwordVisible que puede usarse para mostrar u ocultar el input password
   */
  togglePasswordVisibility() {
    this.passwordVisible = !this.passwordVisible;
  }

  /**
   * Métodos para iniciar sesión mediante OAuth2 con proveedores externos
   * Delegan la llamada al servicio de autenticación con el proveedor correspondiente
   */
  loginGoogle() {
    this.auth.loginWithOAuth2('google');
  }

  loginGitHub() {
    this.auth.loginWithOAuth2('github');
  }
}
