import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { ChartConfiguration } from 'chart.js';
import { SpentService } from '../../../services/spent.service';
import { BaseSpentDto } from '../../../models/api-models/api-models.component';
import { CategoryService } from '../../../services/categories.service';
import { CategoryDto } from '../../../models/api-models/api-models.component';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  ultimosGastos: BaseSpentDto[] = [];
  categorias: CategoryDto[] = [];
  secciones: any = [
    ["mapas", "mapa", true],
    ["monstruos", "monstruo", true],
    ["items", "item", true]
  ];

  // Donut chart: Estimado vs Real
  donutChartData: ChartConfiguration<'doughnut'>['data'] = {
    labels: ['Media semanal', 'Total semanal'],
    datasets: [{
      data: [0, 0],
      backgroundColor: ['#ffc107', '#dc3545'],
      hoverOffset: 10
    }]
  };

  // Monthly bar chart (top 6 categorías + otros)
  monthlyChartData: ChartConfiguration<'bar'>['data'] = {
    labels: [],
    datasets: [
      { 
        label: 'Gastos (€)', 
        data: [], 
        backgroundColor: [] 
      }
    ]
  };

  // Full categories donut chart
  fullCategoriesChartData: ChartConfiguration<'doughnut'>['data'] = {
    labels: [],
    datasets: [{
      data: [],
      backgroundColor: [],
      hoverOffset: 10
    }]
  };

  // Weekly line chart
  weeklyChartData: ChartConfiguration<'line'>['data'] = {
    labels: [],
    datasets: [
      {
        label: 'Gasto diario (€)',
        data: [],
        fill: false,
        borderColor: '#20c997',
        tension: 0.4
      }
    ]
  };

  // Dark mode chart options
  darkChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: {
      legend: {
        labels: {
          color: '#ffffff'
        }
      }
    },
    scales: {
      x: {
        ticks: { color: '#ffffff' },
        grid: { color: '#444' }
      },
      y: {
        ticks: { color: '#ffffff' },
        grid: { color: '#444' },
        beginAtZero: true
      }
    }
  };

  showFullCategories = false;

  constructor(
    private spentService: SpentService, 
    private authService: AuthService, 
    private categoryService: CategoryService
  ) {}

  ngOnInit() {  
    this.cargarDatos();
  }

  cargarDatos() {
    // Cargar categorías primero
    this.categoryService.getCategories().subscribe(cats => {
      this.categorias = cats;
      
      // Luego cargar gastos
      this.spentService.getSpents().subscribe(gastos => {
        this.ultimosGastos = gastos;
        this.actualizarGraficos(gastos);
      });
    });
  }

  actualizarGraficos(gastos: BaseSpentDto[]) {
    this.actualizarDonutChart(gastos);
    this.actualizarMonthlyChart(gastos);
    this.actualizarWeeklyChart(gastos);
    this.actualizarFullCategoriesChart(gastos);
  }

  actualizarDonutChart(gastos: BaseSpentDto[]) {
    const sieteDiasAtras = new Date();
    sieteDiasAtras.setDate(sieteDiasAtras.getDate() - 7);
    
    const gastosSemana = gastos.filter(g => 
      new Date(g.fechaCompra) >= sieteDiasAtras
    );
    
    const totalSemanal = gastosSemana.reduce((sum, g) => sum + g.total, 0);
    const mediaSemanal = totalSemanal / 7;
    
    this.donutChartData = {
      ...this.donutChartData,
      datasets: [{
        ...this.donutChartData.datasets[0],
        data: [mediaSemanal, totalSemanal]
      }]
    };
  }

  actualizarMonthlyChart(gastos: BaseSpentDto[]) {
    const gastosPorCategoria: {[key: number]: {total: number, name: string}} = {};
    
    // Inicializar todas las categorías conocidas
    this.categorias.forEach(cat => {
      gastosPorCategoria[cat.id] = {
        total: 0,
        name: cat.name
      };
    });
    
    // Sumar gastos por categoría
    gastos.forEach(gasto => {
      if (gastosPorCategoria[gasto.categoriaId]) {
        gastosPorCategoria[gasto.categoriaId].total += gasto.total;
      } else {
        gastosPorCategoria[gasto.categoriaId] = {
          total: gasto.total,
          name: `Categoría ${gasto.categoriaId}`
        };
      }
    });
    
    // Ordenar por total y separar top 6 y otros
    const categoriasOrdenadas = Object.values(gastosPorCategoria)
      .sort((a, b) => b.total - a.total);
    
    const topCategorias = categoriasOrdenadas.slice(0, 6);
    const otrasCategorias = categoriasOrdenadas.slice(6);
    const totalOtros = otrasCategorias.reduce((sum, cat) => sum + cat.total, 0);
    
    // Preparar datos para el gráfico
    const labels = topCategorias.map(c => c.name);
    const data = topCategorias.map(c => c.total);
    const backgroundColors = topCategorias.map((_, i) => this.getChartColor(i));
    
    // Agregar "Otros" si hay más categorías
    if (otrasCategorias.length > 0) {
      labels.push('Otros');
      data.push(totalOtros);
      backgroundColors.push('#6c757d'); // Color gris para "Otros"
    }
    
    this.monthlyChartData = {
      labels: labels,
      datasets: [{
        label: 'Top 6 Categorías + Otros',
        data: data,
        backgroundColor: backgroundColors
      }]
    };
  }

  actualizarFullCategoriesChart(gastos: BaseSpentDto[]) {
    const gastosPorCategoria: {[key: number]: {total: number, name: string}} = {};
    
    this.categorias.forEach(cat => {
      gastosPorCategoria[cat.id] = {
        total: 0,
        name: cat.name
      };
    });
    
    gastos.forEach(gasto => {
      if (gastosPorCategoria[gasto.categoriaId]) {
        gastosPorCategoria[gasto.categoriaId].total += gasto.total;
      } else {
        gastosPorCategoria[gasto.categoriaId] = {
          total: gasto.total,
          name: `Categoría ${gasto.categoriaId}`
        };
      }
    });
    
    const categoriasOrdenadas = Object.values(gastosPorCategoria)
      .sort((a, b) => b.total - a.total);
    
    this.fullCategoriesChartData = {
      labels: categoriasOrdenadas.map(c => c.name),
      datasets: [{
        data: categoriasOrdenadas.map(c => c.total),
        backgroundColor: categoriasOrdenadas.map((_, i) => this.getChartColor(i)),
        hoverOffset: 10
      }]
    };
  }

  actualizarWeeklyChart(gastos: BaseSpentDto[]) {
    const hoy = new Date();
    const diasSemana = ['Dom', 'Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb'];
    const gastosPorDia: {[key: string]: number} = {};
    
    for (let i = 6; i >= 0; i--) {
      const fecha = new Date(hoy);
      fecha.setDate(fecha.getDate() - i);
      const diaStr = diasSemana[fecha.getDay()];
      gastosPorDia[diaStr] = 0;
    }
    
    const sieteDiasAtras = new Date();
    sieteDiasAtras.setDate(sieteDiasAtras.getDate() - 7);
    
    gastos
      .filter(g => new Date(g.fechaCompra) >= sieteDiasAtras)
      .forEach(g => {
        const fechaGasto = new Date(g.fechaCompra);
        const diaStr = diasSemana[fechaGasto.getDay()];
        gastosPorDia[diaStr] += g.total;
      });
    
    const diasOrdenados = ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'];
    this.weeklyChartData = {
      ...this.weeklyChartData,
      labels: diasOrdenados,
      datasets: [{
        ...this.weeklyChartData.datasets[0],
        data: diasOrdenados.map(dia => gastosPorDia[dia])
      }]
    };
  }

  pulsarBoton(buttonName: string) {
    if (buttonName === 'todo') {
      this.secciones.forEach((seccion: any) => {
        seccion[2] = true;
      });
    } else {
      this.secciones.forEach((seccion: any) => {
        seccion[2] = false;
      });
      const selectedSection = this.secciones.find((seccion: any) => seccion[0] === buttonName);
      if (selectedSection) {
        selectedSection[2] = true;
      }
    }
  }

  toggleFullCategories() {
    this.showFullCategories = !this.showFullCategories;
  }

  getNombreCategoria(id: number): string {
    const categoria = this.categorias.find(c => c.id === id);
    return categoria ? categoria.name : `Categoría ${id}`;
  }

  getChartColor(index: number): string {
    const colors = [
      '#0d6efd', '#198754', '#fd7e14', '#dc3545', 
      '#6f42c1', '#20c997', '#6610f2', '#d63384',
      '#0dcaf0', '#ffc107', '#adb5bd', '#6c757d'
    ];
    return colors[index % colors.length];
  }
}