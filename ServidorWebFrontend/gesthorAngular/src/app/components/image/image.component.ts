import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-image',
  standalone: false,
  template: `
    <!-- Sección con fondo degradado y texto centrado -->
    <section style="background: linear-gradient(to bottom, #000000 40%, #ffffff 100%); color: #000; padding: 80px 20px 40px; text-align: center;">
      <!-- Contenedor para centrar la imagen con sombra y bordes redondeados -->
      <div style="display: flex; justify-content: center;">
        <div style="opacity: 80%; max-width: 95%; height: auto; border-radius: 12px; box-shadow: 0 0 20px rgba(0,0,0,0.6); overflow: hidden;">
          <!-- Imagen con binding dinámico de src y alt -->
          <img [src]="src" [alt]="alt" style="width: 100%; display: block;">
        </div>
      </div>

      <!-- Texto descriptivo debajo de la imagen -->
      <p style="margin-top: 20px; font-size: 1.1rem;">
        {{ text }}
      </p>
    </section>
  `
})
export class ImageComponent {
  // URL de la imagen a mostrar
  @Input() src = "";

  // Texto alternativo para la imagen
  @Input() alt = "";

  // Texto descriptivo o pie de foto que aparece debajo de la imagen
  @Input() text = "";
}
