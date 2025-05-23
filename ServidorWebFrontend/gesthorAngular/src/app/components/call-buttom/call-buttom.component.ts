import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';

/**
 * Componente CallButtomComponent
 *
 * Componente de llamada a la acción (Call to Action) que muestra un título,
 * un texto descriptivo y un botón con estilos variables según el input.
 *
 * Inputs:
 * - title: título que se muestra en la sección principal.
 * - text: texto descriptivo que aparece debajo del título.
 * - buttomtext: texto que se muestra dentro del botón.
 * - buttomstyle: número que determina el estilo visual del botón (1 o 2).
 * - buttonredirect: ruta a la que se navegará cuando se haga clic en el botón.
 *
 * Funcionalidad:
 * - Renderiza dos tipos diferentes de botón basados en el valor de buttomstyle.
 * - Al hacer clic en el botón, navega a la ruta especificada en buttonredirect.
 */
@Component({
  selector: 'app-call-buttom',   // Selector para usar el componente en plantillas
  standalone: false,              // No es un componente standalone
  template:`
  <!-- Sección de llamada a la acción centrada con padding -->
  <section style="padding: 5rem 2rem; text-align: center;">
    <!-- Título grande -->
    <h2 style="font-size: 2rem; margin-bottom: 1rem;">{{title}}</h2>
    <!-- Texto descriptivo con ancho máximo para mejor lectura -->
    <p style="max-width: 600px; margin: 0 auto;">{{text}}</p>

    <!-- Botón estilo 1: fondo claro, texto oscuro -->
    <button
      (click)="redirectTo(buttonredirect)"
      *ngIf="buttomstyle==1"
      style="margin-top: 2rem; padding: 1rem 2rem; background-color: #f0f0f0; color: #000; border: none; border-radius: 10px; font-weight: bold;">
      {{buttomtext}}
    </button>

    <!-- Botón estilo 2: fondo oscuro, borde claro, texto blanco -->
    <button
      (click)="redirectTo(buttonredirect)"
      *ngIf="buttomstyle==2"
      style="margin-top: 20px; background-color: #000; color: #ffffff; border: 2px solid #f3f3f3; padding: 12px 24px; border-radius: 30px; font-size: 1rem; cursor: pointer;">
      {{buttomtext}}
    </button>
  </section>
  `
})
export class CallButtomComponent {
  /** Título principal que se muestra en la sección */
  @Input() title = ""

  /** Texto descriptivo que aparece debajo del título */
  @Input() text = ""

  /** Texto que aparece dentro del botón */
  @Input() buttomtext = ""

  /** Estilo del botón (1 o 2) que determina la apariencia */
  @Input() buttomstyle = 1

  /** Ruta a la que se navegará al hacer clic en el botón */
  @Input() buttonredirect = ""

  /**
   * Inyección del servicio Router para navegación programática.
   */
  constructor(private router : Router){}

  /**
   * Método que redirige a la ruta recibida como parámetro.
   * @param text Ruta de destino para navegación.
   */
  redirectTo(text : string){
    this.router.navigate([text])
  }
}
