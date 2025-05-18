import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Producto } from '../../../models/models/models.component';
import { SpentService } from '../../../services/spent.service';
import { SubscriptionService } from '../../../services/subscription.service';
import { TicketService } from '../../../services/ticket.service';
import { CategoryDto, ExpenseClass, ExpenseFilterClass } from '../../../models/api-models/api-models.component';
import { CategoryService } from '../../../services/categories.service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-form-herramienta',
  standalone: false,
  templateUrl: './form-herramienta.component.html',
  styleUrl: './form-herramienta.component.css'
})
export class FormHerramientaComponent implements OnInit {
  tipo: 'ticket' | 'subscripcion' | 'gasto' = 'ticket';
  expenseTypes = Object.values(ExpenseFilterClass);
  id!: number;
  productosLocal: Producto[] = [];
  img : string = '';
  form!: FormGroup;
  isLoading = true;
  categories: CategoryDto[] = [];
  error = '';
  server = `${environment.apiUrl}/`;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private spentService: SpentService,
    private subscriptionService: SubscriptionService,
    private ticketService: TicketService,
    private router: Router,
    private categoryService: CategoryService
  ) {
    this.categoryService.getCategories().subscribe(categories => {
      this.categories = categories;
    });
  }

  ngOnInit(): void {
    this.tipo = this.route.snapshot.paramMap.get('tipo') as any;
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.initForm();

    if (this.id !== 0) {
      this.loadData();
    } else {
      this.isLoading = false;
      this.setupAutoFields();
      // Establecer fecha actual solo para nuevos registros
      if (this.tipo === 'gasto') {
        this.form.get('fechaCompra')?.setValue(new Date().toISOString().slice(0, 16));
      }
    }

    // Suscribirse a cambios relevantes para recalcular accumulate
    ['total', 'start', 'end', 'activa', 'intervalTime', 'fechaCompra'].forEach(field => {
      this.form.get(field)?.valueChanges.subscribe(() => {
        this.calculateAccumulate();
      });
    });

    // Controlar la activación para limpiar fecha end si activa=true
    this.form.get('activa')?.valueChanges.subscribe(activa => {
      if (activa) {
        // Si está activa, borrar fecha de fin (end)
        this.form.get('end')?.setValue('');
      } else {
        // Si se desactiva y no hay fecha fin, poner fecha actual
        if (!this.form.get('end')?.value) {
          this.form.get('end')?.setValue(new Date().toISOString().slice(0, 16));
        }
      }
    });

    // Sincronizar start con fechaCompra
    this.form.get('fechaCompra')?.valueChanges.subscribe(fechaCompra => {
      if (fechaCompra) {
        this.form.get('start')?.setValue(fechaCompra, { emitEvent: false });
      }
    });
  }

  initForm() {
    const formConfig: any = {
      spentId: [this.id],
      userId: [1, Validators.required],
      categoriaId: [1, Validators.required],
      name: ['', Validators.required],
      description: [''],
      icon: [''],
      total: [0, [Validators.required, Validators.min(0.01)]],
      iva: [0, [Validators.required, Validators.min(0)]],

      // Ticket
      store: [''],
      productsJSON: [''],

      // Subscripción
      start: [''],
      end: [''],
      accumulate: [0],
      restartDay: [''],
      intervalTime: [30],  // Default 30 días
      activa: [true],

      // Para gasto
      fechaCompra: [''],
      typeExpense: [ExpenseFilterClass.GASTO_GENERICO, Validators.required],
    };

    // Para ticket y subscripción, fechaCompra es editable y required
    if (this.tipo === 'ticket' || this.tipo === 'subscripcion') {
      formConfig.fechaCompra = ['', Validators.required];
      formConfig.typeExpense = [this.tipo === 'ticket' ? ExpenseClass.TICKET : ExpenseClass.SUBSCRIPCION, Validators.required];
    } else if (this.tipo === 'gasto') {
      formConfig.fechaCompra = [this.id === 0 ? new Date().toISOString().slice(0, 16) : '', Validators.required];
      formConfig.typeExpense = [ExpenseFilterClass.GASTO_GENERICO, Validators.required];
    }

    this.form = this.fb.group(formConfig);

    this.form.get('total')?.valueChanges.subscribe(total => {
      if (this.tipo === 'subscripcion') {
        this.form.get('accumulate')?.setValue(total);
      }
    });
  }

  normalizeDataForForm(data: any): any {
    const normalized: any = { ...data };

    if (normalized.fechaCompra) {
      normalized.fechaCompra = new Date(normalized.fechaCompra)
        .toISOString()
        .slice(0, 16);
    }

    if (normalized.start) {
      normalized.start = new Date(normalized.start)
        .toISOString()
        .slice(0, 16);
    }

    if (normalized.end) {
      normalized.end = new Date(normalized.end)
        .toISOString()
        .slice(0, 16);
    }

    if (normalized.productsJSON) {
      try {
        this.productosLocal = JSON.parse(normalized.productsJSON);
      } catch (e) {
        console.error('Error parsing productsJSON', e);
      }
    }

    return normalized;
  }

  loadData() {
    const services = {
      ticket: this.ticketService.getTicketById(this.id),
      subscripcion: this.subscriptionService.getSubscriptionById(this.id),
      gasto: this.spentService.getSpentById(this.id)
    };

    services[this.tipo].subscribe({
      next: (data) => {
        const normalized = this.normalizeDataForForm(data);
        this.form.patchValue(normalized);
        this.isLoading = false;
        this.img = normalized.icon;
      },
      error: (err) => this.handleError(err)
    });
  }

  handleError(err: any) {
    console.error(err);
    this.error = 'Error cargando datos';
    this.isLoading = false;
  }

  setupAutoFields() {
    if (this.tipo !== 'subscripcion') return;

    const startCtrl = this.form.get('start');
    const endCtrl = this.form.get('end');

    startCtrl?.valueChanges.subscribe(startValue => {
      if (startValue) {
        const startDate = new Date(startValue);
        this.form.get('restartDay')?.setValue(startDate.getDate());

        const endValue = endCtrl?.value;
        if (endValue) {
          const endDate = new Date(endValue);
          const interval = Math.ceil((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
          if (interval > 0) {
            this.form.get('intervalTime')?.setValue(interval);
          }
        } else {
          this.form.get('intervalTime')?.setValue(30);
        }
      }
    });

    endCtrl?.valueChanges.subscribe(endValue => {
      const startValue = startCtrl?.value;
      if (startValue && endValue) {
        const startDate = new Date(startValue);
        const endDate = new Date(endValue);
        const interval = Math.ceil((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
        if (interval > 0) {
          this.form.get('intervalTime')?.setValue(interval);
        }
      }
    });
  }

  updateImg(event: { field: string; value: string }) {
    this.img = event.value;
  }

  calculateAccumulate() {
    const total = this.form.get('total')?.value || 0;
    const startValue = this.form.get('start')?.value || this.form.get('fechaCompra')?.value;
    const endValue = this.form.get('end')?.value;
    const activa = this.form.get('activa')?.value;
    const intervalDays = this.form.get('intervalTime')?.value || 1;

    if (!startValue || !total) {
      this.form.get('accumulate')?.setValue(0);
      return;
    }

    const startDate = new Date(startValue);
    const endDate = activa
      ? new Date()  // Fecha actual si está activa
      : (endValue ? new Date(endValue) : new Date());

    // Diferencia en milisegundos y conversión a días
    const diffTime = endDate.getTime() - startDate.getTime();
    const diffDays = diffTime > 0 ? diffTime / (1000 * 60 * 60 * 24) : 0;

    // Cantidad de intervalos completos
    const intervals = Math.floor(diffDays / intervalDays);

    const accumulateValue = total * (intervals > 0 ? intervals : 1);

    this.form.get('accumulate')?.setValue(accumulateValue);
  }

  onSubmit() {
    if (!this.form.valid) {
      this.form.markAllAsTouched();
      return;
    }

    const data = this.form.value;
    const isEdit = this.id !== 0;

    // start siempre igual a fechaCompra
    data.start = data.fechaCompra;

    // Ajustar fecha end si activa es true (limpia end)
    if (data.activa) {
      data.end = null;
    } else {
      if (!data.end) {
        data.end = new Date().toISOString();
      }
    }

    this.calculateAccumulate();
    data.accumulate = this.form.get('accumulate')?.value;

    const actions = {
      ticket: () => {
        const op = isEdit
          ? this.ticketService.updateTicket(this.id, data)
          : this.ticketService.addTicket(data);

        op.subscribe({
          next: () => {
            alert(`Ticket ${isEdit ? 'actualizado' : 'creado'}`);
            this.router.navigate(['/protected/filter/', this.tipo]);
          },
          error: (err) => {
            if (err.error === "El campo de productos no puede estar vacío.") {
              alert("Sin productos..");
            } else {
              this.handleError(err);
            }
          }
        });
      },

      subscripcion: () => {
        const op = isEdit
          ? this.subscriptionService.updateSubscription(this.id, data)
          : this.subscriptionService.addSubscription(data);

        op.subscribe({
          next: () => {
            alert(`Subscripción ${isEdit ? 'actualizada' : 'creada'}`);
            this.router.navigate(['/protected/filter/', this.tipo]);
          },
          error: (err) => this.handleError(err)
        });
      },

      gasto: () => {
        const op = isEdit
          ? this.spentService.updateSpent(this.id, data)
          : this.spentService.addSpent(data);

        op.subscribe({
          next: () => {
            alert(`Gasto ${isEdit ? 'actualizado' : 'creado'}`);
            this.router.navigate(['/protected/filter/', this.tipo]);
          },
          error: (err) => this.handleError(err)
        });
      }
    };

    actions[this.tipo]();
  }

  getCurrentForm(): FormGroup {
    return this.form;
  }

  hasError(controlName: string, error: string) {
    const control = this.form.get(controlName);
    return control?.touched && control?.hasError(error);
  }

  onProductosConfirmados(productos: Producto[]) {
    this.productosLocal = productos;
    this.form.get('productsJSON')?.setValue(JSON.stringify(productos));
  }

  onDelete() {
    if (confirm('¿Estás seguro de que quieres eliminar?')) {
      this.ticketService.deleteTicket(this.id).subscribe({
        next: () => {
          alert('Ticket eliminado');
          this.router.navigate(['/protected/filter/',this.tipo]);
        },
        error: (err) => this.handleError(err)
      });
    }
  }

  // Método para mostrar nombres legibles de tipos de gasto
  getTypeName(type: string): string {
    switch(type) {
      case ExpenseFilterClass.GASTO_GENERICO: return 'Gasto Genérico';
      case ExpenseClass.TICKET: return 'Ticket';
      case ExpenseClass.SUBSCRIPCION: return 'Suscripción';
      default: return type;
    }
  }
}
