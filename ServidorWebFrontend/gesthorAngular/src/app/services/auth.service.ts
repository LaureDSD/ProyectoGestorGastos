import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../environments/environment';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private baseUrl = `${environment.apiUrl}/auth`;

  user: any = {
    profile: '/img/_fddad796-df42-4d46-9d79-5558fa9b7a1f.jpg',
    name: 'Juan Pérez',
    username: 'JP_GamerX',
    server: 'API-Gesthot-1',
    status: "true",
    role: 'USER',
    password: 'USER123',
    activity: '12:12:12',
    email: 'juan.perez@example.com',
    phone: '+34 600 123 456',
    address: 'Calle Ejemplo 123, Madrid',
    fv2: 'Activada',
    totalspents: "12",
    id: 'ID-12345-GT',
    payments: [
      { provider: 'Visa', ref: '**** 1234' },
      { provider: 'PayPal', ref: 'juan.perez@paypal.com' }
    ],
    categories: [
      { name: 'Casa', color: '#3343' },
      { name: 'Patio', color: '#7645' }
    ],
    logs: [
      { date: '12:12:12', status: 'true' },
      { date: '12:12:12', status: 'false' }
    ]
  };;

  constructor(private http: HttpClient) { }

  login(username: string, password: string) {
    this.user = ""
    return this.http.post<{ token: string }>(`${this.baseUrl}/authenticate`, { user: username, password });
  }

  register(username: string, name: string, email: string, password: string) {
    this.user = ""
    return this.http.post<{ token: string }>(`${this.baseUrl}/signup`, { username, name, email, password });
  }

  loginWithOAuth2(provider: 'google' | 'github') {
    window.location.href = `${environment.apiUrl}/oauth2/authorization/${provider}`;
  }

  getToken() {
    return localStorage.getItem('token');
  }

  //Falta
  forgotPassword(email: string): Observable<any> {
    return this.http.post('/api/auth/forgot-password', { email });
  }

  logout(): void {
    localStorage.removeItem('token');
    this.user = ""
  }

  isAdmin(): boolean {
    return this.user.role == "ADMIN";
  }

  getLoadUser(): Observable<any>{
    return this.http.get(`${environment.apiUrl}/api/user/me`);
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    return !!token;
  }

}
