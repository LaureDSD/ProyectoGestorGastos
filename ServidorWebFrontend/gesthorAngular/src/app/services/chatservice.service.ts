import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

interface ChatMessage {
  role: 'user' | 'bot';
  content: string;
  timestamp: Date;
}

@Injectable({
  providedIn: 'root'
})
export class ChatserviceService {

  private _visible = new BehaviorSubject<boolean>(false);
  private _messages = new BehaviorSubject<ChatMessage[]>([]);

  visible$ = this._visible.asObservable();
  messages$ = this._messages.asObservable();

  toggle() {
    this._visible.next(!this._visible.value);
  }

  open() {
    this._visible.next(true);
  }

  close() {
    this._visible.next(false);
  }

  sendMessage(message: string) {
    const current = this._messages.value;
    const newMessage: ChatMessage = {
      role: 'user',
      content: message,
      timestamp: new Date()
    };
    this._messages.next([...current, newMessage]);

    // Simulación de respuesta (aquí puedes usar HttpClient a tu backend)
    setTimeout(() => {
      const reply: ChatMessage = {
        role: 'bot',
        content: 'Esta es una respuesta automática.',
        timestamp: new Date()
      };
      this._messages.next([...this._messages.value, reply]);
    }, 1000);
  }

  clearChat() {
    this._messages.next([]);
  }
}
