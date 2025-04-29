import { HttpClient , HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { AuthService } from './auth.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiserviceService {

    private baseUrl = `${environment.apiUrl}/auth`;
    router: any;

    constructor(
      private http: HttpClient,
      private secureApi : AuthService ) { }

      getTotalUsers() {
        return this.http.get(`${environment.apiUrl}/public/usuarios`);
      }

/* Ejemplo
  obtenerSeccion(ruta: string): Observable<any> {
    const token = this.secureApi.getToken();
    const headers = token
      ? new HttpHeaders({ 'Authorization': `Bearer ${token}` })
      : new HttpHeaders();

    return this.http.get<any>(`http://localhost:8080/api/${ruta}`, { headers });
  }*/

}
