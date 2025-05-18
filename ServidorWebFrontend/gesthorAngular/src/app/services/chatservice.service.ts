import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environments/environment';

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

  constructor(private http: HttpClient) {}

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
    const userMessage: ChatMessage = {
      role: 'user',
      content: message,
      timestamp: new Date()
    };

    this._messages.next([...current, userMessage]);

    this.http.post(`${environment.apiUrl}/api/chat/message`, message, { responseType: 'text' }).subscribe({
      next: (response: string) => {
        const botMessage: ChatMessage = {
          role: 'bot',
          content: response,
          timestamp: new Date()
        };
        this._messages.next([...this._messages.value, botMessage]);
      },
      error: (err) => {
        const errorMessage: ChatMessage = {
          role: 'bot',
          content: 'Error al contactar con el asistente.',
          timestamp: new Date()
        };
        this._messages.next([...this._messages.value, errorMessage]);
        console.error(err);
      }
    });
  }

  clearChat() {
    this._messages.next([]);
  }
}
