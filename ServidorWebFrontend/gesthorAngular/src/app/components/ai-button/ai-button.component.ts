import { Component } from '@angular/core';
import { ChatserviceService } from '../../services/chatservice.service';

@Component({
  selector: 'app-ai-button',
  standalone: false,
  templateUrl: './ai-button.component.html',
  styleUrl: './ai-button.component.css'
})
export class AiButtonComponent {

  constructor(private chatService : ChatserviceService){}

  toggleChat(): void {
    this.chatService.toggle();
  }
}
