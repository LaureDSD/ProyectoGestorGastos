import { Component, HostListener } from '@angular/core';

/**
 * Componente BackButtonComponent
 *
 * Este componente muestra un botón flotante para "volver arriba" cuando el usuario
 * ha hecho scroll hacia abajo más de 300 píxeles en la página.
 *
 * Funcionalidad:
 * - El botón solo se muestra cuando la ventana ha sido desplazada verticalmente más de 300px.
 * - Al hacer clic en el botón, la página se desplaza suavemente hacia arriba (top: 0).
 *
 * Estilos:
 * - Botón circular fijo en la esquina inferior derecha.
 * - Animación suave al pasar el cursor (hover) con aumento de tamaño y cambio de color.
 */
@Component({
  selector: 'app-back-button',  // Selector para uso en templates
  standalone: false,             // No es standalone (depende de módulo)
  template:`
    <!-- Botón solo visible si showButton es true -->
    <button
      *ngIf="showButton"
      class="scroll-top-btn"
      title="Volver arriba"
      (click)="scrollToTop()"
    >
      ⮝
    </button>
  `,
  styles:`
    /* Estilos para el botón flotante de volver arriba */
    .scroll-top-btn {
      position: fixed;
      bottom: 20px;
      right: 20px;
      z-index: 1000;
      border-radius: 50%;
      width: 50px;
      height: 50px;
      font-size: 24px;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
      background-color: #007bff;
      color: white;
      transition: transform 0.3s ease, opacity 0.3s ease;
    }

    /* Efecto hover para el botón: agrandar y oscurecer color */
    .scroll-top-btn:hover {
      transform: scale(1.1);
      background-color: #0056b3;
    }
  `
})
export class BackButtonComponent {

  /**
   * Controla la visibilidad del botón.
   * Es true cuando la página está scrolleada más de 300px.
   */
  showButton = false;

  /**
   * Listener para evento scroll de la ventana.
   * Actualiza showButton en función de la posición vertical del scroll.
   */
  @HostListener('window:scroll', [])
  onWindowScroll() {
    this.showButton = window.scrollY > 300;
  }

  /**
   * Método que desplaza la ventana suavemente al inicio (top: 0).
   * Se ejecuta al hacer clic en el botón.
   */
  scrollToTop(): void {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

}
