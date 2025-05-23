/**
 * Componente Angular SeccionComponent
 *
 * Este componente muestra una sección con un conjunto de elementos que se representan como tarjetas.
 * Incluye un título, un indicador de carga, y la posibilidad de filtrar los elementos por texto de búsqueda.
 *
 * Funcionalidades principales:
 * - Recibe por @Input() el texto de búsqueda, la ruta para cargar datos, y el título de la sección.
 * - Obtiene los parámetros de ruta para actualizar la búsqueda cuando cambian.
 * - Muestra un spinner mientras se cargan los datos.
 * - Renderiza una lista de elementos usando el componente <app-card>, pasando cada elemento y el título de la sección.
 * - Incluye un método para filtrar los resultados según el texto de búsqueda en nombre o descripción.
 *
 * Propiedades:
 * @Input() busqueda: string - Texto para filtrar elementos.
 * @Input() ruta: string - Ruta para solicitar la sección al servicio API.
 * @Input() titulo: string - Título que se muestra en la cabecera de la sección.
 * @Output() elementos: any[] - Lista de elementos a mostrar, actualizada tras filtrar o cargar datos.
 *
 * Variables internas:
 * - lista: any[] - Lista completa de elementos recibidos del servicio.
 * - error: string - Mensaje de error para mostrar si falla la carga.
 * - cargando: boolean - Indicador de estado de carga (true mientras se reciben datos).
 *
 * Métodos:
 * - ngOnInit(): suscribe a cambios en parámetros de ruta para actualizar la búsqueda.
 * - filtrarResultados(data: any[]): any[] - Filtra la lista de datos buscando coincidencias en nombre o descripción.
 * - cargarSeccion(): (comentado) - Método para solicitar datos al servicio API y actualizar la lista y estado.
 *
 * Uso en plantilla:
 * - Se muestra un spinner mientras cargando sea true.
 * - Se recorren los elementos para mostrar tarjetas individuales.
 *
 */

import { Component, Input, Output } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiserviceService } from '../../services/apiservice.service';

@Component({
  selector: 'app-seccion',
  standalone: false,
  template: `
    <div class="card m-3" data-bs-theme="dark">
      <div class="card-body row row-cols-2 row-cols-md-3 g8">

        <h3 class="p-3">{{ titulo | titlecase }}</h3>

        <div class="m-3" *ngIf="cargando">
          <app-spinning></app-spinning>
        </div>

        <div *ngFor="let e of elementos" class="col p-2">
          <app-card [elemento]="[e]" [seccion]="titulo"></app-card>
        </div>

      </div>
    </div>
  `
})
export class SeccionComponent {

  /** Texto para filtrar elementos mostrados */
  @Input() busqueda: string = "";

  /** Ruta relativa para solicitar datos desde el servicio API */
  @Input() ruta: string = "";

  /** Título que aparece en la cabecera de la sección */
  @Input() titulo: string = "";

  /** Lista completa de elementos recibidos */
  lista: any[] = [];

  /** Lista filtrada o completa de elementos para mostrar */
  @Output() elementos: any[] = [];

  /** Mensaje de error si ocurre un fallo en la carga */
  error: string = "";

  /** Indicador de carga activa */
  cargando: boolean = true;

  /**
   * Constructor con inyección de servicios ActivatedRoute y ApiserviceService
   */
  constructor(private activateRoute: ActivatedRoute, private servicio: ApiserviceService) {}

  /**
   * Método inicial que se ejecuta al montar el componente.
   * Suscribe a cambios en parámetros de ruta para actualizar la búsqueda.
   */
  ngOnInit() {
    this.activateRoute.params.subscribe(params => {
      this.busqueda = params['texto'] || '';
      /*this.cargarSeccion();*/
    });
  }

  /**
   * Método para cargar datos de la sección usando el servicio API.
   * (Actualmente comentado)
   * Actualiza la lista completa y la lista filtrada según el texto de búsqueda.
   */
  /*
  cargarSeccion() {
    this.servicio.obtenerSeccion(this.ruta + "/").subscribe(
      data => {
        console.log("Seccion >>>>>> " + this.busqueda);
        this.lista = data;
        this.cargando = false;

        this.elementos = this.busqueda ? this.filtrarResultados(this.lista) : this.lista;
      },
      error => {
        this.error = 'Error de carga';
        this.cargando = false;
      }
    );
  }
  */

  /**
   * Filtra una lista de datos buscando coincidencias parciales en las propiedades nombre o descripcion.
   * @param data Arreglo de objetos con propiedades nombre y descripcion
   * @returns Arreglo filtrado que contiene sólo elementos que coinciden con el texto de búsqueda (case insensitive)
   */
  filtrarResultados(data: any[]): any[] {
    return data.filter((dato: { nombre: string; descripcion: string }) =>
      dato.nombre?.toLowerCase().includes(this.busqueda.toLowerCase()) ||
      dato.descripcion?.toLowerCase().includes(this.busqueda.toLowerCase())
    );
  }
}
