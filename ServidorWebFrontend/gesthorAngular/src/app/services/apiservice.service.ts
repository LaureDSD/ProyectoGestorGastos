import { FormComponent } from '../components/form/form.component';
import { HttpClient , HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { AuthService } from './auth.service';
import { Observable } from 'rxjs';
import { ServerInfoDto } from '../models/api-models/api-models.component';
import { Contacto } from '../models/models/models.component';

/**
 * Servicio Angular para interactuar con distintos endpoints públicos y privados de la API.
 * Proporciona métodos para obtener usuarios, gastos, tickets, enviar formularios y obtener info del servidor.
 */
@Injectable({
  providedIn: 'root' // Permite inyección global del servicio en la app
})
export class ApiserviceService {

  // URL base para endpoints públicos
  private baseUrl = `${environment.apiUrl}/public`;

  /**
   * Constructor que inyecta HttpClient para comunicación HTTP.
   * @param http Cliente HTTP para llamadas a API.
   */
  constructor(
    private http: HttpClient) { }

  /**
   * Obtiene la lista total de usuarios desde el endpoint público `/usuarios`.
   * @returns Observable con la respuesta HTTP (lista de usuarios o datos).
   */
  getTotalUsers() {
    return this.http.get(`${environment.apiUrl}/usuarios`);
  }

  /**
   * Obtiene la suma total de gastos públicos desde el endpoint `/public/gastos`.
   * @returns Observable que emite un número con el total de gastos.
   */
  getPublicSpents(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/gastos`);
  }

  /**
   * Obtiene la cantidad total de tickets públicos desde el endpoint `/public/tickets`.
   * @returns Observable que emite un número con el total de tickets.
   */
  getPublicTickets(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/tickets`);
  }

  /**
   * Envía un formulario de contacto al backend para ser procesado.
   * El objeto `contacto` contiene los datos enviados por el usuario.
   * @param contacto Datos del formulario de contacto.
   * @returns Observable con la respuesta HTTP del servidor.
   */
  enviarMensaje(contacto: Contacto) {
    return this.http.post(`${environment.apiUrl}/public/contacto`, contacto);
  }

  /**
   * Obtiene información del servidor a través del endpoint `/public/server`.
   * Nota: Endpoint pendiente de creación.
   * @returns Observable que emite un objeto ServerInfoDto con los datos del servidor.
   */
  getApiServer(): Observable<ServerInfoDto> {
    return this.http.get<ServerInfoDto>(`${environment.apiUrl}/public/server`);
  }

}
