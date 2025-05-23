import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';

/**
 * Componente CardComponent
 *
 * Este componente representa una tarjeta clickeable que muestra una imagen pequeña
 * junto con un texto. Al hacer clic en la tarjeta, se navega a una página de detalle
 * correspondiente según la sección especificada.
 *
 * Inputs:
 * - elemento: objeto que contiene la información a mostrar y que se usa para obtener
 *   datos como nombre, imagen e ID para navegación.
 * - seccion: cadena que indica a qué tipo de detalle se debe navegar al hacer clic.
 *   Se utiliza para determinar la ruta dinámica en el router.
 *
 * Propiedades internas:
 * - texto: texto que se mostrará en la tarjeta (normalmente nombre del elemento).
 * - imagen: URL de la imagen que se mostrará en la tarjeta.
 * - id: ID numérico extraído del elemento para la navegación.
 *
 * Funcionalidad principal:
 * - En ngOnInit se inicializan texto e imagen a partir del objeto elemento.
 * - Al hacer clic (cargarDetalle), se redirige a una ruta dinámica basada en la sección
 *   y el ID extraído del elemento.
 */
@Component({
  selector: 'app-card',       // Selector para usar este componente en templates
  standalone: false,          // No es un componente standalone (pertenece a módulo)
  template:`
  <a (click)="cargarDetalle()" class="card text-decoration-none text-reset shadow">
    <div class="card-body d-flex align-items-center">
      <!-- Imagen pequeña con ancho fijo 20px -->
      <img style="width:20px" [src]="[imagen]" alt="Icono">
      <!-- Texto con pipe titlecase para capitalizar cada palabra -->
      <p class="ms-3 mb-0">{{texto | titlecase }}</p>
    </div>
  </a>
  `
})
export class CardComponent {
  /**
   * Objeto que contiene la información del elemento a mostrar.
   * Se espera un arreglo donde el primer elemento contiene las propiedades necesarias.
   */
  @Input() elemento : any

  /**
   * Sección a la que pertenece el elemento, determina la ruta a la que navegar.
   * Ejemplos: 'mapas', 'monstruos', 'items', 'misiones', 'grupos', 'habilidades', 'npcs', 'efectos'.
   */
  @Input() seccion : string = ""

  /** Texto a mostrar en la tarjeta, extraído de elemento. */
  texto : string = ""

  /** URL de la imagen a mostrar en la tarjeta. */
  imagen : string = ""

  /** ID extraído del elemento para construir la ruta de navegación. */
  id : number = 0

  /**
   * Inyección del servicio Router para navegación programática.
   */
  constructor(private router: Router){}

  /**
   * Inicializa el texto e imagen al cargar el componente.
   * - Asigna el nombre desde elemento[0].nombre o "Error" si no existe.
   * - Asigna la imagen a un valor por defecto "/favicon.ico" (comentada la posible imagen dinámica).
   * - Muestra por consola el objeto elemento para debug.
   */
  ngOnInit(): void {
    if (this.elemento) {
      this.texto = this.elemento[0].nombre ?? "Error";
      this.imagen = /*this.elemento[0].imagen ??*/ "/favicon.ico";
      // Debug en consola del elemento recibido
      console.log(this.elemento)
    }
  }

  /**
   * Método que se ejecuta al hacer clic en la tarjeta.
   * Determina la ruta de detalle a la que navegar en base a la sección.
   * Extrae el ID adecuado del elemento y llama al router para cambiar la ruta.
   * En caso de sección no válida, muestra un error en consola.
   */
  cargarDetalle() {
    switch(this.seccion) {
      case "mapas":
        this.id = this.elemento[0].mapa_id;
        this.router.navigate(["/detalle/mapa/" + this.id]);
        break;
      case "monstruos":
        this.id = this.elemento[0].id;
        this.router.navigate(["/detalle/monstruo/" + this.id]);
        break;
      case "items":
        this.id = this.elemento[0].item_id;
        this.router.navigate(["/detalle/item/" + this.id]);
        break;
      case "misiones":
        this.id = this.elemento[0].id;
        this.router.navigate(["/detalle/mision/" + this.id]);
        break;
      case "grupos":
        this.id = this.elemento[0].grupo_id;
        this.router.navigate(["/detalle/grupos/" + this.id]);
        break;
      case "habilidades":
        this.id = this.elemento[0].habilidad_id;
        this.router.navigate(["/detalle/habilidad/" + this.id]);
        break;
      case "npcs":
        this.id = this.elemento[0].id;
        console.log(this.elemento[0]);
        this.router.navigate(["/detalle/npc/" + this.id]);
        break;
      case "efectos":
        this.id = this.elemento[0].efecto_id;
        this.router.navigate(["/detalle/efectos/" + this.id]);
        break;
      default:
        // Sección inválida: muestra error en consola
        console.error("Sección no válida");
        break;
    }
  }
}
