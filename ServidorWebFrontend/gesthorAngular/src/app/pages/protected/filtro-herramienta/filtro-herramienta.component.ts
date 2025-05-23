import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TicketService } from '../../../services/ticket.service';
import { SubscriptionService } from '../../../services/subscription.service';
import { SpentService } from '../../../services/spent.service';
import {
  TicketDto,
  SubscriptionDto,
  SpentDto,
  ExpenseClass
} from '../../../models/api-models/api-models.component';

@Component({
  selector: 'app-filtro-herramienta',
  standalone: false,
  templateUrl: './filtro-herramienta.component.html',
  styleUrls: ['./filtro-herramienta.component.css']
})
export class FiltroHerramientaComponent implements OnInit {

  /**
   * Tipo de ítem a mostrar y filtrar.
   * Puede ser:
   * - 'ticket' para tickets
   * - 'subscripcion' para suscripciones
   * - 'gasto' para gastos generales
   */
  tipo: 'ticket' | 'subscripcion' | 'gasto' = 'ticket';

  /**
   * Array que contiene los ítems cargados desde el backend
   * Pueden ser TicketDto, SubscriptionDto o SpentDto dependiendo del tipo.
   */
  items: Array<TicketDto | SubscriptionDto | SpentDto> = [];

  /**
   * Array que contiene los ítems filtrados, resultado de aplicar filtros
   * sobre `items`.
   */
  filteredItems: Array<TicketDto | SubscriptionDto | SpentDto> = [];

  /** Indicador de carga activa de datos para mostrar loaders */
  isLoading = true;

  /** Mensaje de error para mostrar en caso de fallo al cargar o filtrar */
  error = '';

  /** Controla la visibilidad de los filtros en la interfaz */
  mostrarFiltros: boolean = false;

  /**
   * Getter que devuelve la categoría en plural según el tipo de ítem.
   * Ejemplo: 'tickets' para tipo 'ticket', 'suscripciones' para 'subscripcion', etc.
   */
  get tipoCategoria(): string {
    switch (this.tipo) {
      case 'ticket':
        return 'tickets';
      case 'subscripcion':
        return 'suscripciones';
      case 'gasto':
        return 'gastos';
      default:
        return '';
    }
  }

  /**
   * Objeto que contiene los filtros aplicados desde la interfaz.
   * Cada campo representa un criterio de filtrado, puede ser vacío o indefinido si no se aplica.
   */
  filters: any = {
    texto: '',          // Texto para búsqueda libre (por nombre)
    fechaDesde: undefined, // Fecha mínima para filtro de fechaCompra
    fechaHasta: undefined, // Fecha máxima para filtro de fechaCompra
    categoria: '',      // Categoría principal (ExpenseClass)
    precioMin: undefined, // Precio mínimo total
    precioMax: undefined, // Precio máximo total
    fechaFin: undefined, // Fecha fin para filtro especial de suscripciones
    estado: '',         // Estado para suscripciones: 'ACTIVA' o 'CANCELADA'
    categoriaInterna: '', // Categoría interna de productos (para tickets)
    productoNombre: ''   // Nombre de producto para filtrar tickets
  };

  /** Lista de categorías principales (ExpenseClass) para filtros */
  categorias: string[] = Object.values(ExpenseClass);

  /**
   * Constructor con inyección de dependencias necesarias para:
   * - Lectura de parámetros de ruta
   * - Navegación entre rutas
   * - Servicios para obtener y manipular tickets, suscripciones y gastos
   */
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private ticketService: TicketService,
    private subscriptionService: SubscriptionService,
    private spentService: SpentService
  ) {}

  /**
   * Método para alternar la visibilidad del panel de filtros en la UI.
   * Se activa/desactiva con un toggle.
   */
  toggleFiltros(): void {
    this.mostrarFiltros = !this.mostrarFiltros;
  }

  /**
   * Lifecycle hook OnInit.
   * - Obtiene el tipo de ítem desde la ruta (parámetro 'tipo').
   * - Carga los ítems correspondientes desde el backend.
   */
  ngOnInit(): void {
    this.tipo = this.route.snapshot.paramMap.get('tipo') as any;
    this.loadItems();
  }

  /**
   * Carga los ítems desde el backend según el tipo seleccionado.
   * Actualiza `items` y aplica filtros para actualizar `filteredItems`.
   * Maneja estados de carga y errores.
   */
  loadItems(): void {
    this.isLoading = true;
    this.error = '';

    // En este componente no se usa clientId, se deja undefined.
    const clientId: number | undefined = undefined;
    let observable;

    // Selección del observable según tipo
    switch (this.tipo) {
      case 'ticket':
        observable = this.ticketService.getTickets(clientId);
        break;
      case 'subscripcion':
        observable = this.subscriptionService.getSubscriptions(clientId);
        break;
      case 'gasto':
        observable = this.spentService.getSpents(clientId);
        break;
    }

    // Suscripción a la respuesta del servicio
    observable.subscribe({
      next: data => {
        this.items = data;
        this.applyFilters();  // Aplicar filtros tras carga
        this.isLoading = false;
      },
      error: err => {
        console.error(err);
        this.error = 'Error al cargar datos';
        this.isLoading = false;
      }
    });
  }

  /**
   * Aplica los filtros definidos en `this.filters` sobre `this.items`.
   * Actualiza el array `filteredItems` con los elementos que cumplan todas las condiciones.
   *
   * Filtros aplicados:
   * - Texto en el campo 'name' (case insensitive)
   * - Categoría principal (ExpenseClass)
   * - Precio mínimo y máximo (campo 'total')
   * - Fechas desde y hasta (campo 'fechaCompra')
   * - Filtros específicos para suscripciones (fechaFin y estado)
   * - Filtros específicos para tickets (nombre producto y categoría interna en productosJSON)
   */
  applyFilters(): void {
    const f = this.filters;
    const texto = f.texto?.toLowerCase() || '';

    this.filteredItems = this.items.filter(item => {
      const fechaCompra = new Date(item.fechaCompra);

      // 1. Filtro de texto por nombre (campo 'name'), case insensitive
      let matchTexto = true;
      if (f.texto) {
        const textoLower = f.texto.toLowerCase();
        matchTexto = 'name' in item && item.name?.toLowerCase().includes(textoLower);
      }

      // 2. Categoría principal, coincide con ExpenseClass del typeExpense del ítem
      const matchCategoria = !f.categoria || ExpenseClass[item.typeExpense] === f.categoria;

      // 3. Precio mínimo y máximo
      const matchPrecioMin = f.precioMin == null || item.total >= f.precioMin;
      const matchPrecioMax = f.precioMax == null || item.total <= f.precioMax;

      // 4. Fechas desde / hasta (fechaCompra)
      const matchFechaDesde = !f.fechaDesde || fechaCompra >= new Date(f.fechaDesde);
      const matchFechaHasta = !f.fechaHasta || fechaCompra <= new Date(f.fechaHasta);

      // 5. Filtros adicionales específicos según tipo de gasto
      let matchExtra = true;

      // Filtros para suscripciones: fecha fin y estado activo o cancelado
      if (item.typeExpense === 'SUBSCRIPCION') {
        const subs = item as SubscriptionDto;
        const fechaFin = subs.end ? new Date(subs.end) : undefined;
        matchExtra = (!!(!f.fechaFin || (fechaFin && fechaFin <= new Date(f.fechaFin))) &&
                     (!f.estado || (f.estado === 'ACTIVA' && subs.activa) || (f.estado === 'CANCELADA' && !subs.activa)));
      }

      // Filtros para tickets: productoNombre y categoriaInterna dentro de productosJSON
      if (item.typeExpense === 'TICKET') {
        const ticket = item as TicketDto;
        let productos = [];
        try {
          productos = JSON.parse(ticket.productsJSON);
        } catch (e) {
          productos = [];
        }

        // Coincidencia en nombre de producto (case insensitive)
        const matchProdNombre = !f.productoNombre || productos.some((p: any) =>
          p.nombre?.toLowerCase().includes(f.productoNombre.toLowerCase())
        );

        // Coincidencia en categoría interna del producto
        const matchCategoriaInt = !f.categoriaInterna || productos.some((p: any) =>
          p.categorias?.includes(f.categoriaInterna)
        );

        matchExtra = matchProdNombre && matchCategoriaInt;
      }

      // Devuelve true si el item cumple todos los filtros
      return (
        matchTexto &&
        matchCategoria &&
        matchPrecioMin &&
        matchPrecioMax &&
        matchFechaDesde &&
        matchFechaHasta &&
        matchExtra
      );
    });
  }

  /**
   * Método para editar un ítem.
   * Busca el ítem por id en `items`, obtiene el tipo y redirige al formulario
   * correspondiente según el tipo (ticket, subscripcion, gasto).
   *
   * @param id Identificador del ítem a editar
   */
  editItem(id: number): void {
    const item = this.items.find(i => i.spentId === id);
    if (!item) return;

    let tipoRuta: string;

    switch (item.typeExpense) {
      case 'TICKET':
        tipoRuta = 'ticket';
        break;
      case 'SUBSCRIPCION':
        tipoRuta = 'subscripcion';
        break;
      default:
        tipoRuta = 'gasto';
    }

    // Navegar a la ruta del formulario con tipo y id
    this.router.navigate([`/protected/form/${tipoRuta}/${id}`]);
  }

  /**
   * Método para eliminar un ítem.
   * Pide confirmación al usuario y llama al servicio correspondiente
   * según el tipo para eliminar el ítem por id.
   * Luego recarga la lista.
   *
   * @param id Identificador del ítem a eliminar
   */
  deleteItem(id: number): void {
    if (!confirm('¿Seguro que quieres eliminar este elemento?')) return;

    let deleteObs;

    // Llamada al servicio según tipo para eliminar
    switch (this.tipo) {
      case 'ticket':
        deleteObs = this.ticketService.deleteTicket(id);
        break;
      case 'subscripcion':
        deleteObs = this.subscriptionService.deleteSubscription(id);
        break;
      case 'gasto':
        deleteObs = this.spentService.deleteSpent(id);
        break;
    }

    // Suscripción a la respuesta de eliminación
    deleteObs.subscribe({
      next: () => {
        alert('Elemento eliminado con éxito');
        this.loadItems(); // Recargar ítems tras eliminación
      },
      error: () => alert('Error al eliminar elemento')
    });
  }
}
