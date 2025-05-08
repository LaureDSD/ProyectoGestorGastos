import { Contacto, FormComponent } from '../components/form/form.component';
import { HttpClient , HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { AuthService } from './auth.service';

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

  //Falta
  enviarMensaje(contacto: Contacto) {
    return this.http.post(`${environment.apiUrl}/public/forms`, contacto);
  }

  getApiServer(): any {
    return { name: "GESTHOR1" , activity: "12321"}
  }

}
