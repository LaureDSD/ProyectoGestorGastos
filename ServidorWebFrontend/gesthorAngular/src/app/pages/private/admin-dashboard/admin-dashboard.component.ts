import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: false,
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent {
  token: string | null = null;

  userl: any;

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.token = this.authService.getToken();
    this.authService.getCurrentUser();
  }

}
