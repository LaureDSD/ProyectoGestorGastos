// Angular + Bootstrap Dashboard avanzado con filtros dinámicos y gráficos
// Requiere instalar: ngx-bootstrap, chart.js, ng2-charts

import { Component, OnInit } from '@angular/core';
import { SpentService } from './../../../services/spent.service';
import { SpentFullDto, ExpenseClass } from '../../../models/api-models/api-models.component';
import { ChartConfiguration, ChartType } from 'chart.js';


@Component({
  selector: 'app-gastos',
    standalone: false,
  templateUrl: './gastos.component.html',
  styleUrls: ['./gastos.component.css']
})
export class GastosComponent implements OnInit {
 spents: SpentFullDto[] = [];
  filteredSpents: SpentFullDto[] = [];

  activeYear: number = new Date().getFullYear();
  availableYears: number[] = [];

  activeType: string = 'ALL';
  typeExpenses: string[] = ['ALL', 'TICKET', 'SUBSCRIPCION', 'TRANSFERENCIA', 'GASTO_GENERICO'];
  searchTerm: string = '';

  chartData: ChartConfiguration<'bar'>['data'] = {
    labels: [],
    datasets: [
      { data: [], label: 'Gasto Mensual' }
    ]
  };

  constructor(private spentService: SpentService) {}

  ngOnInit(): void {
    this.spentService.getFullSpents().subscribe({
      next: (data) => {
        this.spents = data;
        this.availableYears = [...new Set(this.spents.map(s => new Date(s.fechaCompra).getFullYear()))];
        this.availableYears.sort((a, b) => a - b);
        this.updateFiltered();
      },
      error: (err) => console.error('Error al cargar los gastos', err)
    });
  }

  updateFiltered() {
    let spentsByYear = this.spents.filter(s => new Date(s.fechaCompra).getFullYear() === this.activeYear);

    if (this.activeType !== 'ALL') {
      spentsByYear = spentsByYear.filter(s => s.typeExpense === this.activeType);
    }

    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase();
      spentsByYear = spentsByYear.filter(s => {
        const json = this.parseProducts(s.productsJSON ?? null);
        const prodMatch = json?.some(p => p.nombre?.toLowerCase().includes(term) || p.categorias?.some((c: string) => c?.toLowerCase().includes(term)));
        return (
          s.name?.toLowerCase().includes(term) ||
          s.description?.toLowerCase().includes(term) ||
          prodMatch
        );
      });
    }

    this.filteredSpents = spentsByYear;
    this.updateChart();
  }

  updateChart() {
    const months = Array(12).fill(0);
    this.filteredSpents.forEach(s => {
      const month = new Date(s.fechaCompra).getMonth();
      months[month] += s.total;
    });

    this.chartData.labels = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];
    this.chartData.datasets[0].data = months;
  }

  parseProducts(productsJSON: string | null): any[] {
    try {
      return productsJSON ? JSON.parse(productsJSON) : [];
    } catch {
      return [];
    }
  }

  changeYear(delta: number) {
    const index = this.availableYears.indexOf(this.activeYear);
    const newIndex = index + delta;
    if (newIndex >= 0 && newIndex < this.availableYears.length) {
      this.activeYear = this.availableYears[newIndex];
      this.updateFiltered();
    }
  }

  canNavigateYear(delta: number): boolean {
    const index = this.availableYears.indexOf(this.activeYear);
    const newIndex = index + delta;
    return newIndex >= 0 && newIndex < this.availableYears.length;
  }

  setType(type: string) {
    this.activeType = type;
    this.updateFiltered();
  }
}
