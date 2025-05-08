import { ChatserviceService } from './../../services/chatservice.service';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-tabvar',
  standalone: false,
  templateUrl: './tabvar.component.html',
  styleUrl: './tabvar.component.css'
})
export class TabvarComponent {

  constructor(private router: Router , private chatService : ChatserviceService) {}

  navigateTo(route: string): void {
    this.router.navigate([route]);
  }

  toggleChat(): void {
    this.chatService.toggle();
  }

}
