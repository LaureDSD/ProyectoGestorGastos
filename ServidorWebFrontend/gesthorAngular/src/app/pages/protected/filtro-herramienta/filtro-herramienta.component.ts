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
  tipo: 'ticket' | 'subscripcion' | 'gasto' = 'ticket';
  items: Array<TicketDto | SubscriptionDto | SpentDto> = [];
  filteredItems: Array<TicketDto | SubscriptionDto | SpentDto> = [];
  isLoading = true;
  error = '';

  mostrarFiltros: boolean = false;

  get tipoCategoria(): string {
  switch (this.tipo) {
    case 'ticket':
      return 'tickets';
    case 'subscripcion':
      return 'subscripciones';
    case 'gasto':
      return 'gastos';
    default:
      return '';
  }
}

  filters: any = {
    texto: '',
    fechaDesde: undefined,
    fechaHasta: undefined,
    categoria: '',
    precioMin: undefined,
    precioMax: undefined,
    fechaFin: undefined,
    estado: '',
    categoriaInterna: '',
    productoNombre: ''
  };

  categorias: string[] = Object.values(ExpenseClass);

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private ticketService: TicketService,
    private subscriptionService: SubscriptionService,
    private spentService: SpentService
  ) {}



toggleFiltros(): void {
  this.mostrarFiltros = !this.mostrarFiltros;
}

  ngOnInit(): void {
    this.tipo = this.route.snapshot.paramMap.get('tipo') as any;
    this.loadItems();
  }

  loadItems(): void {
    this.isLoading = true;
    this.error = '';

    const clientId: number | undefined = undefined;
    let observable;

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

    observable.subscribe({
      next: data => {
        this.items = data;
        this.applyFilters();
        this.isLoading = false;
      },
      error: err => {
        console.error(err);
        this.error = 'Error al cargar datos';
        this.isLoading = false;
      }
    });
  }

 applyFilters(): void {
  const f = this.filters;
  const texto = f.texto?.toLowerCase() || '';

  this.filteredItems = this.items.filter(item => {
    const fechaCompra = new Date(item.fechaCompra);

    // 1. Texto general (en todo el objeto)
    /*let matchTexto = true;
      if (f.texto) {
        const textoLower = f.texto.toLowerCase();

        if ('name' in item && item.name?.toLowerCase().includes(textoLower)) {
          matchTexto = true;
        }
        else if ('description' in item && item.description?.toLowerCase().includes(textoLower)) {
          matchTexto = true;
        } else if ('productsJSON' in item) {
          try {
            const productos = JSON.parse(item.productsJSON);
            matchTexto = productos.some((p: any) =>
              p.nombre?.toLowerCase().includes(textoLower)
            )
          } catch (e) {
            matchTexto = false;
          }
        } else {
          matchTexto = false;
        }

      }*/
      // 1. Filtro solo por nombre
      let matchTexto = true;
      if (f.texto) {
        const textoLower = f.texto.toLowerCase();
        matchTexto = 'name' in item && item.name?.toLowerCase().includes(textoLower);
      }



    // 2. Categoría principal
    const matchCategoria = !f.categoria || ExpenseClass[item.typeExpense] === f.categoria;

    // 3. Precio mínimo y máximo
    const matchPrecioMin = f.precioMin == null || item.total >= f.precioMin;
    const matchPrecioMax = f.precioMax == null || item.total <= f.precioMax;

    // 4. Fechas desde / hasta
    const matchFechaDesde = !f.fechaDesde || fechaCompra >= new Date(f.fechaDesde);
    const matchFechaHasta = !f.fechaHasta || fechaCompra <= new Date(f.fechaHasta);

    // 5. Filtros específicos según tipo
    let matchExtra = true;

    if (item.typeExpense === 'SUBSCRIPCION') {
      const subs = item as SubscriptionDto;
      const fechaFin = subs.end ? new Date(subs.end) : undefined;
      matchExtra = (!!(!f.fechaFin || (fechaFin && fechaFin <= new Date(f.fechaFin))) &&
                   (!f.estado || (f.estado === 'ACTIVA' && subs.activa) || (f.estado === 'CANCELADA' && !subs.activa)));
    }

    if (item.typeExpense === 'TICKET') {
      const ticket = item as TicketDto;
      let productos = [];
      try {
        productos = JSON.parse(ticket.productsJSON);
      } catch (e) {
        productos = [];
      }

      const matchProdNombre = !f.productoNombre || productos.some((p: any) =>
        p.nombre?.toLowerCase().includes(f.productoNombre.toLowerCase())
      );
      const matchCategoriaInt = !f.categoriaInterna || productos.some((p: any) =>
        p.categorias?.includes(f.categoriaInterna)
      );

      matchExtra = matchProdNombre && matchCategoriaInt;
    }

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


  // Editar elemento y redirigir al formulario de edición
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

  this.router.navigate([`/protected/form/${tipoRuta}/${id}`]);
}



  // Eliminar elemento y luego recargar los datos
  deleteItem(id: number): void {
    if (!confirm('¿Seguro que quieres eliminar este elemento?')) return;

    let deleteObs;
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

    deleteObs.subscribe({
      next: () => {
        alert('Elemento eliminado con éxito');
        this.loadItems();
      },
      error: () => alert('Error al eliminar elemento')
    });
  }
}
