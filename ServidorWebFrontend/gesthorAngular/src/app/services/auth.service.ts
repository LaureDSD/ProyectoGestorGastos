import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../environments/environment';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private baseUrl = `${environment.apiUrl}/auth`;
  router: any;
  role: string = "";

  constructor(private http: HttpClient) { }

  login(username: string, password: string) {
    this.role = ""
    return this.http.post<{ token: string }>(`${this.baseUrl}/authenticate`, { user: username, password });
  }

  register(username: string, name: string, email: string, password: string) {
    this.role = ""
    return this.http.post<{ token: string }>(`${this.baseUrl}/signup`, { username, name, email, password });
  }

  loginWithOAuth2(provider: 'google' | 'github') {
    window.location.href = `${environment.apiUrl}/oauth2/authorization/${provider}`;
  }

  getToken() {
    return localStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    if(this.role==""){
      this.checkAdmin()
    }
    return !!this.getToken();
  }

  getCurrentUser() {
    return this.http.get(`${environment.apiUrl}/api/user/me`);
  }

  logout(): void {
    localStorage.removeItem('token');
    this.role = ""
    this.router.navigate(['/login']);
  }

  forgotPassword(email: string): Observable<any> {
    return this.http.post('/api/auth/forgot-password', { email });
  }

  private decodeToken(): any {
    const token = localStorage.getItem('token');
    if (!token) return null;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload;
    } catch (e) {
      return null;
    }
  }

  private checkAdmin(): void {
    const payload = this.decodeToken();
    this.role = payload?.rol;
  }

  isAdmin(): boolean {
    return this.role == "ADMIN";
  }

}
