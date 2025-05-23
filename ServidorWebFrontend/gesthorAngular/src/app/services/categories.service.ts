import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { CategoryDto } from '../models/api-models/api-models.component';

/**
 * Servicio Angular para interactuar con la API de categorías.
 * Provee métodos para obtener datos relacionados con las categorías desde el backend.
 */
@Injectable({
  providedIn: 'root'  // Permite que el servicio sea inyectado en toda la aplicación
})
export class CategoryService {

  // URL base para las operaciones relacionadas con categorías en la API
  private readonly baseUrl = `${environment.apiUrl}/api/categorias`;

  /**
   * Constructor que inyecta HttpClient para hacer peticiones HTTP.
   * @param http Cliente HTTP para comunicación con el backend.
   */
  constructor(private http: HttpClient) {}

  /**
   * Obtiene la lista completa de categorías desde la API.
   * Realiza una petición GET al endpoint correspondiente.
   * @returns Observable que emite un arreglo de objetos CategoryDto.
   */
  getCategories(): Observable<CategoryDto[]> {
    return this.http.get<CategoryDto[]>(this.baseUrl);
  }
}
