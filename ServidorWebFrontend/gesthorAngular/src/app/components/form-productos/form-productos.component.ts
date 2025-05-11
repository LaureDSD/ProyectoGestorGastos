import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Producto } from '../../models/models/models.component';

@Component({
  selector: 'app-form-productos',
  standalone: false,
  templateUrl: './form-productos.component.html',
  styleUrl: './form-productos.component.css'
})
export class FormProductosComponent {
  @Input() productos: Producto[] = [];
  @Output() productosConfirmados = new EventEmitter<Producto[]>();

  productoActual: Producto = this.resetProducto();
  categoriaTexto = '';

  editIndex: number | null = null;

  resetProducto(): Producto {
    return { nombre: '', categorias: [], cantidad: 1, precio: 0 };
  }

  agregarProducto() {
  const categoriasArray = this.categoriaTexto.split(',').map(c => c.trim()).filter(c => c !== '');
  if (this.editIndex !== null) {
    this.productos[this.editIndex] = {
      ...this.productoActual,
      categorias: categoriasArray
    };
    this.editIndex = null;
  } else {
    this.productos.push({
      ...this.productoActual,
      categorias: categoriasArray
    });
  }

  this.productoActual = this.resetProducto();
  this.categoriaTexto = '';
}

editarProducto(index: number) {
  this.productoActual = { ...this.productos[index] };
  this.categoriaTexto = this.productoActual.categorias.join(', ');
  this.editIndex = index;
}

  eliminarProducto(index: number) {
    this.productos.splice(index, 1);
    if (this.editIndex === index) {
      this.productoActual = this.resetProducto();
      this.editIndex = null;
    }
  }

  confirmarLista() {
    this.productosConfirmados.emit(this.productos);
  }
}
