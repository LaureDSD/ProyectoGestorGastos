import { Component, Input } from '@angular/core';

/**
 * Componente CarrouselComponent
 *
 * Este componente presenta un carrusel de imágenes utilizando Bootstrap 5.
 * Permite mostrar un conjunto de imágenes agrupadas en "slides" con controles
 * para navegar entre ellos. Cada slide contiene un número configurable de imágenes.
 *
 * Inputs:
 * - imglist: arreglo de URLs de las imágenes a mostrar en el carrusel.
 * - imgSlide: número de imágenes que se mostrarán por cada slide del carrusel.
 * - title: título que se muestra arriba del carrusel.
 * - text: texto descriptivo que se muestra debajo del carrusel.
 *
 * Funcionamiento interno:
 * - Se divide la lista de imágenes en grupos (chunks) de tamaño imgSlide para
 *   crear cada slide.
 * - El primer slide se marca como activo para que el carrusel inicie en ese.
 * - Usa las clases y controles nativos de Bootstrap para el comportamiento del carrusel.
 */
@Component({
  selector: 'app-carrousel',    // Selector para usar el componente en templates
  standalone: false,            // No es un componente standalone (se usa dentro de módulos)
  template:`
  <section class="container" style=" text-align: center;">
    <!-- Título principal del carrusel -->
    <h2 class="mb-5 mt-0">{{title}}</h2>

    <!-- Contenedor principal del carrusel Bootstrap -->
    <div id="carouselExampleDark" class="carousel" data-bs-ride="carousel">

      <!-- Contenedor de los slides -->
      <div class="carousel-inner">
        <!-- Itera sobre los grupos de imágenes divididos en chunks -->
        <div *ngFor="let group of chunkedImages; let i = index" class="carousel-item" [ngClass]="{ active: i === 0 }">
          <div class="row">
            <!-- Por cada imagen en el grupo, crea una columna con la imagen -->
            <div class="col" *ngFor="let img of group">
              <img [src]="img" class="d-block w-100" alt="..." />
            </div>
          </div>
        </div>
      </div>

      <!-- Botón para ir al slide anterior -->
      <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleDark" data-bs-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Anterior</span>
      </button>

      <!-- Botón para ir al siguiente slide -->
      <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleDark" data-bs-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Siguiente</span>
      </button>
    </div>

    <!-- Texto descriptivo debajo del carrusel -->
    <p class="mt-5">{{text}}</p>
  </section>
  `
})
export class CarrouselComponent {
  /**
   * Lista de URLs de las imágenes a mostrar en el carrusel.
   * Cada URL debe apuntar a una imagen válida.
   */
  @Input() imglist : string[] = []

  /**
   * Número de imágenes que se mostrarán en cada slide del carrusel.
   * Por defecto se muestra 4 imágenes por slide.
   */
  @Input() imgSlide : number = 4

  /**
   * Título que se muestra arriba del carrusel.
   * Cadena de texto simple.
   */
  @Input() title = ""

  /**
   * Texto descriptivo que se muestra debajo del carrusel.
   */
  @Input() text = ""

  /**
   * Getter que retorna la lista de imágenes dividida en grupos (chunks)
   * con tamaño igual a imgSlide. Esto facilita iterar en el template
   * para crear cada slide con un número fijo de imágenes.
   *
   * Ejemplo:
   * imglist = ['img1','img2','img3','img4','img5']
   * imgSlide = 2
   * chunkedImages = [['img1','img2'], ['img3','img4'], ['img5']]
   */
  get chunkedImages() {
    const chunks = [];
    for (let i = 0; i < this.imglist.length; i += this.imgSlide) {
      chunks.push(this.imglist.slice(i, i + this.imgSlide));
    }
    return chunks;
  }

  // Constructor vacío, sin lógica adicional.
  constructor(){}
}
