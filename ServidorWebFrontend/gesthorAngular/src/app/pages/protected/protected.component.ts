import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-protected',
  standalone: false,
  templateUrl: './protected.component.html'
})
export class ProtectedComponent implements OnInit {
  constructor(private router: Router) { }
  ngOnInit(): void {
    //this.router.navigate(['/login']);
  }
}