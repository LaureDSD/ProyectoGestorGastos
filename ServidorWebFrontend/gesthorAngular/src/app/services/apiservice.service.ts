import { Contacto, FormComponent } from '../components/form/form.component';
import { HttpClient , HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { AuthService } from './auth.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiserviceService {

  private baseUrl = `${environment.apiUrl}/public`;

  constructor(
    private http: HttpClient) { }

  getTotalUsers() {
    return this.http.get(`${environment.apiUrl}/usuarios`);
  }

  //Falta crear endpoint
  enviarMensaje(contacto: Contacto) {
    return this.http.post(`${environment.apiUrl}/forms`, contacto);
  }

  //Falta crear endpoint
  getApiServer(): any {
    return { name: "GESTHOR1" , activity: "12321"}
  }

  

}
