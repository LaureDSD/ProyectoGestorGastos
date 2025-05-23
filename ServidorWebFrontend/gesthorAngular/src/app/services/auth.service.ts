import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../environments/environment';
import { Observable } from 'rxjs';

/**
 * Servicio Angular encargado de gestionar la autenticación y gestión del usuario.
 * Proporciona métodos para login, registro, autenticación vía OAuth2, manejo de token,
 * recuperación de contraseña, y verificación de roles y estado de autenticación.
 */
@Injectable({ providedIn: 'root' })
export class AuthService {

  // URL base para los endpoints de autenticación en la API
  private baseUrl = `${environment.apiUrl}/auth`;

  /**
   * Objeto que simula o almacena la información del usuario actualmente logueado.
   * Contiene datos como perfil, nombre, username, rol, datos de contacto, categorías,
   * métodos de pago y logs de actividad.
   */
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
  };

  /**
   * Constructor que inyecta HttpClient para realizar peticiones HTTP.
   * @param http Cliente HTTP para comunicación con la API.
   */
  constructor(private http: HttpClient) { }

  /**
   * Realiza la autenticación del usuario enviando username y password al backend.
   * Limpia el usuario actual antes de hacer la petición.
   * @param username Nombre de usuario para login.
   * @param password Contraseña para login.
   * @returns Observable con el token JWT en caso de éxito.
   */
  login(username: string, password: string) {
    this.user = ""; // Resetea el usuario antes de login
    return this.http.post<{ token: string }>(`${this.baseUrl}/authenticate`, { user: username, password });
  }

  /**
   * Registra un nuevo usuario enviando los datos al backend.
   * Limpia el usuario actual antes de hacer la petición.
   * @param username Nombre de usuario para registro.
   * @param name Nombre completo del usuario.
   * @param email Correo electrónico.
   * @param password Contraseña para la cuenta.
   * @returns Observable con el token JWT en caso de éxito.
   */
  register(username: string, name: string, email: string, password: string) {
    this.user = ""; // Resetea el usuario antes de registro
    return this.http.post<{ token: string }>(`${this.baseUrl}/signup`, { username, name, email, password });
  }

  /**
   * Inicia el flujo de autenticación OAuth2 redirigiendo a la URL de autorización del proveedor.
   * @param provider Proveedor OAuth2 ('google' o 'github').
   */
  loginWithOAuth2(provider: 'google' | 'github') {
    window.location.href = `${environment.apiUrl}/oauth2/authorization/${provider}`;
  }

  /**
   * Obtiene el token JWT almacenado en localStorage.
   * @returns Token JWT como string o null si no existe.
   */
  getToken() {
    return localStorage.getItem('token');
  }

  /**
   * Solicita el inicio del proceso de recuperación de contraseña enviando el email.
   * @param email Correo electrónico del usuario para recuperación.
   * @returns Observable con la respuesta del backend.
   */
  forgotPassword(email: string): Observable<any> {
    return this.http.post(`${environment.apiUrl}/public/forgot-password`, { email });
  }

  /**
   * Cierra la sesión eliminando el token del localStorage y limpiando el objeto usuario.
   */
  logout(): void {
    localStorage.removeItem('token');
    this.user = "";
  }

  /**
   * Verifica si el usuario actual tiene rol de administrador.
   * @returns true si el rol es 'ADMIN', false en otro caso.
   */
  isAdmin(): boolean {
    return this.user.role == "ADMIN";
  }

  /**
   * Obtiene los datos del usuario actualmente autenticado consultando la API.
   * @returns Observable con los datos del usuario.
   */
  getLoadUser(): Observable<any>{
    return this.http.get(`${environment.apiUrl}/api/user/me`);
  }

  /**
   * Comprueba si hay un token almacenado para determinar si el usuario está autenticado.
   * @returns true si existe un token, false si no.
   */
  isAuthenticated(): boolean {
    const token = this.getToken();
    return !!token;
  }

}
