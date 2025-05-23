import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-hero', // Selector para usar este componente en templates HTML
  standalone: false,    // No es un componente independiente, se declara en algún módulo Angular
  template: `
    <!-- Sección principal del héroe con margen superior -->
    <section class="hero mt-5">
      <!-- Imagen del logo con binding dinámico al input 'logo' -->
      <img [src]="logo" height="150px">
      <!-- Título principal con binding dinámico al input 'title' -->
      <h1>{{title}}</h1>
      <!-- Texto descriptivo con binding dinámico al input 'text' -->
      <p>{{text}}</p>
    </section>
  `,
  styles: `
    /* Estilos para el contenedor principal del héroe */
    .hero {
      text-align: center;
      padding: 80px 20px 60px;
      background: linear-gradient(145deg, #000000, #000); /* Fondo degradado oscuro */
      animation: fadeIn 1s ease-out; /* Animación de aparición */
    }

    /* Estilos para el título h1 */
    .hero h1 {
      font-size: 3.5rem;
      font-weight: bold;
      text-shadow: 2px 2px #444; /* Sombra para mejorar legibilidad */
      animation: slideUp 1s ease-out; /* Animación de desplazamiento hacia arriba */
    }

    /* Estilos para el párrafo descriptivo */
    .hero p {
      font-size: 1.3rem;
      max-width: 800px; /* Ancho máximo para mantener lectura cómoda */
      margin: 1rem auto; /* Centramos el párrafo horizontalmente */
    }

    /* Definición de animación fadeIn para opacidad */
    @keyframes fadeIn {
      0% { opacity: 0; }
      100% { opacity: 1; }
    }

    /* Definición de animación slideUp para desplazar y aparecer */
    @keyframes slideUp {
      0% { transform: translateY(20px); opacity: 0; }
      100% { transform: translateY(0); opacity: 1; }
    }
  `
})
export class HeroComponent {
  // Input para el título del héroe, texto principal visible
  @Input() title = "";

  // Input para el texto descriptivo debajo del título
  @Input() text = "";

  // Input para la URL o path del logo que aparece arriba del título
  @Input() logo = "";
}
