import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-tabvar',
  standalone: false,
  templateUrl: './tabvar.component.html',
  styleUrl: './tabvar.component.css'
})
export class TabvarComponent {


  wheelActive = false;

  constructor(private router: Router) {}

  toggleWheel(): void {
    this.wheelActive = !this.wheelActive;
  }

  navigateTo(route: string): void {
    this.router.navigate([route]);
    this.wheelActive = false;
  }

}
