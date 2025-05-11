import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OcrService {

private baseUrl = `${environment.apiUrl}/api/ocr`;

  constructor(private http: HttpClient) { }

  procesarTicketImagen(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('archivo', file);

    return this.http.post<any>(`${this.baseUrl}/ticket`, formData, {
      headers: new HttpHeaders(),
    });
  }

  procesarTicketDigital(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('archivo', file);

    return this.http.post<any>(`${this.baseUrl}/ticketdigital`, formData, {
      headers: new HttpHeaders(),
    });
  }

}
