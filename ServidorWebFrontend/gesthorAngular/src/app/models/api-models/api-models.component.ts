import { Component } from '@angular/core';

@Component({
  selector: 'app-api-models',
  standalone: false,
  template: ''
})
export class ApiModelsComponent {}

// ----------------------------------
// DTOs genéricos de la API
// ----------------------------------

export interface UserDto {
  id: number;
  name: string;
  username: string;
  email: string;
  phone: string;
  address: string;
  imageUrl: string;
  server: string;
  role: string;
  active: boolean;
  fv2: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface LoginAttemptDto {
  id: number;
  username: string;
  attemptTime: string;
  success: boolean;
}

export interface CategoryDto {
  id: number;
  name: string;
  description: string;
  iva: number;
}

export interface ServerInfoDto {
  name: string;
  users: number;
  spenses: number;
  active: boolean;
}

// ----------------------------------
// ExpenseClass y filtros
// ----------------------------------

export enum ExpenseClass {
  TICKET           = 'TICKET',
  FACTURA          = 'FACTURA',
  SUBSCRIPCION     = 'SUBSCRIPCION',
  GASTO_GENERICO   = 'GASTO_GENERICO',
  TRANSFERENCIA    = 'TRANSFERENCIA'
}

// ----------------------------------
// BaseSpent y extensiones
// ----------------------------------

/**
 * Campos comunes a todos los “spents”
 */
export interface BaseSpentDto {
  spentId:    number;
  userId:     number;
  categoriaId:number;
  name:       string;
  description?: string;
  icon?:      string;
  fechaCompra: string;
  total:      number;
  iva:        number;
  typeExpense:ExpenseClass;
}

/**
 * Ticket: añade tienda y lista de productos
 */
export interface TicketDto extends BaseSpentDto {
  store: string;
  productsJSON:string; // JSON string o bien array tipado si prefieres
}

/**
 * Subscription: añade periodicidad, fechas y estado
 */
export interface SubscriptionDto extends BaseSpentDto {
  start:       string;
  end:         string | null;
  accumulate:  number;
  restartDay:  number;
  intervalTime:number;
  activa:      boolean;
}

/**
 * Gasto genérico: no tiene campos extra, reusa BaseSpentDto
 */
export type SpentDto = BaseSpentDto;

// ----------------------------------
// Unión discriminada si la necesitas
// ----------------------------------

export type AnySpentDto
  = (TicketDto & { typeExpense: ExpenseClass.TICKET })
  | (SubscriptionDto & { typeExpense: ExpenseClass.SUBSCRIPCION })
  | (SpentDto & { typeExpense: ExpenseClass.GASTO_GENERICO | ExpenseClass.FACTURA | ExpenseClass.TRANSFERENCIA });
