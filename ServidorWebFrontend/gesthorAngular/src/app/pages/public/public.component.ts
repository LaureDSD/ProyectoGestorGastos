import { Route, Router } from '@angular/router';
import { ApiserviceService } from './../../services/apiservice.service';
import { Component, HostListener, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-public',
  standalone: false,
  templateUrl: './public.component.html'
})
export class PublicComponent implements OnInit {
  constructor(private router: Router) { }
  ngOnInit(): void {
    this.router.navigate(['/public/home']);
  }
}
