import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { SpentFullDto } from '../../models/api-models/api-models.component';
import { ChartConfiguration } from 'chart.js';

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

  // Resto de los métodos se mantienen igual...
  parseProducts(json: string): any[] {
    try {
      return JSON.parse(json);
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
}
