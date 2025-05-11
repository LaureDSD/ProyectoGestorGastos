
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Producto } from '../../../models/models/models.component';

import { SpentService } from '../../../services/spent.service';
import { SubscriptionService } from '../../../services/subscription.service';
import { TicketService } from '../../../services/ticket.service';
import { Category } from '../../../models/api-models/api-models.component';
import { CategoryService } from '../../../services/categories.service';


@Component({
  selector: 'app-form-herramienta',
  standalone: false,
  templateUrl: './form-herramienta.component.html',
  styleUrl: './form-herramienta.component.css'
})
export class FormHerramientaComponent implements OnInit {
  tipo: 'ticket' | 'subscripcion' | 'gasto' = 'ticket';
  id!: number;
  productosLocal: Producto[] = [];
  formTicket!: FormGroup;
  formSubscripcion!: FormGroup;
  formGasto!: FormGroup;
  isLoading = true;
  categories : Category[] = [];
  error = '';


  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private spentService : SpentService,
    private subscriptionService : SubscriptionService,
    private ticketService : TicketService,
    private router : Router,
    private categoryService: CategoryService
  ) {
    this.categoryService.getCategories().subscribe(categories => {
      this.categories = categories;
    });
  }

  ngOnInit(): void {
    this.tipo = this.route.snapshot.paramMap.get('tipo') as any;
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.initForms();

    if (this.id !== 0) {
      this.loadData();
    } else {
      this.isLoading = false;
      this.setupAutoFields();
    }
  }

  initForms() {
    this.formTicket = this.fb.group({
      spentId: [this.id],
      userId: [1, Validators.required],
      categoriaId: [1, Validators.required],
      name: ['', Validators.required],
      description: [''],
      icon: [''],
      fechaCompra: ['', Validators.required],
      total: [0, [Validators.required, Validators.min(0.01)]],
      iva: [0, [Validators.required, Validators.min(0)]],
      store: ['', Validators.required],
      productsJSON: ['']
    });

    this.formSubscripcion = this.fb.group({
      spentId: [this.id],
      userId: [1, Validators.required],
      name: ['', Validators.required],
      description: [''],
      icon: [''],
      fechaCompra: ['', Validators.required],
      total: [0, [Validators.required, Validators.min(0.01)]],
      iva: [0, [Validators.required, Validators.min(0)]],
      start: ['', Validators.required],
      end: [''],
      accumulate: [0, [Validators.required, Validators.min(0)]],
      restartDay: ["", [Validators.required, Validators.min(1), Validators.max(31)]],
      intervalTime: [1, [Validators.required, Validators.min(1)]],
      activa: [true, Validators.required]
    });

    this.formGasto = this.fb.group({
      spentId: [this.id],
      userId: [1, Validators.required],
      categoriaId: [1, Validators.required],
      name: ['', Validators.required],
      description: [''],
      icon: [''],
      fechaCompra: ['', Validators.required],
      total: [0, [Validators.required, Validators.min(0.01)]],
      iva: [0, [Validators.required, Validators.min(0)]],
      typeExpense: ['', Validators.required]
    });
  }

  loadData() {
  const handlers = {
    ticket: () => this.ticketService.getTicketById(this.id).subscribe({
  next: (ticket) => {

    const fechaFormateada = new Date(ticket.fechaCompra).toISOString().slice(0, 16);
    this.formTicket.patchValue({
      ...ticket,
      fechaCompra: fechaFormateada
    });
    if (ticket.productsJSON) {
      this.productosLocal = JSON.parse(ticket.productsJSON);
    }
    this.isLoading = false;
  },
  error: (err) => this.handleError(err)
}),

    subscripcion: () => this.subscriptionService.getSubscriptionById(this.id).subscribe({
      next: (sub) => {
        this.formSubscripcion.patchValue(sub);
        this.isLoading = false;
      },
      error: (err) => this.handleError(err)
    }),

    gasto: () => this.spentService.getSpentById(this.id).subscribe({
      next: (gasto) => {
        this.formGasto.patchValue(gasto);
        this.isLoading = false;
      },
      error: (err) => this.handleError(err)
    })
  };
  handlers[this.tipo]();
}

handleError(err: any) {
  console.error(err);
  this.error = 'Error cargando datos';
  this.isLoading = false;
}

  setupAutoFields() {
    if (this.tipo !== 'subscripcion') return;

    const startCtrl = this.formSubscripcion.get('start');
    const endCtrl = this.formSubscripcion.get('end');

    startCtrl?.valueChanges.subscribe(startValue => {
      if (startValue) {
        const startDate = new Date(startValue);
        this.formSubscripcion.get('restartDay')?.setValue(startDate.getDate());

        const endValue = endCtrl?.value;
        if (endValue) {
          const endDate = new Date(endValue);
          const interval = Math.ceil((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
          if (interval > 0) {
            this.formSubscripcion.get('intervalTime')?.setValue(interval);
          }
        } else {
          this.formSubscripcion.get('intervalTime')?.setValue(30);
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
          this.formSubscripcion.get('intervalTime')?.setValue(interval);
        }
      }
    });
  }

 onSubmit() {
  const form = this.getCurrentForm();
  if (!form.valid) {
    form.markAllAsTouched();
    return;
  }

  const data = form.value;
  const isEdit = this.id !== 0;

  const actions = {
    ticket: () => {
      if (isEdit) {
        this.ticketService.updateTicket(this.id, data).subscribe({
          next: () => alert('Ticket actualizado'),
          error: (err) => this.handleError(err)
        });
      } else {
        this.ticketService.addTicket(data).subscribe({
          next: () => alert('Ticket creado'),
          error: (err) => {
            if (err.error === "El campo de productos no puede estar vacío.") {
              alert("Sin productos..");
            } else {
              alert("Error al guardar.");
            }
          }
        });
      }
    },

    subscripcion: () => {
      if (isEdit) {
        this.subscriptionService.updateSubscription(this.id, data).subscribe({
          next: () => alert('Subscripción actualizada'),
          error: (err) => this.handleError(err)
        });
      } else {
        this.subscriptionService.addSubscription(data).subscribe({
          next: () => alert('Subscripción creada'),
          error: (err) => this.handleError(err)
        });
      }
    },

    gasto: () => {
      if (isEdit) {
        this.spentService.updateSpent(this.id, data).subscribe({
          next: () => alert('Gasto actualizado'),
          error: (err) => this.handleError(err)
        });
      } else {
        this.spentService.addSpent(data).subscribe({
          next: () => alert('Gasto creado'),
          error: (err) => this.handleError(err)
        });
      }
    }
  };

  actions[this.tipo]();
}

  getCurrentForm(): FormGroup {
    switch (this.tipo) {
      case 'ticket': return this.formTicket;
      case 'subscripcion': return this.formSubscripcion;
      case 'gasto': return this.formGasto;
    }
  }

  hasError(controlName: string, error: string) {
    const control = this.getCurrentForm().get(controlName);
    return control?.touched && control?.hasError(error);
  }

  onProductosConfirmados(productos: Producto[]) {
    this.productosLocal = productos;
    this.formTicket.get('productsJSON')?.setValue(JSON.stringify(productos));
  }

  onDelete() {
  if (confirm('¿Estás seguro de que quieres eliminar este ticket?')) {
    this.ticketService.deleteTicket(this.id).subscribe({
      next: () => {
        alert('Ticket eliminado');
        this.router.navigate(['/protected/tools']);
      },
      error: (err) => this.handleError(err)
    });
  }
}

}
