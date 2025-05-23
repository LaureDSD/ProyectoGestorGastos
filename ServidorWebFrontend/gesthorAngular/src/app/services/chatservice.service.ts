import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environments/environment';
import { ModelsComponent, ChatMessage } from '../models/models/models.component';

/**
 * Servicio Angular que maneja la lógica del chat entre el usuario y un asistente (bot).
 * Provee métodos para controlar la visibilidad del chat, enviar mensajes y mantener
 * el historial de mensajes utilizando observables reactivas (BehaviorSubject).
 */
@Injectable({
  providedIn: 'root'  // Permite que este servicio sea inyectado globalmente en toda la app
})
export class ChatserviceService {

  // Estado privado para controlar la visibilidad del componente chat.
  // BehaviorSubject permite emitir el valor actual y nuevas actualizaciones.
  private _visible = new BehaviorSubject<boolean>(false);

  // Estado privado que mantiene el historial de mensajes del chat.
  private _messages = new BehaviorSubject<ChatMessage[]>([]);

  // Observable público para suscribirse a cambios de visibilidad.
  visible$ = this._visible.asObservable();

  // Observable público para suscribirse a cambios en la lista de mensajes.
  messages$ = this._messages.asObservable();

  /**
   * Inyecta HttpClient para realizar peticiones HTTP al backend.
   * @param http Cliente HTTP para comunicación con API.
   */
  constructor(private http: HttpClient) {}

  /**
   * Alterna la visibilidad del chat.
   * Si estaba abierto, lo cierra; si estaba cerrado, lo abre.
   */
  toggle() {
    this._visible.next(!this._visible.value);
  }

  /**
   * Abre el chat estableciendo su visibilidad a true.
   */
  open() {
    this._visible.next(true);
  }

  /**
   * Cierra el chat estableciendo su visibilidad a false.
   */
  close() {
    this._visible.next(false);
  }

  /**
   * Envía un mensaje del usuario al backend y actualiza el historial de mensajes.
   * - Añade el mensaje del usuario a la lista.
   * - Realiza una petición POST con el texto al endpoint de chat.
   * - Al recibir la respuesta del bot, añade el mensaje del bot al historial.
   * - En caso de error, añade un mensaje de error del bot y registra en consola.
   * @param message Texto del mensaje que envía el usuario.
   */
  sendMessage(message: string) {
    // Obtener mensajes actuales para agregarlos junto con el nuevo.
    const current = this._messages.value;

    // Crear objeto mensaje para el usuario.
    const userMessage: ChatMessage = {
      role: 'user',
      content: message,
      timestamp: new Date()
    };

    // Emitir la nueva lista de mensajes incluyendo el del usuario.
    this._messages.next([...current, userMessage]);

    // Enviar el mensaje al backend via HTTP POST y manejar la respuesta.
    this.http.post(`${environment.apiUrl}/api/chat/message`, message, { responseType: 'text' }).subscribe({
      next: (response: string) => {
        // Crear mensaje del bot con la respuesta recibida.
        const botMessage: ChatMessage = {
          role: 'bot',
          content: response,
          timestamp: new Date()
        };
        // Añadir mensaje del bot a la lista actualizada.
        this._messages.next([...this._messages.value, botMessage]);
      },
      error: (err) => {
        // En caso de error, crear mensaje informativo del bot.
        const errorMessage: ChatMessage = {
          role: 'bot',
          content: 'Error al contactar con el asistente.',
          timestamp: new Date()
        };
        // Añadir mensaje de error al historial.
        this._messages.next([...this._messages.value, errorMessage]);
        // Registrar error en la consola para depuración.
        console.error(err);
      }
    });
  }

  /**
   * Limpia el historial de mensajes del chat, dejando la lista vacía.
   */
  clearChat() {
    this._messages.next([]);
  }
}
