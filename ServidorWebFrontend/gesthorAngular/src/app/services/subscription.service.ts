import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { SubscriptionDto } from '../models/api-models/api-models.component';

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {
  private readonly baseUrl = `${environment.apiUrl}/api/subscripciones`;

  constructor(private http: HttpClient) {}

  /** Obtiene todas las subscripciones, opcionalmente filtradas por clienteId */
  getSubscriptions(clienteId?: number): Observable<SubscriptionDto[]> {
    let params = new HttpParams();
    if (clienteId != null) {
      params = params.set('clienteId', String(clienteId));
    }
    return this.http.get<SubscriptionDto[]>(this.baseUrl, { params });
  }

  /** Obtiene una subscripción por su ID */
  getSubscriptionById(id: number): Observable<SubscriptionDto> {
    return this.http.get<SubscriptionDto>(`${this.baseUrl}/${id}`);
  }

  /** Crea una nueva subscripción */
  addSubscription(subscription: SubscriptionDto): Observable<SubscriptionDto> {
    return this.http.post<SubscriptionDto>(this.baseUrl, subscription);
  }

  /** Actualiza una subscripción existente */
  updateSubscription(id: number, subscription: SubscriptionDto): Observable<SubscriptionDto> {
    return this.http.put<SubscriptionDto>(`${this.baseUrl}/${id}`, subscription);
  }

  /** Elimina una subscripción por su ID */
  deleteSubscription(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
