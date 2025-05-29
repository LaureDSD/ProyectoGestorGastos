import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { SpentFullDto } from '../../models/api-models/api-models.component';
import { ChartConfiguration } from 'chart.js';
import jsPDF from 'jspdf';
import autoTable, { CellInput } from 'jspdf-autotable';

@Component({
  selector: 'app-gasto-detalle',  // Selector para insertar este componente en otros templates
  standalone: false,
  template: `
  <div class="m-4">
    <!-- Muestra la descripción e IVA si están presentes -->
    <div class="mb-4">
      <p *ngIf="gasto.description"><strong>Descripción:</strong> {{gasto.description}}</p>
      <p *ngIf="gasto.iva"><strong>IVA</strong>: {{gasto.iva}}%</p>

      <!-- Botones para exportar datos a CSV o PDF -->
      <div class="text-start mt-3">
        <button class="btn btn-sm btn-outline-light me-2" (click)="exportToCSV()">Descargar CSV</button>
        <button class="btn btn-sm btn-outline-light" (click)="exportToPDF()">Descargar PDF</button>
      </div>
    </div>

    <!-- Sección específica para gastos tipo 'TICKET' con dos gráficos -->
    <div *ngIf="gasto.typeExpense === 'TICKET'">
      <div class="row">

        <div class="col-md-6">
          <h5>Gráfico: Producto</h5>
          <canvas baseChart [data]="chartProductos" [type]="'doughnut'"></canvas>
        </div>

        <div *ngIf="(chartCategorias?.datasets?.[0]?.data?.length ?? 0) > 0" class="col-md-6">
          <h5>Gráfico: Categoría</h5>
          <canvas baseChart [data]="chartCategorias" [type]="'pie'"></canvas>
        </div>

      </div>
    </div>

    <!-- Sección específica para gastos tipo 'SUBSCRIPCION' -->
    <div *ngIf="gasto.typeExpense === 'SUBSCRIPCION'">
      <p><strong>Acumulado</strong>: {{gasto.accumulate}}€</p>
      <p>Fecha de renovación: {{ fechaRenovacion | date }}</p>

      <!-- Barra de progreso que indica días restantes para la renovación -->
      <div class="progress">
        <div
          class="progress-bar"
          role="progressbar"
          [style.width.%]="((totalDias - diasRestantes) / totalDias) * 100"
          [attr.aria-valuenow]="totalDias - diasRestantes"
          aria-valuemin="0"
          [attr.aria-valuemax]="totalDias"
        >
          {{ diasRestantes }} días restantes
        </div>
      </div>
    </div>
  </div>
  `
})
export class GastoDetalleComponent implements OnChanges {
  // Recibe un gasto completo que debe ser mostrado
  @Input() gasto!: SpentFullDto;

  // Array para guardar los productos extraídos del gasto (para gastos tipo TICKET)
  productos: any[] = [];

  // Variables para controlar cálculo del progreso de suscripciones
  totalDias: number = 30;
  diasRestantes: number = 0;
  fechaRenovacion!: Date;

  // Datos para gráficos (doughnut y pie) con Chart.js
  chartProductos: ChartConfiguration<'doughnut'>['data'] = { labels: [], datasets: [] };
  chartCategorias: ChartConfiguration<'pie'>['data'] = { labels: [], datasets: [] };

  /**
   * Detecta cambios en la propiedad 'gasto' y genera contenido actualizado
   */
  ngOnChanges(changes: SimpleChanges) {
    if (changes['gasto'] && this.gasto) {
      this.generarContenido();
    }
  }

  /**
   * Genera el contenido basado en el tipo de gasto
   * - Si es 'TICKET', parsea productos y genera gráficos
   * - Si es 'SUBSCRIPCION', calcula fechas y progreso
   */
  generarContenido() {
    if (this.gasto.typeExpense === 'TICKET') {
      this.productos = this.parseProducts(this.gasto.productsJSON ?? '');
      this.initGraficosTicket();
    } else if (this.gasto.typeExpense === 'SUBSCRIPCION') {
      this.calcularRenovacion();
    }
  }

  /**
   * Intenta parsear el JSON de productos; si falla, retorna array vacío
   */
  parseProducts(json: string | null | undefined): any[] {
    try {
      return json ? JSON.parse(json) : [];
    } catch {
      return [];
    }
  }

  /**
   * Inicializa los datos para los gráficos de tipo 'TICKET'
   * Calcula el total por producto y por categoría para mostrar en doughnut y pie charts
   */
  initGraficosTicket() {
    const porProducto: { [key: string]: number } = {};
    const porCategoria: { [key: string]: number } = {};

    this.productos.forEach(p => {
      const total = p.precio * p.cantidad;
      porProducto[p.nombre] = (porProducto[p.nombre] || 0) + total;

      (p.categorias || []).forEach((cat: string) => {
        porCategoria[cat] = (porCategoria[cat] || 0) + total;
      });
    });

    this.chartProductos = {
      labels: Object.keys(porProducto),
      datasets: [{
        data: Object.values(porProducto),
        backgroundColor: [
          '#66b2ff', '#3399ff', '#2673cc', '#1a4d80', '#003366'
        ],
        label: 'Gasto por producto'
      }]
    };

    this.chartCategorias = {
      labels: Object.keys(porCategoria),
      datasets: [{
        data: Object.values(porCategoria),
        backgroundColor: [
          '#ff6666', '#ff4d4d', '#cc0000', '#800000', '#4d0000'
        ],
        label: 'Gasto por categoría'
      }]
    };
  }

  /**
   * Calcula la fecha de renovación y días restantes para suscripciones
   * Asume período mensual a partir de la fecha de compra
   */
  calcularRenovacion() {
    const compra = new Date(this.gasto.fechaCompra);
    this.fechaRenovacion = new Date(compra);
    this.fechaRenovacion.setMonth(compra.getMonth() + 1);

    const hoy = new Date();
    const diffTotal = this.fechaRenovacion.getTime() - compra.getTime();
    const diffRestante = this.fechaRenovacion.getTime() - hoy.getTime();

    this.totalDias = Math.ceil(diffTotal / (1000 * 60 * 60 * 24)); // Total días del período
    this.diasRestantes = Math.max(0, Math.ceil(diffRestante / (1000 * 60 * 60 * 24))); // Días restantes
  }

  /**
   * Convierte un array de objetos en un string CSV
   * Maneja arrays internos convirtiéndolos a cadenas separadas por coma
   */
  convertToCSV(data: any[]): string {
    if (!data.length) return '';

    const headers = Object.keys(data[0]);
    const rows = data.map(row =>
      headers.map(field => {
        const value = row[field];
        if (Array.isArray(value)) return `"${value.join(', ')}"`; // Array a string
        return `"${value ?? ''}"`;
      }).join(',')
    );

    return headers.join(',') + '\n' + rows.join('\n');
  }

  /**
   * Genera y descarga un archivo CSV con datos del gasto y productos
   */
  exportToCSV() {
    const csvHeaderLines = [
      `Nombre del gasto:,"${this.gasto.name}"`,
      `Descripción:,"${this.gasto.description ?? '-'}"`,
      `Fecha de compra:,"${this.gasto.fechaCompra}"`,
      `Tienda:,"${this.gasto.store ?? '-'}"`,
      `Total:,"${this.gasto.total}€"`,
      `IVA:,"${this.gasto.iva}%"`,
      `Tipo:,"${this.gasto.typeExpense}"`,
      ''
    ].join('\n');

    const csvProductos = this.convertToCSV(this.productos);
    const contenido = csvHeaderLines + '\n' + csvProductos;

    const blob = new Blob([contenido], { type: 'text/csv;charset=utf-8;' });
    const url = window.URL.createObjectURL(blob);

    const a = document.createElement('a');
    a.href = url;
    a.download = `${this.gasto.name}_detalle.csv`;
    a.click();
    window.URL.revokeObjectURL(url);
  }

  /**
   * Genera y descarga un archivo PDF con datos del gasto y tabla de productos
   * Usa jsPDF y jspdf-autotable para crear el documento
   */
  exportToPDF() {
    const doc = new jsPDF();
    let y = 30;

    const img = new Image();
    img.src = '/icon/icon.png';

    doc.setFontSize(40);
    doc.setFont('bold');
    doc.text('GESTHOR', doc.internal.pageSize.getWidth() / 2, y, { align: 'center' });
    y += 10;

    // Espera a que la imagen cargue antes de continuar con el resto
    img.onload = () => {
      doc.addImage(img, 'PNG', 180, 5, 20, 20);
      y += 25;

      // Texto con los datos principales del gasto
      doc.setFontSize(12);
      doc.text(`Nombre del gasto: ${this.gasto.name}`, 14, y); y += 8;
      doc.text(`Descripción: ${this.gasto.description ?? '-'}`, 14, y); y += 8;
      doc.text(`Fecha de compra: ${this.gasto.fechaCompra}`, 14, y); y += 8;
      doc.text(`Tienda: ${this.gasto.store ?? '-'}`, 14, y); y += 8;
      doc.text(`Total: ${this.gasto.total}€`, 14, y); y += 8;
      doc.text(`IVA: ${this.gasto.iva}%`, 14, y); y += 8;
      doc.text(`Tipo: ${this.gasto.typeExpense}`, 14, y); y += 12;

      // Tabla con productos si hay
      if (this.productos?.length) {
        const headers = Object.keys(this.productos[0]).map(key => this.capitalize(key));
        const rows = this.productos.map(p => Object.values(p).map(v => v as CellInput));

        autoTable(doc, {
          startY: y,
          head: [headers],
          body: rows as import('jspdf-autotable').CellInput[][],
          theme: 'grid',
          styles: { fontSize: 10 },
          headStyles: { fillColor: [22, 160, 133] },
          margin: { left: 14, right: 14 },
        });
      }

      // Guarda el PDF con el nombre basado en el gasto
      doc.save(`${this.gasto.name}_detalle.pdf`);
    };
  }

  /**
   * Capitaliza la primera letra de un texto
   */
  capitalize(text: string) {
    return text.charAt(0).toUpperCase() + text.slice(1);
  }
}
