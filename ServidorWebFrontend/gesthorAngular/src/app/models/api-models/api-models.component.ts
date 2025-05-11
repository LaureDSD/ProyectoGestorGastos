import { Component } from '@angular/core';

@Component({
  selector: 'app-api-models',
  standalone: false,
  template: ''
})
export class ApiModelsComponent {}

export interface UserDto {
  id: number;
  name: string;
  username: string;
  email: string;
  phone: string;
  address: string;
  imageUrl: string;
  server: string;
  role: string;
  active: boolean;
  fv2: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface LoginAttempt {
  id: number,
  username: string;
  attemptTime: string;
  success: boolean;
}

export interface Category {
  id: number,
  name: string;
  description: string;
  iva: number;
}

export interface ServerInfo {
  name: string;
  users: number,
  spenses: number;
  active: boolean;
}

export interface Ticket {
  spentId: number;
  store: string;
  fechaCompra: Date;
  name: string;
  description: string;
  total: number;
  iva: number;
  icon: string;
  productsJSON: string;
}
