import { Component } from '@angular/core';
import { ChatserviceService } from '../../services/chatservice.service';
import {  ElementRef, ViewChild, AfterViewChecked } from '@angular/core';

@Component({
  selector: 'app-chat',
  standalone: false,
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.css'
})
export class ChatComponent {
  isVisible = false;
  messages$;
  input = '';
  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;

  constructor(private chatService: ChatserviceService) {
    this.messages$ = chatService.messages$;
    this.chatService.visible$.subscribe(v => this.isVisible = v);
  }

  sendMessage() {
    if (this.input.trim()) {
      this.chatService.sendMessage(this.input.trim());
      this.input = '';
    }
  }

  closeChat() {
    this.chatService.close();
  }

  ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

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


