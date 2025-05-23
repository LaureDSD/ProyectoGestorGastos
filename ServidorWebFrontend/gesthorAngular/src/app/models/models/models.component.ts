import { Component } from '@angular/core';

/**
 * Componente ModelsComponent
 *
 * Componente Angular vacío que actúa como contenedor para definir modelos de datos (interfaces)
 * usados en la aplicación. No tiene plantilla ni lógica.
 */
@Component({
  selector: 'app-models',
  standalone: false, // No es standalone, debe estar declarado en un módulo
  template: ``        // Plantilla vacía porque solo contiene interfaces
})
export class ModelsComponent {}

/**
 * Interfaz Producto
 *
 * Define la estructura de un producto utilizado en la aplicación.
 *
 * Propiedades:
 * - nombre: nombre del producto (string).
 * - categorias: arreglo opcional de categorías a las que pertenece el producto (string[]).
 * - cantidad: cantidad del producto (number).
 * - precio: precio unitario del producto (number).
 * - subtotal: subtotal calculado opcional (number).
 */
export interface Producto {
  nombre: string;
  categorias?: string[];
  cantidad: number;
  precio: number;
  subtotal?: number;
}

/**
 * Interfaz Contacto
 *
 * Define la estructura de un mensaje de contacto enviado por un usuario.
 *
 * Propiedades:
 * - nombre: nombre del remitente (string).
 * - correo: correo electrónico del remitente (string).
 * - asunto: asunto del mensaje (string).
 * - mensaje: cuerpo o contenido del mensaje (string).
 */
export interface Contacto {
  nombre: string;
  correo: string;
  asunto: string;
  mensaje: string;
}

/**
 * Interfaz ChatMessage
 *
 * Define la estructura de un mensaje dentro de un chat entre usuario y asistente.
 *
 * Propiedades:
 * - role: indica el emisor del mensaje, puede ser 'user' (usuario) o 'bot' (asistente).
 * - content: texto del mensaje enviado (string).
 * - timestamp: fecha y hora en la que se envió el mensaje (Date).
 */
export interface ChatMessage {
  role: 'user' | 'bot';
  content: string;
  timestamp: Date;
}
