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

/**
 * Servicio para gestionar operaciones relacionadas con el usuario actual.
 * Encapsula llamadas HTTP para obtener y actualizar información de usuario,
 * manejar su foto de perfil, cambiar contraseña, obtener logs de login,
 * estadísticas de gastos y eliminar la cuenta.
 *
 * Está marcado con providedIn: 'root' para que sea singleton y disponible
 * globalmente en la aplicación Angular.
 */
@Injectable({
  providedIn: 'root'
})
export class UserserviceService {
  // URL base del API para las operaciones relacionadas al usuario actual
  private readonly baseUrl = `${environment.apiUrl}/api/user/me` ;

  constructor(private http: HttpClient) {}

  /**
   * Obtiene todos los datos del usuario actual en forma de UserDto.
   * Realiza una petición GET al endpoint `/api/user/me`.
   *
   * @returns Observable<UserDto> con los datos completos del usuario autenticado.
   */
  getFullCurrentUser(): Observable<UserDto> {
    return this.http.get<UserDto>(this.baseUrl);
  }

  /**
   * Sube o actualiza la foto de perfil del usuario.
   * Envía un FormData que contiene la imagen al endpoint `/api/user/me/uploadProfile`
   * mediante un PUT HTTP.
   *
   * @param image FormData con el archivo de imagen a subir.
   * @returns Observable<any> con la respuesta del servidor, normalmente el usuario actualizado.
   */
  subirFotoPerfil(image: FormData): Observable<any> {
    return this.http.put<UserDto>(`${this.baseUrl}/uploadProfile`, image);
  }

  /**
   * Actualiza los datos parciales del usuario actual.
   * Envía un objeto parcial UserDto con los campos a actualizar.
   * Realiza un PUT HTTP a `/api/user/me`.
   *
   * @param user Partial<UserDto> con los campos que se desean modificar.
   * @returns Observable<UserDto> con los datos actualizados del usuario.
   */
  actualizarUsuario(user: Partial<UserDto>): Observable<UserDto> {
    return this.http.put<UserDto>(this.baseUrl, user);
  }

  /**
   * Cambia la contraseña del usuario actual.
   * Envía el password actual y el nuevo password para actualizarlo.
   * Realiza un PUT HTTP a `/api/user/me/changePassword`.
   *
   * @param current Contraseña actual del usuario.
   * @param newPassword Nueva contraseña que se desea establecer.
   * @returns Observable<void> que indica éxito o error en la operación.
   */
  cambiarPassword(current: string, newPassword: string): Observable<void> {
    const body = { current, newPassword };
    return this.http.put<void>(`${this.baseUrl}/changePassword`, body);
  }

  /**
   * Obtiene el historial de intentos de login del usuario.
   * Realiza una petición GET a `/api/user/me/logs`.
   *
   * @returns Observable<LoginAttemptDto[]> con los registros de intentos de login.
   */
  getLogs(): Observable<LoginAttemptDto[]> {
    return this.http.get<LoginAttemptDto[]>(`${this.baseUrl}/logs`);
  }

  /**
   * Obtiene estadísticas básicas del usuario, como el total de gastos realizados.
   * Realiza una petición GET a `/api/user/me/count`.
   *
   * @returns Observable<number> con el total de gastos o alguna métrica similar.
   */
  getTotalSpents(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/count`);
  }

  /**
   * Elimina la cuenta del usuario actual.
   * Realiza una petición DELETE a `/api/user/me`.
   *
   * @returns Observable<void> indicando éxito o fallo de la eliminación.
   */
  deleteUser(): Observable<void> {
    return this.http.delete<void>(this.baseUrl);
  }

  /**
   * Devuelve un listado estático de preguntas frecuentes (FAQ) relacionadas con el usuario.
   * Esta función simula una carga local, ya que no hace llamadas HTTP.
   *
   * @returns Array con objetos que contienen `pregunta` y `respuesta`.
   */
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
