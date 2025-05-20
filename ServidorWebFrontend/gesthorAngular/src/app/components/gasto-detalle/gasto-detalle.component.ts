import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { SpentFullDto } from '../../models/api-models/api-models.component';
import { ChartConfiguration } from 'chart.js';
import jsPDF from 'jspdf';
import autoTable, { CellInput } from 'jspdf-autotable';

@Component({
  selector: 'app-gasto-detalle',
  standalone: false,
  templateUrl: './gasto-detalle.component.html',
  styleUrls: ['./gasto-detalle.component.css']
})
export class GastoDetalleComponent implements OnChanges {
  @Input() gasto!: SpentFullDto;

  productos: any[] = [];
  totalDias: number = 30;
  diasRestantes: number = 0;
  fechaRenovacion!: Date;

  chartProductos: ChartConfiguration<'doughnut'>['data'] = { labels: [], datasets: [] };
  chartCategorias: ChartConfiguration<'pie'>['data'] = { labels: [], datasets: [] };

  ngOnChanges(changes: SimpleChanges) {
    if (changes['gasto'] && this.gasto) {
      this.generarContenido();
    }
  }

  generarContenido() {
    if (this.gasto.typeExpense === 'TICKET') {
      this.productos = this.parseProducts(this.gasto.productsJSON ?? '');
      this.initGraficosTicket();
    } else if (this.gasto.typeExpense === 'SUBSCRIPCION') {
      this.calcularRenovacion();
    }
  }

  parseProducts(json: string | null | undefined): any[] {
    try {
      return json ? JSON.parse(json) : [];
    } catch {
      return [];
    }
  }

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
          '#66b2ff',
          '#3399ff',
          '#2673cc',
          '#1a4d80',
          '#003366'
        ],
        label: 'Gasto por producto'
      }]
    };

    this.chartCategorias = {
      labels: Object.keys(porCategoria),
      datasets: [{
        data: Object.values(porCategoria),
        backgroundColor: [
          '#ff6666',
          '#ff4d4d',
          '#cc0000',
          '#800000',
          '#4d0000'
        ],
        label: 'Gasto por categoría'
      }]
    };
  }

  calcularRenovacion() {
    const compra = new Date(this.gasto.fechaCompra);
    this.fechaRenovacion = new Date(compra);
    this.fechaRenovacion.setMonth(compra.getMonth() + 1);

    const hoy = new Date();
    const diffTotal = this.fechaRenovacion.getTime() - compra.getTime();
    const diffRestante = this.fechaRenovacion.getTime() - hoy.getTime();

    this.totalDias = Math.ceil(diffTotal / (1000 * 60 * 60 * 24));
    this.diasRestantes = Math.max(0, Math.ceil(diffRestante / (1000 * 60 * 60 * 24)));
  }


  convertToCSV(data: any[]): string {
    if (!data.length) return '';

    const headers = Object.keys(data[0]);
    const rows = data.map(row =>
      headers.map(field => {
        const value = row[field];
        if (Array.isArray(value)) return `"${value.join(', ')}"`; // Array to string
        return `"${value ?? ''}"`;
      }).join(',')
    );

    return headers.join(',') + '\n' + rows.join('\n');
  }

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


  exportToPDF() {
    const doc = new jsPDF();
    let y = 30;

    const img = new Image();
    img.src = '/icon/icon.png';

    doc.setFontSize(40);
    doc.setFont('bold');
    doc.text('GESTHOR', doc.internal.pageSize.getWidth() / 2, y, { align: 'center' });
    y += 10;

    img.onload = () => {
      doc.addImage(img, 'PNG', 180, 5, 20, 20);
      y += 25;

      doc.setFontSize(12);
      doc.text(`Nombre del gasto: ${this.gasto.name}`, 14, y); y += 8;
      doc.text(`Descripción: ${this.gasto.description ?? '-'}`, 14, y); y += 8;
      doc.text(`Fecha de compra: ${this.gasto.fechaCompra}`, 14, y); y += 8;
      doc.text(`Tienda: ${this.gasto.store ?? '-'}`, 14, y); y += 8;
      doc.text(`Total: ${this.gasto.total}€`, 14, y); y += 8;
      doc.text(`IVA: ${this.gasto.iva}%`, 14, y); y += 8;
      doc.text(`Tipo: ${this.gasto.typeExpense}`, 14, y); y += 12;

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

      doc.save(`${this.gasto.name}_detalle.pdf`);
    };
  }

  capitalize(text: string) {
    return text.charAt(0).toUpperCase() + text.slice(1);
  }

}
