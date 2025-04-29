
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html'

})
export class DashboardComponent implements OnInit {
  token: string | null = null;

  userl: any;

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.token = this.authService.getToken();

    this.authService.getCurrentUser().subscribe({
      next: (user) => {
        this.userl = user
      },
      error: (error) => {
        console.error('Error obteniendo el usuario:', error);
      }
    });
  }

}
