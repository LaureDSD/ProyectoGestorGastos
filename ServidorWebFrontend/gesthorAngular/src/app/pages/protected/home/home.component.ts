import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { ChartConfiguration } from 'chart.js';
import { SpentService } from '../../../services/spent.service';
import { BaseSpentDto } from '../../../models/api-models/api-models.component';
import { CategoryService } from '../../../services/categories.service';
import { CategoryDto } from '../../../models/api-models/api-models.component';

/**
 * Componente principal de la vista de inicio.
 * Encargado de cargar datos del usuario autenticado, gastos y categorías,
 * y generar múltiples visualizaciones (gráficos) basadas en esos datos.
 */
@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  /** Últimos gastos ordenados por fecha */
  ultimosGastos: BaseSpentDto[] = [];

  /** Lista de categorías obtenidas desde el backend */
  categorias: CategoryDto[] = [];

  /** Secciones visuales controladas por botones (visibilidad true/false) */
  secciones: any = [
    ["mapas", "mapa", true],
    ["monstruos", "monstruo", true],
    ["items", "item", true]
  ];

  /** Datos para el gráfico tipo Donut que muestra media semanal vs total semanal */
  donutChartData: ChartConfiguration<'doughnut'>['data'] = {
    labels: ['Media semanal', 'Total semanal'],
    datasets: [{
      data: [0, 0],
      backgroundColor: ['#ffc107', '#dc3545'],
      hoverOffset: 10
    }]
  };

  /** Gráfico de barras con las 6 categorías con más gasto + categoría 'Otros' */
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

  /** Gráfico Donut de todas las categorías, mostrando proporción del gasto */
  fullCategoriesChartData: ChartConfiguration<'doughnut'>['data'] = {
    labels: [],
    datasets: [{
      data: [],
      backgroundColor: [],
      hoverOffset: 10
    }]
  };

  /** Gráfico de líneas con gasto diario de la última semana */
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

  /** Configuración de estilos para gráficos en modo oscuro */
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

  /** Controla la visualización del gráfico completo de categorías */
  showFullCategories = false;

  constructor(
    private spentService: SpentService,
    private authService: AuthService,
    private categoryService: CategoryService
  ) {}

  /** Hook de inicialización del componente. Carga los datos necesarios. */
  ngOnInit() {
    this.cargarDatos();
  }

  /** Obtiene categorías y luego los gastos desde los servicios y actualiza gráficos */
  cargarDatos() {
    this.categoryService.getCategories().subscribe(cats => {
      this.categorias = cats;

      this.spentService.getSpents().subscribe(gastos => {
        this.ultimosGastos = gastos.sort((a, b) => new Date(b.fechaCompra).getTime() - new Date(a.fechaCompra).getTime());
        this.actualizarGraficos(gastos);
      });
    });
  }

  /** Actualiza todos los gráficos con los gastos proporcionados */
  actualizarGraficos(gastos: BaseSpentDto[]) {
    this.actualizarDonutChart(gastos);
    this.actualizarMonthlyChart(gastos);
    this.actualizarWeeklyChart(gastos);
    this.actualizarFullCategoriesChart(gastos);
  }

  /**
   * Actualiza el gráfico Donut semanal con media y total de los últimos 7 días
   */
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
        data: [mediaSemanal, totalSemanal],
        backgroundColor: ['#000000', '#4dc9f6'],
        hoverBackgroundColor: ['#2C9AB7', '#CC527A']
      }]
    };
  }

  /**
   * Actualiza el gráfico de barras agrupando por categoría,
   * muestra las 6 con mayor gasto y agrupa el resto como "Otros".
   */
  actualizarMonthlyChart(gastos: BaseSpentDto[]) {
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

    const categoriasOrdenadas = Object.values(gastosPorCategoria).sort((a, b) => b.total - a.total);
    const topCategorias = categoriasOrdenadas.slice(0, 6);
    const otrasCategorias = categoriasOrdenadas.slice(6);
    const totalOtros = otrasCategorias.reduce((sum, cat) => sum + cat.total, 0);

    const labels = topCategorias.map(c => c.name);
    const data = topCategorias.map(c => c.total);
    const backgroundColors = topCategorias.map((_, i) => this.getChartColor(i));

    if (otrasCategorias.length > 0) {
      labels.push('Otros');
      data.push(totalOtros);
      backgroundColors.push('#6c757d');
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

  /**
   * Genera un gráfico Donut que muestra el gasto total por cada categoría.
   */
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

    const categoriasOrdenadas = Object.values(gastosPorCategoria).sort((a, b) => b.total - a.total);

    this.fullCategoriesChartData = {
      labels: categoriasOrdenadas.map(c => c.name),
      datasets: [{
        data: categoriasOrdenadas.map(c => c.total),
        backgroundColor: categoriasOrdenadas.map((_, i) => this.getChartColor(i)),
        hoverOffset: 10
      }]
    };
  }

  /**
   * Construye el gráfico de líneas para los gastos diarios en los últimos 7 días.
   */
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

  /**
   * Cambia la visibilidad de las secciones según el botón pulsado.
   * Si se pulsa 'todo', se activan todas las secciones.
   */
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

  /** Alterna la visualización del gráfico de todas las categorías */
  toggleFullCategories() {
    this.showFullCategories = !this.showFullCategories;
  }

  /**
   * Retorna el nombre de una categoría dado su ID
   * @param id ID de la categoría
   */
  getNombreCategoria(id: number): string {
    const categoria = this.categorias.find(c => c.id === id);
    return categoria ? categoria.name : `Categoría ${id}`;
  }

  /**
   * Devuelve un color del array predefinido basado en un índice,
   * útil para asignar colores únicos por categoría o dataset.
   * @param index Índice del color a usar
   */
  getChartColor(index: number): string {
    const colors = [
      '#8800f6', '#00b0f6', '#f68700', '#dc3545',
      '#6f42c1', '#20c997', '#6610f2', '#d63384',
      '#0dcaf0', '#ffc107', '#adb5bd', '#6c757d'
    ];
    return colors[index % colors.length];
  }
}
