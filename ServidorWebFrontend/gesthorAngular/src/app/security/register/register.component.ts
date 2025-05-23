import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

/**
 * Componente Angular para el registro de nuevos usuarios.
 * Contiene un formulario reactivo con validación personalizada para
 * verificar fortaleza de contraseña y confirmación de contraseña.
 */
@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  /** FormGroup reactivo que contiene los controles del formulario */
  registerForm: FormGroup;

  /** Indicador de carga para mostrar estado mientras se procesa la solicitud */
  isLoading = false;

  /** Mensaje de error para mostrar mensajes de validación o del backend */
  error = '';

  /** Control para mostrar u ocultar la contraseña */
  passwordVisible = false;

  /** Control para mostrar u ocultar la confirmación de contraseña */
  passwordVisibleConfirm = false;

  /**
   * Constructor que inyecta FormBuilder, AuthService y Router.
   * Inicializa el formulario con validadores sincronizados y personalizados.
   * @param fb FormBuilder para crear el FormGroup.
   * @param auth Servicio de autenticación para registro.
   * @param router Servicio Router para navegación programática.
   */
  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    // Definición del formulario reactivo con validadores
    this.registerForm = this.fb.group({
      username: ['', Validators.required],                  // Nombre de usuario obligatorio
      name: ['', Validators.required],                      // Nombre obligatorio
      email: ['', [Validators.required, Validators.email]],// Email obligatorio y con formato válido
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        this.passwordStrengthValidator.bind(this)          // Validador personalizado de fortaleza de contraseña
      ]],
      confirmPassword: ['', Validators.required]            // Confirmación de contraseña obligatoria
    }, { validators: this.passwordMatchValidator });         // Validador personalizado para confirmar que las contraseñas coincidan
  }

  /**
   * Alterna la visibilidad del campo de contraseña principal.
   */
  togglePasswordVisibility() {
    this.passwordVisible = !this.passwordVisible;
  }

  /**
   * Alterna la visibilidad del campo de confirmación de contraseña.
   */
  togglePasswordVisibilityConfirm() {
    this.passwordVisibleConfirm = !this.passwordVisibleConfirm;
  }

  /**
   * Validador personalizado para verificar que el campo 'password' y 'confirmPassword' coincidan.
   * @param form FormGroup completo del formulario.
   * @returns null si coinciden, objeto con error si no.
   */
  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { mismatch: true };
  }

  /**
   * Validador personalizado que verifica la fortaleza de la contraseña.
   * Debe tener al menos 8 caracteres, mayúsculas, minúsculas, números y caracteres especiales.
   * Actualiza el mensaje de error en el componente para mostrar al usuario.
   * @param control Control del formulario correspondiente a la contraseña.
   * @returns null si es fuerte, objeto con error si es débil.
   */
  passwordStrengthValidator(control: any) {
    const value = control.value;
    if (!value) return null;

    const errors = [];

    if (value.length < 8) errors.push(' minimo 8 carcateres');
    if (!/[A-Z]/.test(value)) errors.push(' una letra mayuscula');
    if (!/[a-z]/.test(value)) errors.push(' una letra minuscula');
    if (!/[0-9]/.test(value)) errors.push(' un numero');
    if (!/[!@#$%^&*(),.?":{}|<>]/.test(value)) errors.push(' un caracter especial');

    if (errors.length > 0) {
      this.error = ' Debe contener: ' + errors.join(', ') + '.';
      return { weakPassword: true };
    }

    this.error = '';
    return null;
  }

  /**
   * Getter para determinar si hay error de coincidencia de contraseñas
   * y si el formulario ha sido tocado para mostrar el error.
   */
  get passwordsMismatch() {
    return this.registerForm.errors?.['mismatch'] && this.registerForm.touched;
  }

  /**
   * Getter para determinar si la contraseña es débil y el campo ha sido tocado.
   */
  get weakPassword() {
    const passwordControl = this.registerForm.get('password');
    return passwordControl?.errors?.['weakPassword'] && passwordControl.touched;
  }

  /**
   * Método que se ejecuta al enviar el formulario.
   * Valida el formulario, muestra errores si es inválido.
   * Si es válido, llama al servicio de autenticación para registrar el usuario.
   * En caso de éxito guarda token y navega a ruta protegida.
   * En caso de error muestra mensaje y detiene carga.
   */
  onSubmit() {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched(); // Marca todos como tocados para mostrar errores
      return;
    }
    this.isLoading = true;
    const { username, name, email, password } = this.registerForm.value;
    this.auth.register(username, name, email, password).subscribe({
      next: res => {
        localStorage.setItem('token', res.token);    // Guarda token en localStorage
        this.router.navigate(['/protected/home']);   // Navega a la página protegida
      },
      error: err => {
        this.error = 'Registration failed.';         // Mensaje de error genérico
        this.isLoading = false;
      }
    });
  }
}
