import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../environments/environment';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = `${environment.apiUrl}/auth`;
  router: any;

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

  getToken() {
    return localStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getCurrentUser() {
    return this.http.get(`${environment.apiUrl}/api/user/me`);
  }

  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  forgotPassword(email: string): Observable<any> {
    return this.http.post('/api/auth/forgot-password', { email });
  }

}
