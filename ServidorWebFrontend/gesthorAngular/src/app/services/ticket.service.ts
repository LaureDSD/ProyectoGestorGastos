import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { TicketDto } from '../models/api-models/api-models.component';

/**
 * Servicio para gestionar operaciones CRUD relacionadas con tickets.
 * Provee métodos para obtener, crear, actualizar y eliminar tickets
 * a través de llamadas HTTP a la API REST correspondiente.
 */
@Injectable({
  providedIn: 'root'
})
export class TicketService {
  // URL base para los endpoints de tickets
  private readonly baseUrl = `${environment.apiUrl}/api/tickets`;

  constructor(private http: HttpClient) {}

  /**
   * Obtiene una lista de todos los tickets.
   * Opcionalmente puede filtrar los tickets por clienteId enviando ese parámetro.
   *
   * @param clienteId (opcional) ID del cliente para filtrar los tickets.
   * @returns Observable con un arreglo de TicketDto.
   */
  getTickets(clienteId?: number): Observable<TicketDto[]> {
    let params = new HttpParams();
    if (clienteId != null) {
      params = params.set('clienteId', String(clienteId));
    }
    return this.http.get<TicketDto[]>(this.baseUrl, { params });
  }

  /**
   * Obtiene un ticket específico dado su ID.
   *
   * @param ticketId ID del ticket a obtener.
   * @returns Observable con el TicketDto correspondiente.
   */
  getTicketById(ticketId: number): Observable<TicketDto> {
    return this.http.get<TicketDto>(`${this.baseUrl}/${ticketId}`);
  }

  /**
   * Crea un nuevo ticket en el sistema.
   *
   * @param ticket Objeto TicketDto con los datos del nuevo ticket.
   * @returns Observable con el TicketDto creado.
   */
  addTicket(ticket: TicketDto): Observable<TicketDto> {
    return this.http.post<TicketDto>(this.baseUrl, ticket);
  }

  /**
   * Actualiza un ticket existente identificado por su ID.
   *
   * @param ticketId ID del ticket a actualizar.
   * @param ticket Objeto TicketDto con los datos actualizados.
   * @returns Observable con el TicketDto actualizado.
   */
  updateTicket(ticketId: number, ticket: TicketDto): Observable<TicketDto> {
    return this.http.put<TicketDto>(`${this.baseUrl}/${ticketId}`, ticket);
  }

  /**
   * Elimina un ticket dado su ID.
   *
   * @param ticketId ID del ticket a eliminar.
   * @returns Observable<void> que indica éxito o fallo de la eliminación.
   */
  deleteTicket(ticketId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${ticketId}`);
  }
}
