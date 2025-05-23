import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { SubscriptionDto } from '../models/api-models/api-models.component';

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {
  // URL base para acceder a la API de subscripciones
  private readonly baseUrl = `${environment.apiUrl}/api/subscripciones`;

  constructor(private http: HttpClient) {}

  /**
   * Obtiene todas las subscripciones.
   * Se puede filtrar por clienteId, si se proporciona.
   *
   * @param clienteId Opcional, filtra las subscripciones por cliente.
   * @returns Observable que emite un array de SubscriptionDto.
   */
  getSubscriptions(clienteId?: number): Observable<SubscriptionDto[]> {
    let params = new HttpParams();
    if (clienteId != null) {
      params = params.set('clienteId', String(clienteId));
    }
    return this.http.get<SubscriptionDto[]>(this.baseUrl, { params });
  }

  /**
   * Obtiene una subscripción específica por su ID.
   *
   * @param id ID de la subscripción a obtener.
   * @returns Observable que emite el SubscriptionDto correspondiente.
   */
  getSubscriptionById(id: number): Observable<SubscriptionDto> {
    return this.http.get<SubscriptionDto>(`${this.baseUrl}/${id}`);
  }

  /**
   * Crea una nueva subscripción.
   *
   * @param subscription Objeto SubscriptionDto con los datos de la nueva subscripción.
   * @returns Observable que emite la subscripción creada.
   */
  addSubscription(subscription: SubscriptionDto): Observable<SubscriptionDto> {
    // Aquí podrías calcular campos derivados, por ejemplo:
    // subscription.accumulate = subscription.total + (subscription.total * (subscription.iva / 100));
    return this.http.post<SubscriptionDto>(this.baseUrl, subscription);
  }

  /**
   * Actualiza una subscripción existente.
   *
   * @param id ID de la subscripción a actualizar.
   * @param subscription Objeto SubscriptionDto con los datos actualizados.
   * @returns Observable que emite la subscripción actualizada.
   */
  updateSubscription(id: number, subscription: SubscriptionDto): Observable<SubscriptionDto> {
    // Igual que en addSubscription, aquí se puede calcular algún campo si es necesario.
    return this.http.put<SubscriptionDto>(`${this.baseUrl}/${id}`, subscription);
  }

  /**
   * Elimina una subscripción por su ID.
   *
   * @param id ID de la subscripción a eliminar.
   * @returns Observable que emite void al completarse la operación.
   */
  deleteSubscription(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
