import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { Ticket } from '../models/api-models/api-models.component';

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  private baseUrl = `${environment.apiUrl}/api/tickets`;
  constructor(private http: HttpClient) {}

  getTickets(clienteId?: number): Observable<any[]> {
    const url = clienteId ? `${this.baseUrl}/?clienteId=${clienteId}` : `${this.baseUrl}/`;
    return this.http.get<any[]>(url);
  }

  getTicketById(ticketId: number): Observable<Ticket> {
    return this.http.get<Ticket>(`${this.baseUrl}/${ticketId}`);
  }

  addTicket(ticket: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/`, ticket);
  }

  updateTicket(id: number, ticket: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${id}`, ticket);
  }

  deleteTicket(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

}
