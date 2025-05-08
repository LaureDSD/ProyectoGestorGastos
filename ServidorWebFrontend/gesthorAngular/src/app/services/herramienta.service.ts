import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class HerramientaService {

  private apiUrl = 'https://tu-backend.com/api/herramientas';

  constructor(private http: HttpClient) {}

  subirArchivo(formData: FormData) {
    return this.http.post<any>(`${this.apiUrl}/subir`, formData);
  }

  crearHerramienta(datos: any) {
    return this.http.post<any>(`${this.apiUrl}/crear`, datos);
  }

  obtenerHerramientas() {
    return this.http.get<any[]>(`${this.apiUrl}`);
  }

  filtrarHerramientas(fechaInicio: string, fechaFin: string) {
    return this.http.get<any[]>(
      `${this.apiUrl}/filtrar?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`
    );
  }
}
