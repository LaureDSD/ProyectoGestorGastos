import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ServerInfoDto } from '../models/api-models/api-models.component';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ServerStatsService {
  // URL base para la API de información del servidor
  private baseUrl = `${environment.apiUrl}/api/server`;

  constructor(private http: HttpClient) {}

  /**
   * Obtiene la información del servidor.
   * @returns Observable con datos del servidor (ServerInfoDto)
   */
  getServerInfo(): Observable<ServerInfoDto> {
    return this.http.get<ServerInfoDto>(`${this.baseUrl}/info`);
  }
}
