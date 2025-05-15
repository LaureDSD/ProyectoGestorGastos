import { Component } from '@angular/core';

@Component({
  selector: 'app-models',
  standalone: false,
  template: ``
})
export class ModelsComponent {}

export interface Producto {
  nombre: string;
  categorias?: string[];
  cantidad: number;
  precio: number;
  subtotal?: number;
}
