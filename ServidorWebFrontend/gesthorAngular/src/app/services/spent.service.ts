import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SpentService {

private baseUrl = `${environment.apiUrl}/api/gastos`;
constructor(private http: HttpClient) {}

getSpents(clienteId?: number): Observable<any[]> {
    const url = clienteId ? `${this.baseUrl}/?clienteId=${clienteId}` : `${this.baseUrl}/`;
    return this.http.get<any[]>(url);
}

getSpentById(id: number): Observable<any> {
  return this.http.get<any>(`${this.baseUrl}/${id}`);
}

addSpent(spent: any): Observable<any> {
  return this.http.post<any>(`${this.baseUrl}/`, spent);
}

updateSpent(id: number, spent: any): Observable<any> {
  return this.http.put<any>(`${this.baseUrl}/${id}`, spent);
}

deleteSpent(id: number): Observable<void> {
  return this.http.delete<void>(`${this.baseUrl}/${id}`);
}

}
