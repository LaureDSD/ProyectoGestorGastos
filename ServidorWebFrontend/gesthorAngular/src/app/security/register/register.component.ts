import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  registerForm: FormGroup;
  isLoading = false;
  error = '';
  passwordVisible = false;
  passwordVisibleConfirm = false;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8), this.passwordStrengthValidator.bind(this)]],
      confirmPassword: ['', Validators.required]
    }, { validators: this.passwordMatchValidator });
  }

  togglePasswordVisibility() {
  this.passwordVisible = !this.passwordVisible;
}

togglePasswordVisibilityConfirm() {
  this.passwordVisibleConfirm = !this.passwordVisibleConfirm;
}

  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { mismatch: true };
  }

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

  get passwordsMismatch() {
    return this.registerForm.errors?.['mismatch'] && this.registerForm.touched;
  }

  get weakPassword() {
    const passwordControl = this.registerForm.get('password');
    return passwordControl?.errors?.['weakPassword'] && passwordControl.touched;
  }

  onSubmit() {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }
    this.isLoading = true;
    const { username, name, email, password } = this.registerForm.value;
    this.auth.register(username, name, email, password).subscribe({
      next: res => {
        localStorage.setItem('token', res.token);
        this.router.navigate(['/protected/home']);
      },
      error: err => {
        this.error = 'Registration failed.';
        this.isLoading = false;
      }
    });
  }
}
