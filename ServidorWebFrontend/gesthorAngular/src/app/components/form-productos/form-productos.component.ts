/**
 * FormProductosComponent
 *
 * Componente Angular para gestionar un formulario dinámico de productos.
 * Permite añadir, editar y eliminar productos de una lista, así como confirmar
 * la lista final emitida hacia el componente padre.
 *
 * Características principales:
 * - Entrada (@Input) de lista de productos para mostrar y editar.
 * - Salida (@Output) que emite la lista confirmada de productos.
 * - Manejo interno de un producto actual para crear o editar.
 * - Soporte para categorías múltiples, ingresadas como texto separado por comas.
 * - Renderizado de la lista de productos con botones para editar y borrar.
 *
 * Uso:
 * <app-form-productos
 *   [productos]="arrayDeProductos"
 *   (productosConfirmados)="manejarConfirmacion($event)">
 * </app-form-productos>
 */

import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Producto } from '../../models/models/models.component';

@Component({
  selector: 'app-form-productos',
  standalone: false,
  template: `
  <!-- Contenedor principal con estilos Bootstrap para presentación -->
  <div class="card bg-dark text-light mb-4 p-3 rounded shadow-sm">

    <!-- Título del formulario -->
    <h5 class="card-title d-flex justify-content-between align-items-center">
      Productos
    </h5>

    <!-- Campos de entrada para crear/editar un producto -->
    <div class="mb-3 row">

      <!-- Campo Nombre producto -->
      <div class="col-md-3">
        <label class="form-label">Nombre</label>
        <input
          class="form-control bg-secondary text-light border-0"
          [(ngModel)]="productoActual.nombre"
          placeholder="Ej: Pan"
        />
      </div>

      <!-- Campo Categorías (texto separado por comas) -->
      <div class="col-md-3">
        <label class="form-label">Categorías</label>
        <input
          class="form-control bg-secondary text-light border-0"
          [(ngModel)]="categoriaTexto"
          placeholder="Ej: Alimentación, Panadería"
        />
      </div>

      <!-- Campo Cantidad producto -->
      <div class="col-md-3">
        <label class="form-label">Cantidad</label>
        <input
          type="number"
          class="form-control bg-secondary text-light border-0"
          [(ngModel)]="productoActual.cantidad"
          placeholder="Ej: 2"
        />
      </div>

      <!-- Campo Precio producto en euros -->
      <div class="col-md-3">
        <label class="form-label">Precio (€)</label>
        <input
          type="number"
          class="form-control bg-secondary text-light border-0"
          [(ngModel)]="productoActual.precio"
          placeholder="Ej: 1.50"
        />
      </div>

      <!-- Botón para añadir o editar producto -->
      <div class="col-md-3">
        <button
          class="btn btn-success md-10 mt-3 w-100"
          (click)="agregarProducto()"
        >
          Añadir/Editar
        </button>
      </div>
    </div>

    <!-- Lista dinámica de productos actuales -->
    <ul class="list-group list-group-flush">
      <li
        *ngFor="let p of productos; let i = index"
        class="list-group-item bg-secondary d-flex justify-content-between align-items-center"
        style="color: aliceblue;"
      >
        <div>
          <!-- Muestra nombre, cantidad y precio -->
          <strong>{{ p.nombre }}</strong> - {{ p.cantidad }} × {{ p.precio }}€<br />
          <!-- Muestra categorías separadas por coma -->
          <small>Categorías: {{ p.categorias?.join(', ') }}</small>
        </div>
        <div>
          <!-- Botón para editar el producto -->
          <button class="btn btn-light btn-sm me-2" (click)="editarProducto(i)">Editar</button>
          <!-- Botón para eliminar el producto -->
          <button class="btn btn-danger btn-sm" (click)="eliminarProducto(i)">Borrar</button>
        </div>
      </li>
    </ul>

    <!-- Botón para confirmar y emitir la lista final -->
    <div class="text-end mt-3">
      <button class="btn btn-primary" (click)="confirmarLista()">Confirmar productos</button>
    </div>
  </div>
  `
})
export class FormProductosComponent {

  /**
   * Lista de productos recibida desde el componente padre.
   * Tipo: Array de Producto.
   */
  @Input() productos: Producto[] = [];

  /**
   * Emite la lista de productos cuando el usuario confirma.
   * Tipo: EventEmitter con payload Producto[].
   */
  @Output() productosConfirmados = new EventEmitter<Producto[]>();

  /**
   * Objeto que representa el producto actualmente en edición o creación.
   */
  productoActual: Producto = this.resetProducto();

  /**
   * Cadena de texto para manejar categorías como texto separado por comas.
   */
  categoriaTexto = '';

  /**
   * Índice del producto que se está editando.
   * null indica que no se está editando ninguno, sino añadiendo uno nuevo.
   */
  editIndex: number | null = null;

  /**
   * Resetea y retorna un producto vacío con valores por defecto.
   * @returns Producto inicializado vacío
   */
  resetProducto(): Producto {
    return { nombre: '', categorias: [], cantidad: 1, precio: 0 };
  }

  /**
   * Añade un nuevo producto o edita el producto existente en la lista.
   * Convierte la cadena de categorías en un array filtrando entradas vacías.
   * Resetea el formulario después de la operación.
   */
  agregarProducto() {
    const categoriasArray = this.categoriaTexto
      .split(',')
      .map(c => c.trim())
      .filter(c => c !== '');

    if (this.editIndex !== null) {
      // Editar producto existente
      this.productos[this.editIndex] = {
        ...this.productoActual,
        categorias: categoriasArray
      };
      this.editIndex = null; // Salir modo edición
    } else {
      // Añadir nuevo producto
      this.productos.push({
        ...this.productoActual,
        categorias: categoriasArray
      });
    }

    // Limpiar formulario
    this.productoActual = this.resetProducto();
    this.categoriaTexto = '';
  }

  /**
   * Carga un producto en el formulario para editarlo.
   * @param index Índice del producto en la lista
   */
  editarProducto(index: number) {
    this.productoActual = { ...this.productos[index] };
    this.categoriaTexto = this.productoActual.categorias?.join(', ') || '';
    this.editIndex = index;
  }

  /**
   * Elimina un producto de la lista.
   * Si se estaba editando ese producto, resetea el formulario.
   * @param index Índice del producto a eliminar
   */
  eliminarProducto(index: number) {
    this.productos.splice(index, 1);

    if (this.editIndex === index) {
      this.productoActual = this.resetProducto();
      this.editIndex = null;
    }
  }

  /**
   * Emite la lista actual de productos al componente padre.
   * Se ejecuta cuando el usuario confirma la lista.
   */
  confirmarLista() {
    this.productosConfirmados.emit(this.productos);
  }
}
