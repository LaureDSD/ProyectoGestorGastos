import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient) { }

  login(username: string, password: string) {
    return this.http.post<{ token: string }>(`${this.baseUrl}/authenticate`, { user: username, password });
  }

  register(username: string, name: string, email: string, password: string) {
    return this.http.post<{ token: string }>(`${this.baseUrl}/signup`, { username, name, email, password });
  }

  loginWithOAuth2(provider: 'google' | 'github') {
    window.location.href = `${environment.apiUrl}/oauth2/authorization/${provider}`;
  }
}