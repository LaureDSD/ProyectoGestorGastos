import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-forgot-password',
  standalone: false,
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {
  resetPasswordForm: FormGroup;
  isLoading = false;
  error = '';
  success = '';

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    this.resetPasswordForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  ngOnInit(): void {}

  get email() {
    return this.resetPasswordForm.get('email');
  }

  onSubmit() {
  if (this.resetPasswordForm.invalid) return;

  this.isLoading = true;
  this.error = '';
  this.success = '';

  const { email } = this.resetPasswordForm.value;

  this.auth.forgotPassword(email).subscribe({
    next: () => {
      this.success = 'Hemos enviado un enlace para restablecer tu contraseña a tu correo electrónico.';
      this.isLoading = false;
      this.resetPasswordForm.reset(); 
    },
    error: err => {
      this.error = 'Hubo un error al enviar el enlace. Intenta nuevamente más tarde.';
      console.error(err);
      this.isLoading = false;
    }
  });
}

}
