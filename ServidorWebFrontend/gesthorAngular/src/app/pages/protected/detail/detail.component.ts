/**
 * Componente Angular DetailComponent
 *
 * Este componente muestra los detalles de un elemento específico
 * basado en parámetros dinámicos de ruta ('seccion' e 'id').
 *
 * Funcionalidad principal:
 * - Obtiene los parámetros de la URL mediante ActivatedRoute.
 * - Almacena los datos del elemento seleccionado y muestra propiedades dinámicas.
 * - Tiene la estructura para cargar detalles desde un servicio API (código comentado).
 *
 * Uso:
 * Se utiliza para visualizar detalles de diferentes secciones,
 * por ejemplo, detalles de un objeto específico identificado por 'id'.
 */

import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiserviceService } from '../../../services/apiservice.service';

@Component({
  selector: 'app-detail',              // Selector HTML para usar este componente
  standalone: false,                   // No es standalone, se declara en módulos
  templateUrl: './detail.component.html',  // Archivo HTML asociado para la vista
  styleUrl: './detail.component.css'        // Archivo CSS asociado para estilos
})
export class DetailComponent {
  // Objeto que contiene los datos del elemento cargado
  elemento: any = {};

  // Referencia a Object.keys para iterar dinámicamente sobre las propiedades de 'elemento'
  objectKeys = Object.keys;

  // Parámetro de ruta que indica la sección o categoría del elemento
  seccion: string = '';

  // Parámetro de ruta que indica el identificador del elemento
  id: string = '';

  // Mensaje de error para mostrar en caso de fallo en la carga de datos
  errorMessage: string = '';

  // Lista de items relacionados (por ejemplo, 'drops' o 'recompensas')
  listaItems : any[] = [];

  /**
   * Constructor del componente
   *
   * @param servicio Servicio que provee métodos para consumir APIs
   * @param route Servicio para obtener parámetros de la ruta actual
   */
  constructor(
    private servicio: ApiserviceService,
    private route: ActivatedRoute
  ) { }

  /**
   * Método del ciclo de vida Angular que se ejecuta al inicializar el componente
   * Se suscribe a los parámetros de la ruta para obtener 'seccion' e 'id'.
   * Idealmente, aquí se debería llamar a la función que carga los detalles del elemento.
   */
  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.seccion = params.get('seccion')!;  // Obtiene el parámetro 'seccion' de la ruta
      this.id = params.get('id')!;            // Obtiene el parámetro 'id' de la ruta
      /*
      Código comentado para cargar detalles desde el servicio API:
      this.cargarDetalles(this.seccion, this.id);
      */
    });
  }

  /**
   * Método para cargar detalles del elemento desde el API (actualmente comentado)
   *
   * @param seccion Nombre de la sección o recurso a consultar
   * @param id Identificador del elemento a obtener
   *
   * Realiza una llamada HTTP a la API para obtener datos detallados,
   * almacena el resultado en 'elemento' y extrae listas asociadas.
   * Captura errores y actualiza 'errorMessage' en caso de fallo.
   */
  /*
  cargarDetalles(seccion: string, id: string): void {
    this.servicio.obtenerSeccion(`${seccion}/${id}`).subscribe({
      next: (response) => {
        console.log(seccion,id);
        this.elemento = response;
        // Extrae arrays de drops o recompensas si existen
        this.listaItems = this.elemento.drops ?? this.elemento.recompensas ?? [];
      },
      error: (err) => {
        this.errorMessage = 'Error de carga';
        console.error('Error en detalles:', err);
      }
    });
  }
  */
}
