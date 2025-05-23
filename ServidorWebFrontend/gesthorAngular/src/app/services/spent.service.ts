import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { SpentDto } from '../models/api-models/api-models.component';

@Injectable({
  providedIn: 'root'
})
export class SpentService {
  // URL base para la API de gastos
  private readonly baseUrl = `${environment.apiUrl}/api/gastos`;

  constructor(private http: HttpClient) {}

  /**
   * Obtiene todos los gastos.
   * Se puede filtrar por clienteId si se proporciona.
   * @param clienteId Opcional, filtra los gastos por cliente
   * @returns Observable con arreglo de gastos (SpentDto[])
   */
  getSpents(clienteId?: number): Observable<SpentDto[]> {
    let params = new HttpParams();
    if (clienteId != null) {
      params = params.set('clienteId', String(clienteId));
    }
    return this.http.get<SpentDto[]>(this.baseUrl, { params });
  }

  /**
   * Obtiene todos los gastos con detalles completos.
   * También admite filtro por clienteId.
   * @param clienteId Opcional, filtra gastos por cliente
   * @returns Observable con arreglo de gastos completos (SpentDto[])
   */
  getFullSpents(clienteId?: number): Observable<SpentDto[]> {
    let params = new HttpParams();
    if (clienteId != null) {
      params = params.set('clienteId', String(clienteId));
    }
    return this.http.get<SpentDto[]>(`${this.baseUrl}/fullspents`, { params });
  }

  /**
   * Obtiene un gasto específico por su ID.
   * @param id ID del gasto
   * @returns Observable con el gasto (SpentDto)
   */
  getSpentById(id: number): Observable<SpentDto> {
    return this.http.get<SpentDto>(`${this.baseUrl}/${id}`);
  }

  /**
   * Crea un nuevo gasto.
   * @param spent Objeto SpentDto con datos del gasto a crear
   * @returns Observable con el gasto creado
   */
  addSpent(spent: SpentDto): Observable<SpentDto> {
    return this.http.post<SpentDto>(this.baseUrl, spent);
  }

  /**
   * Actualiza un gasto existente.
   * @param id ID del gasto a actualizar
   * @param spent Datos actualizados
   * @returns Observable con el gasto actualizado
   */
  updateSpent(id: number, spent: SpentDto): Observable<SpentDto> {
    return this.http.put<SpentDto>(`${this.baseUrl}/${id}`, spent);
  }

  /**
   * Elimina un gasto por su ID.
   * @param id ID del gasto a eliminar
   * @returns Observable que emite void al completar la eliminación
   */
  deleteSpent(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  /**
   * Sube o actualiza la imagen asociada a un gasto.
   * @param spentId ID del gasto al que se asocia la imagen
   * @param image Archivo de imagen a subir
   * @returns Observable con la respuesta del servidor
   */
  subirFotoGasto(spentId: number, image: File): Observable<any> {
    const formData = new FormData();
    formData.append('image', image);
    formData.append('spentId', spentId.toString());
    return this.http.put<any>(`${this.baseUrl}/me/uploadSpenseImage`, formData);
  }
}
