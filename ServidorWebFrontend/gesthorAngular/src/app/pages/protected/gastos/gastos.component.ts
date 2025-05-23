/**
 * Componente GastosComponent
 *
 * Gestiona la visualización, filtrado y análisis de los gastos registrados,
 * mostrando además un gráfico mensual de los gastos filtrados.
 *
 * Funcionalidades principales:
 * - Carga y almacenamiento de gastos completos.
 * - Filtrado por año, tipo de gasto y búsqueda por texto en nombre, descripción o productos.
 * - Gestión de suscripciones activas.
 * - Visualización de un gráfico de barras con gasto mensual.
 * - Navegación entre años disponibles.
 * - Control de visualización de detalles individuales por gasto.
 * - Redirección a formularios específicos para edición o detalles.
 */

import { Router } from '@angular/router';
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
  /** Lista completa de gastos obtenidos desde el servicio */
  spents: SpentFullDto[] = [];

  /** Lista filtrada de gastos según criterios actuales */
  filteredSpents: SpentFullDto[] = [];

  /** Indica si la carga de datos está en progreso */
  isLoading = true;

  /** Lista de suscripciones activas filtradas entre los gastos */
  activeSubscriptions: SpentFullDto[] = [];

  /** Año activo para filtro (por defecto el año actual) */
  activeYear: number = new Date().getFullYear();

  /** Lista de años disponibles, extraídos de los gastos */
  availableYears: number[] = [];

  /** Tipo de gasto activo para filtro ('TODO' = todos) */
  activeType: string = 'TODO';

  /** Lista de tipos de gastos disponibles para filtro */
  typeExpenses: string[] = ['TODO', 'TICKET', 'SUBSCRIPCION', 'TRANSFERENCIA', 'GASTO_GENERICO'];

  /** Término de búsqueda para filtrar gastos por texto */
  searchTerm: string = '';

  /** Configuración de datos para gráfico de barras */
  chartData: ChartConfiguration<'bar'>['data'] = {
    labels: [],
    datasets: [
      { data: [], label: 'Gasto Mensual' }
    ]
  };

  /**
   * Constructor con inyección de servicios necesarios:
   * - spentService: servicio para obtener datos de gastos.
   * - router: para navegación entre rutas.
   */
  constructor(private spentService: SpentService, private router : Router) {}

  /**
   * Ciclo de vida OnInit
   *
   * Solicita los gastos completos desde el servicio y procesa:
   * - Almacena gastos en `spents`.
   * - Extrae y ordena años disponibles.
   * - Filtra suscripciones activas.
   * - Actualiza estado de carga.
   * - Actualiza filtro y gráfico inicial.
   */
  ngOnInit(): void {
    this.spentService.getFullSpents().subscribe({
      next: (data) => {
        this.spents = data;
        this.availableYears = [...new Set(this.spents.map(s => new Date(s.fechaCompra).getFullYear()))];
        this.availableYears.sort((a, b) => a - b);

        const hoy = new Date();
        this.activeSubscriptions = this.spents.filter(s =>
          s.typeExpense === 'SUBSCRIPCION' &&
          s.activa !== false &&
          (
            !s.end || new Date(s.end).getTime() > hoy.getTime()
          )
        );

        this.isLoading = false;
        this.updateFiltered();
      },
      error: (err) => {
        this.isLoading = false;
        console.error('Error al cargar los gastos', err);
      }
    });
  }

  /**
   * Calcula la fecha de renovación de una suscripción dado el gasto.
   * Retorna la fecha un mes posterior a la fecha de compra.
   * @param gasto gasto del cual calcular la renovación
   * @returns Fecha de renovación como objeto Date
   */
  getFechaRenovacion(gasto: SpentFullDto): Date {
    const compra = new Date(gasto.fechaCompra);
    const renovacion = new Date(compra);
    renovacion.setMonth(compra.getMonth() + 1);
    return renovacion;
  }

  /**
   * Actualiza la lista filtrada de gastos `filteredSpents`
   * aplicando filtros de año, tipo de gasto y término de búsqueda.
   * Además actualiza los datos del gráfico con los gastos filtrados.
   */
  updateFiltered() {
    // Filtrado por año
    let spentsByYear = this.spents.filter(s => new Date(s.fechaCompra).getFullYear() === this.activeYear);

    // Filtrado por tipo de gasto, excepto si es TODO (mostrar todos)
    if (this.activeType !== 'TODO') {
      spentsByYear = spentsByYear.filter(s => s.typeExpense === this.activeType);
    }

    // Filtrado por término de búsqueda en nombre, descripción o productos
    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase();
      spentsByYear = spentsByYear.filter(s => {
        const json = this.parseProducts(s.productsJSON ?? null);
        const prodMatch = json?.some(p =>
          p.nombre?.toLowerCase().includes(term) ||
          p.categorias?.some((c: string) => c?.toLowerCase().includes(term))
        );
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

  /**
   * Actualiza los datos para el gráfico de barras que muestra
   * la suma de gastos mensuales del conjunto filtrado.
   */
  updateChart() {
    // Inicializa arreglo con 12 meses con valor 0
    const months = Array(12).fill(0);

    // Suma el total de gastos por mes en el arreglo
    this.filteredSpents.forEach(s => {
      const month = new Date(s.fechaCompra).getMonth();
      months[month] += s.total;
    });

    // Actualiza la configuración de datos del gráfico
    this.chartData = {
      labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
      datasets: [
        {
          data: months,
          label: 'Gasto Mensual',
          backgroundColor: ['#4dc9f6'],
          borderRadius: 6
        }
      ]
    };
  }

  /**
   * Parsea un string JSON que representa productos a un arreglo de objetos.
   * En caso de error de parsing retorna un arreglo vacío.
   * @param productsJSON JSON string de productos
   * @returns Arreglo de productos
   */
  parseProducts(productsJSON: string | null): any[] {
    try {
      return productsJSON ? JSON.parse(productsJSON) : [];
    } catch {
      return [];
    }
  }

  /**
   * Cambia el año activo para filtro sumando el valor delta (+1 o -1).
   * Actualiza el filtro y gráfico tras el cambio.
   * @param delta incremento o decremento para cambiar año
   */
  changeYear(delta: number) {
    const index = this.availableYears.indexOf(this.activeYear);
    const newIndex = index + delta;
    if (newIndex >= 0 && newIndex < this.availableYears.length) {
      this.activeYear = this.availableYears[newIndex];
      this.updateFiltered();
    }
  }

  /**
   * Indica si es posible navegar hacia un año anterior o siguiente
   * según el delta recibido.
   * @param delta incremento o decremento para año
   * @returns true si puede navegar, false si está fuera de rango
   */
  canNavigateYear(delta: number): boolean {
    const index = this.availableYears.indexOf(this.activeYear);
    const newIndex = index + delta;
    return newIndex >= 0 && newIndex < this.availableYears.length;
  }

  /**
   * Cambia el tipo de gasto activo para el filtro y actualiza listado y gráfico.
   * @param type nuevo tipo de gasto para filtrar
   */
  setType(type: string) {
    this.activeType = type;
    this.updateFiltered();
  }

  /**
   * Objeto que almacena el estado visible de detalles para cada índice de gasto.
   * Es usado para mostrar/ocultar detalles individuales.
   */
  detallesVisibles: {[key: number]: boolean} = {};

  /**
   * Alterna la visibilidad del detalle del gasto para el índice indicado.
   * @param index índice del gasto para alternar detalle
   */
  toggleDetalle(index: number) {
    this.detallesVisibles[index] = !this.detallesVisibles[index];
  }

  /**
   * Retorna true si el detalle para el índice indicado está visible, false en caso contrario.
   * @param index índice de gasto
   * @returns estado de visibilidad del detalle
   */
  mostrarDetalle(index: number): boolean {
    return this.detallesVisibles[index] || false;
  }

  /**
   * Redirige a la ruta de edición o visualización de una suscripción
   * pasando el ID de suscripción.
   * @param subId ID de suscripción a redirigir
   */
  redirectToSubscription(subId: number) {
    this.redirectTo(`/protected/form/subscripcion/${subId}`);
  }

  /**
   * Redirige a la ruta indicada mediante el Router.
   * @param route ruta a la que se desea navegar
   */
  redirectTo(route : string){
    this.router.navigate([route])
  }
}
