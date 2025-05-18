import { FormComponent } from '../components/form/form.component';
import { HttpClient , HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { AuthService } from './auth.service';
import { Observable } from 'rxjs';
import { ServerInfoDto } from '../models/api-models/api-models.component';
import { Contacto } from '../models/models/models.component';

@Injectable({
  providedIn: 'root'
})
export class ApiserviceService {

  private baseUrl = `${environment.apiUrl}/public`;

  constructor(
    private http: HttpClient) { }

  /** Obtiene todos los usuarios */
  getTotalUsers() {
    return this.http.get(`${environment.apiUrl}/usuarios`);
  }

  /** Obtiene todos los gastos públicos */
  getPublicSpents(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/gastos`);
  }

  /** Obtiene todos los tickets públicos */
  getPublicTickets(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/tickets`);
  }

  /**Envia formulario al server*/
  enviarMensaje(contacto: Contacto) {
    return this.http.post(`${environment.apiUrl}/public/contacto`, contacto);
  }

  //Falta crear endpoint /server
  getApiServer(): Observable<ServerInfoDto> {
  return this.http.get<ServerInfoDto>(`${environment.apiUrl}/public/server`);
  }



}
