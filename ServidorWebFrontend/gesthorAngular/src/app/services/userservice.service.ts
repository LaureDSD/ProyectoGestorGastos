import { AnySpentDto } from './../models/api-models/api-models.component';
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import {
  UserDto,
  LoginAttemptDto,
  ServerInfoDto
} from '../models/api-models/api-models.component';

@Injectable({
  providedIn: 'root'
})
export class UserserviceService {
  private readonly baseUrl = `${environment.apiUrl}/api/user/me`;

  constructor(private http: HttpClient) {}

  /** Obtiene los datos completos del usuario actual */
  getFullCurrentUser(): Observable<UserDto> {
    return this.http.get<UserDto>(this.baseUrl);
  }

  /** Sube o actualiza la foto de perfil del usuario actual */
  subirFotoPerfil(image: FormData): Observable<any> {
    return this.http.put<UserDto>(`${this.baseUrl}/uploadProfile`, image);
  }

  /** Actualiza los datos del usuario actual */
  actualizarUsuario(user: Partial<UserDto>): Observable<UserDto> {
    return this.http.put<UserDto>(this.baseUrl, user);
  }

  /** Cambia la contraseña del usuario actual */
  cambiarPassword(current: string, newPassword: string): Observable<void> {
    const body = { current, newPassword };
    return this.http.put<void>(`${this.baseUrl}/changePassword`, body);
  }

  /** Obtiene el historial de intentos de login del usuario */
  getLogs(): Observable<LoginAttemptDto[]> {
    return this.http.get<LoginAttemptDto[]>(`${this.baseUrl}/logs`);
  }

  /** Obtiene estadísticas (e.g., total de gastos) del usuario */
  getTotalSpents(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/count`);
  }

  /** Elimina la cuenta del usuario actual */
  deleteUser(): Observable<void> {
    return this.http.delete<void>(this.baseUrl);
  }

  /** Preguntas frecuentes locales */
  getQuestions(): { pregunta: string; respuesta: string }[] {
    return [
      {
        pregunta: '¿Cómo cambio mi nombre público?',
        respuesta: 'Ve a tu perfil y haz clic en el botón actualizar junto al nombre público.'
      },
      {
        pregunta: '¿Puedo cambiar mi servidor?',
        respuesta: 'No, el servidor no se puede cambiar una vez registrado.'
      }
    ];
  }
}
