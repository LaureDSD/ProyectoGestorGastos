import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {

private baseUrl = `${environment.apiUrl}/api/tickets`;
constructor(private http: HttpClient) {}

getSubscriptions(clienteId?: number): Observable<any[]> {
    const url = clienteId ? `${this.baseUrl}/?clienteId=${clienteId}` : `${this.baseUrl}/`;
    return this.http.get<any[]>(url);
  }

  getSubscriptionById(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/${id}`);
  }

  addSubscription(subscription: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/`, subscription);
  }

  updateSubscription(id: number, subscription: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${id}`, subscription);
  }

  deleteSubscription(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
