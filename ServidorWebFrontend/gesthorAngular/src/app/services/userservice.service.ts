import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserserviceService {

  private baseUrl = `${environment.apiUrl}`;

  constructor(private http: HttpClient) { }

  getFullCurrentUser(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/api/user/me`);
  }

  subirFotoPerfil(image: FormData): Observable<any> {
    return this.http.put(`${this.baseUrl}/api/user/me/uploadProfile`, image);
  }

  actualizarUsuario(user: any): Observable<any> {
    console.log(user)
    return this.http.put(`${this.baseUrl}/api/user/me`, user);
  }

  cambiarPassword(current: string, newPassword: string): Observable<any> {
    const body = { current, newPassword };
    return this.http.put(`${this.baseUrl}/api/user/me/changePassword`, body);
  }

  getLogs(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/api/user/me/logs`);
  }

  deleteUser(){
    return this.http.delete(`${this.baseUrl}/api/user/me`);
  }

  getQuestions(){
    return [
      {
        pregunta: '¿Cómo cambio mi nombre público?',
        respuesta: 'Ve a tu perfil y haz clic en el botón actualizar junto al nombre público.',
        class: 'accordion-collapse collapse'
      },
      {
        pregunta: '¿Puedo cambiar mi servidor?',
        respuesta: 'No, el servidor no se puede cambiar una vez registrado.',
        class: 'accordion-collapse collapse'
      }
    ];
  }
}
