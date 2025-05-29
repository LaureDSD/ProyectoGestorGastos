import { Component } from '@angular/core';

/**
 * Componente ApiModelsComponent
 *
 * Componente Angular vacío que actúa como contenedor para definir los modelos y DTOs
 * (Data Transfer Objects) que representan los datos que se intercambian con la API backend.
 * No contiene plantilla ni lógica.
 */
@Component({
  selector: 'app-api-models',
  standalone: false,
  template: ''
})
export class ApiModelsComponent {}

// ----------------------------------
// DTOs genéricos de la API
// ----------------------------------

/**
 * DTO que representa un usuario.
 * Contiene datos de identificación, contacto, roles y estado.
 */
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
  provider: string;
  createdAt: string; // fecha de creación en formato ISO
  updatedAt: string; // fecha de última actualización en formato ISO
}

/**
 * DTO que representa un intento de login.
 * Incluye la hora del intento y si fue exitoso.
 */
export interface LoginAttemptDto {
  id: number;
  username: string;
  attemptTime: string; // fecha y hora del intento en formato ISO
  success: boolean;
}

/**
 * DTO que representa una categoría de gastos o productos.
 */
export interface CategoryDto {
  id: number;
  name: string;
  description: string;
  iva: number; // porcentaje del IVA aplicado
}

/**
 * DTO con información general y estadísticas del servidor.
 */
export interface ServerInfoDto {
  // Datos generales del servidor
  name: string;
  users: number;
  activeocr: boolean;      // OCR activo o no
  spenses: number;         // cantidad de gastos procesados
  activeapi: boolean;      // API activa o no
  storage: number;         // almacenamiento total en GB
  usedStorage: number;     // almacenamiento usado en GB
  createdAt: string;       // fecha de creación en ISO
  updatedAt: string;       // fecha de última actualización en ISO

  // Estadísticas del sistema
  os: string;              // sistema operativo
  cpuLoad: number;         // carga CPU en porcentaje (0-100)
  uptimeSeconds: number;   // tiempo de actividad en segundos
  totalMemory: number;     // memoria total en bytes
  usedMemory: number;      // memoria usada en bytes
  totalDisk: number;       // espacio total en disco en bytes
  usedDisk: number;        // espacio usado en disco en bytes
  cpuTemperature: number;  // temperatura CPU en grados Celsius
  info : string; //Informaciondel servidor python
}


// ----------------------------------
// Enumeraciones de clases de gasto y filtros
// ----------------------------------

/**
 * Enumeración para tipos de gasto reconocidos en la aplicación.
 */
export enum ExpenseClass {
  TICKET           = 'TICKET',
  FACTURA          = 'FACTURA',
  SUBSCRIPCION     = 'SUBSCRIPCION',
  GASTO_GENERICO   = 'GASTO_GENERICO',
  TRANSFERENCIA    = 'TRANSFERENCIA'
}

/**
 * Enumeración para filtros de gastos que se pueden aplicar en algunas vistas.
 */
export enum ExpenseFilterClass {
  FACTURA          = 'FACTURA',
  GASTO_GENERICO   = 'GASTO_GENERICO',
  TRANSFERENCIA    = 'TRANSFERENCIA'
}

// ----------------------------------
// Interfaces base y extendidas para gastos ("spents")
// ----------------------------------

/**
 * Interface base común para todos los tipos de gastos.
 */
export interface BaseSpentDto {
  spentId: number;          // identificador del gasto
  userId: number;           // id del usuario que generó el gasto
  categoriaId: number;      // id de la categoría del gasto
  name: string;             // nombre o título del gasto
  description?: string;     // descripción opcional
  icon?: string;            // icono asociado opcional
  fechaCompra: string;      // fecha de compra en formato ISO
  total: number;            // total del gasto (importe)
  iva: number;              // porcentaje de IVA aplicado
  typeExpense: ExpenseClass;// tipo de gasto según enumeración ExpenseClass
}

/**
 * DTO específico para gastos tipo Ticket.
 * Añade tienda y lista de productos en formato JSON.
 */
export interface TicketDto extends BaseSpentDto {
  store: string;            // nombre de la tienda donde se realizó la compra
  productsJSON: string;     // lista de productos en formato JSON string
}

/**
 * DTO específico para gastos tipo Suscripción.
 * Añade detalles de periodicidad, fechas y estado.
 */
export interface SubscriptionDto extends BaseSpentDto {
  start: string;            // fecha de inicio en formato ISO
  end: string | null;       // fecha de fin o null si indefinido
  accumulate: number;       // acumulado o contador (numérico)
  restartDay: number;       // día del reinicio del ciclo
  intervalTime: number;     // intervalo de tiempo en días u otra unidad
  activa: boolean;          // estado activo/inactivo de la suscripción
}

/**
 * DTO genérico para gastos no específicos.
 */
export type SpentDto = BaseSpentDto;

/**
 * DTO que contiene toda la información de un gasto,
 * incluyendo propiedades opcionales específicas para cada tipo.
 */
export interface SpentFullDto {
  // Campos comunes
  spentId: number;
  userId: number;
  categoriaId: number;
  name: string;
  description?: string;
  icon?: string;
  fechaCompra: string;
  total: number;
  iva: number;
  typeExpense: ExpenseClass;

  // Campos opcionales para Ticket
  store?: string;
  productsJSON?: string;

  // Campos opcionales para Subscription
  start?: string;
  end?: string | null;
  accumulate?: number;
  restartDay?: number;
  intervalTime?: number;
  activa?: boolean;
}


// ----------------------------------
// Unión discriminada para tipos de gasto
// ----------------------------------

/**
 * Tipo discriminado que representa cualquier gasto posible,
 * con propiedades adicionales según el tipoExpense.
 */
export type AnySpentDto
  = (TicketDto & { typeExpense: ExpenseClass.TICKET })
  | (SubscriptionDto & { typeExpense: ExpenseClass.SUBSCRIPCION })
  | (SpentDto & { typeExpense: ExpenseClass.GASTO_GENERICO | ExpenseClass.FACTURA | ExpenseClass.TRANSFERENCIA });
