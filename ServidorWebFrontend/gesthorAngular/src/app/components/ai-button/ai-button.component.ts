import { Component } from '@angular/core';
import { ChatserviceService } from '../../services/chatservice.service';

/**
 * Componente AiButtonComponent
 *
 * Botón flotante que representa un ícono de inteligencia artificial (AI).
 * Al hacer clic, activa o desactiva el chat mediante el servicio ChatserviceService.
 *
 * Funcionalidad:
 * - Muestra un botón circular fijo en la esquina inferior derecha con un ícono AI.
 * - Al hacer clic, invoca el método toggle() del servicio de chat para abrir o cerrar el chat.
 *
 * Estilos:
 * - Botón con fondo oscuro, sombra, y animación de escala al pasar el cursor.
 */
@Component({
  selector: 'app-ai-button',   // Selector para usar el componente en templates
  standalone: false,           // No es standalone (depende de módulo)
  template:`
    <!-- Botón flotante con imagen de ícono AI -->
    <button
      class="scroll-top-btn"
      title="Ai icon by Icons8"
      (click)="toggleChat()">
      <img style="width: 27px;" src="/icon/icons8-ai-50.png" alt="Icono AI">
    </button>
  `,
  styles:`
    /* Estilos del botón flotante */
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
      background-color: #383838;
      color: white;
      transition: transform 0.3s ease, opacity 0.3s ease;
    }

    /* Efecto hover: escala y cambio de color */
    .scroll-top-btn:hover {
      transform: scale(1.1);
      background-color: #4a6072;
    }
  `
})
export class AiButtonComponent {

  /**
   * Inyección del servicio ChatserviceService para controlar el chat.
   * @param chatService Servicio para manejar la visibilidad del chat.
   */
  constructor(private chatService : ChatserviceService){}

  /**
   * Método que alterna el estado de visibilidad del chat.
   * Llama al método toggle() del servicio chatService.
   */
  toggleChat(): void {
    this.chatService.toggle();
  }
}
