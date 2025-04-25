import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: false,             
  template: `
  <div class="container mt-5">
    <h2>Dashboard</h2>
    <p>¡Autenticado exitosamente!</p>
  </div>`
})
export class DashboardComponent { }