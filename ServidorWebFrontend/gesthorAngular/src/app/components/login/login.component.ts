import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: false,             
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  isLoading = false;
  error = '';

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {}

  onSubmit() {
    if (this.loginForm.invalid) return;
    this.isLoading = true;
    const { username, password } = this.loginForm.value;
    this.auth.login(username, password).subscribe({
      next: res => {
        localStorage.setItem('token', res.token);
        console.log(res.token)
        this.router.navigate(['/home']);
      },
      error: err => {
        this.error = 'Login failed';
        this.isLoading = false;
      }
    });
  }

  passwordVisible: boolean = false;

  togglePasswordVisibility() {
  this.passwordVisible = !this.passwordVisible;
}


  loginGoogle() { this.auth.loginWithOAuth2('google'); }
  loginGitHub() { this.auth.loginWithOAuth2('github'); }
}