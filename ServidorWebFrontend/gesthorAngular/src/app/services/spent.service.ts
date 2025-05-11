import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { SpentDto } from '../models/api-models/api-models.component';

@Injectable({
  providedIn: 'root'
})
export class SpentService {
  private readonly baseUrl = `${environment.apiUrl}/api/gastos`;

  constructor(private http: HttpClient) {}

  /** Obtiene todos los gastos, opcionalmente filtrados por clienteId */
  getSpents(clienteId?: number): Observable<SpentDto[]> {
    let params = new HttpParams();
    if (clienteId != null) {
      params = params.set('clienteId', String(clienteId));
    }
    return this.http.get<SpentDto[]>(this.baseUrl, { params });
  }

  /** Obtiene un gasto por su ID */
  getSpentById(id: number): Observable<SpentDto> {
    return this.http.get<SpentDto>(`${this.baseUrl}/${id}`);
  }

  /** Crea un nuevo gasto */
  addSpent(spent: SpentDto): Observable<SpentDto> {
    return this.http.post<SpentDto>(this.baseUrl, spent);
  }

  /** Actualiza un gasto existente */
  updateSpent(id: number, spent: SpentDto): Observable<SpentDto> {
    return this.http.put<SpentDto>(`${this.baseUrl}/${id}`, spent);
  }

  /** Elimina un gasto por su ID */
  deleteSpent(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
