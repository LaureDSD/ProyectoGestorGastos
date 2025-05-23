/**
 * Componente Angular: ChatComponent
 * ---------------------------------
 * Este componente implementa una interfaz de chat tipo chatbot para la aplicación.
 * Soporta dos modos de visualización según el tamaño de pantalla:
 *  - Escritorio: una barra lateral fija en la parte derecha (visible en pantallas md en adelante).
 *  - Móvil: una vista de pantalla completa (visible en pantallas pequeñas).
 *
 * Funcionalidades:
 * - Mostrar una lista de mensajes intercambiados entre el usuario y el chatbot.
 * - Diferenciar visualmente mensajes del usuario y del bot mediante estilos.
 * - Permitir al usuario escribir y enviar mensajes.
 * - Controlar la visibilidad del chat desde un servicio centralizado.
 * - Mantener el scroll siempre al final para ver el último mensaje.
 * - Botón para minimizar o cerrar el chat.
 *
 * Selector: 'app-chat'
 *
 * Propiedades:
 * - isVisible: Booleano que indica si el chat está visible o minimizado.
 * - messages$: Observable que contiene la lista de mensajes del chat, provisto por el servicio.
 * - input: Cadena que enlaza con el input para capturar el texto que el usuario escribe.
 *
 * Decoradores:
 * - @ViewChild('messagesContainer'): Referencia al contenedor de mensajes para controlar el scroll.
 *
 * Métodos:
 * - constructor: Inyecta el servicio ChatserviceService, inicializa mensajes y suscribe la visibilidad.
 * - sendMessage(): Envía el mensaje escrito al servicio si no está vacío y limpia el input.
 * - closeChat(): Llama al método del servicio para cerrar/minimizar el chat.
 * - ngAfterViewChecked(): Ciclo de vida Angular que se ejecuta después de actualizar la vista, para asegurar scroll abajo.
 * - scrollToBottom(): Desplaza el contenedor de mensajes al final para mostrar el último mensaje.
 *
 * Plantilla HTML:
 * - Contiene estructura adaptativa con clases Bootstrap para responsividad.
 * - Botón para minimizar el chat.
 * - Uso de directivas Angular *ngIf, *ngFor, [(ngModel)], [ngClass].
 * - Uso de componentes hijos: <app-separator>, <app-ai-button>.
 *
 */

import { Component, ElementRef, ViewChild, AfterViewChecked } from '@angular/core';
import { ChatserviceService } from '../../services/chatservice.service';

@Component({
  selector: 'app-chat',
  standalone: false,
  template: `
  <!-- Escritorio: lateral derecho (oscuro) -->
  <div *ngIf="isVisible" class="d-none d-md-block">
    <div
      class="chat-sidebar position-fixed bottom-0 end-0 text-white shadow"
      style="width: 400px; height: 50vh; z-index: 1050; border-top: 1px solid #444; background-color:rgba(44, 44, 44, 0.856)"
    >
      <div class="p-2 ms-3 border-secondary d-flex justify-content-between">
        <span class="fw-bold ">ChatBot GesThor</span>
        <button class="btn btn-sm btn-outline-light" (click)="closeChat()">Minimizar</button>
      </div>

      <app-separator margin="0rem 0"></app-separator>

      <div #messagesContainer class="p-3 overflow-auto" style="height: calc(50vh - 120px);">
        <div *ngFor="let msg of messages$ | async" [ngClass]="{'text-end': msg.role === 'user'}">
          <div
            [ngClass]="msg.role === 'user' ? 'bg-primary text-white' : 'bg-secondary text-white'"
            class="d-inline-block rounded p-2 mb-2"
          >
            {{ msg.content }}
          </div>
        </div>
      </div>

      <app-separator margin="0rem 0"></app-separator>

      <div class="p-2 border-secondary input-group">
        <input [(ngModel)]="input" class="form-control text-black border-secondary" placeholder="Escribe un mensaje...">
        <button class="btn btn-primary" (click)="sendMessage()">Enviar</button>
      </div>
    </div>
  </div>

  <app-ai-button *ngIf="!isVisible" class="d-none d-md-block"></app-ai-button>

  <!-- Móvil: pantalla completa -->
  <div *ngIf="isVisible" class="d-block d-md-none">
    <div class="position-fixed top-0 start-0 text-white w-100 shadow pt-3" style="height: 100vh; z-index: 1010; background-color:rgba(44, 44, 44, 0.856)">
      <div class="p-2 ms-3 border-secondary d-flex justify-content-between">
        <span class="fw-bold">ChatBot GesThor</span>
        <button class="btn btn-sm btn-outline-light" (click)="closeChat()">Minimizar</button>
      </div>

      <app-separator margin="0rem 0"></app-separator>

      <div #messagesContainer class="p-3 overflow-auto" style="height: calc(100vh - 200px);">
        <div *ngFor="let msg of messages$ | async" [ngClass]="{'text-end': msg.role === 'user'}">
          <div
            [ngClass]="msg.role === 'user' ? 'bg-primary text-white' : 'bg-secondary text-white'"
            class="d-inline-block rounded p-2 mb-2"
          >
            {{ msg.content }}
          </div>
        </div>
      </div>

      <app-separator margin="0rem 0"></app-separator>

      <div class="p-2 border-secondary input-group">
        <input [(ngModel)]="input" class="form-control text-black border-secondary" placeholder="Escribe un mensaje...">
        <button class="btn btn-primary" (click)="sendMessage()">Enviar</button>
      </div>
    </div>
  </div>
  `
})
export class ChatComponent implements AfterViewChecked {
  /** Controla la visibilidad del chat */
  isVisible = false;

  /** Observable con los mensajes del chat */
  messages$;

  /** Texto actual escrito por el usuario */
  input = '';

  /** Referencia al contenedor de mensajes para controlar el scroll */
  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;

  /**
   * Constructor que inyecta el servicio de chat y suscribe la visibilidad y mensajes.
   * @param chatService Servicio que maneja la lógica y estado del chat.
   */
  constructor(private chatService: ChatserviceService) {
    this.messages$ = chatService.messages$;
    this.chatService.visible$.subscribe(v => this.isVisible = v);
  }

  /**
   * Envía el mensaje escrito al servicio si no está vacío y limpia el input.
   */
  sendMessage() {
    if (this.input.trim()) {
      this.chatService.sendMessage(this.input.trim());
      this.input = '';
    }
  }

  /**
   * Cierra o minimiza el chat mediante el servicio.
   */
  closeChat() {
    this.chatService.close();
  }

  /**
   * Lifecycle hook que se ejecuta después de que Angular haya actualizado la vista.
   * Se usa para asegurar que el scroll siempre esté al final.
   */
  ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

  /**
   * Desplaza el contenedor de mensajes hasta el final para mostrar el último mensaje.
   * Maneja posibles errores en el scroll.
   */
  private scrollToBottom(): void {
    if (!this.messagesContainer) return;

    try {
      this.messagesContainer.nativeElement.scrollTop =
        this.messagesContainer.nativeElement.scrollHeight;
    } catch (err) {
      console.error('Scroll error:', err);
    }
  }
}
