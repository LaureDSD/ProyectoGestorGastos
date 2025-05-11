import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { TicketDto } from '../models/api-models/api-models.component';

@Injectable({
  providedIn: 'root'
})
export class TicketService {
  private readonly baseUrl = `${environment.apiUrl}/api/tickets`;

  constructor(private http: HttpClient) {}

  /** Obtiene todos los tickets, opcionalmente filtrados por clienteId */
  getTickets(clienteId?: number): Observable<TicketDto[]> {
    let params = new HttpParams();
    if (clienteId != null) {
      params = params.set('clienteId', String(clienteId));
    }
    return this.http.get<TicketDto[]>(this.baseUrl, { params });
  }

  /** Obtiene un ticket por su ID */
  getTicketById(ticketId: number): Observable<TicketDto> {
    return this.http.get<TicketDto>(`${this.baseUrl}/${ticketId}`);
  }

  /** Crea un nuevo ticket */
  addTicket(ticket: TicketDto): Observable<TicketDto> {
    return this.http.post<TicketDto>(this.baseUrl, ticket);
  }

  /** Actualiza un ticket existente */
  updateTicket(ticketId: number, ticket: TicketDto): Observable<TicketDto> {
    return this.http.put<TicketDto>(`${this.baseUrl}/${ticketId}`, ticket);
  }

  /** Elimina un ticket por su ID */
  deleteTicket(ticketId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${ticketId}`);
  }
}
