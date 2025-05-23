// Importa las funcionalidades necesarias desde Angular core y router
import { Component } from '@angular/core';
import { Router } from '@angular/router';

// Decorador que define el componente y sus metadatos
@Component({
  selector: 'app-tools',                   // Nombre del selector utilizado en las plantillas HTML
  standalone: false,                       // Indica que este componente no es standalone
  templateUrl: './tools.component.html',   // Ruta al archivo HTML del componente
  styleUrl: './tools.component.css'        // Ruta al archivo CSS del componente
})
export class ToolsComponent {

  // Constructor que inyecta el servicio Router para navegación programática
  constructor(private router: Router) {}

  // Lista de herramientas disponibles para subir tickets digitales o imágenes
  herramientas = [
    {
      nombre: 'Cargar Ticket Digital',                 // Nombre que se mostrará en la interfaz
      icono: '/icon/icons8-upload-100.png',            // Ruta del icono representativo
      descripcion: 'Sube un ticket digital (PDF o XML) para registrar un gasto automáticamente.', // Descripción de la funcionalidad
      accion: 'Cargar',                                // Texto del botón o acción
      ruta: '/protected/tool/ticketdigital'            // Ruta de navegación cuando se selecciona esta herramienta
    },
    {
      nombre: 'Cargar Ticket Imagen',
      icono: '/icon/icons8-upload-100.png',
      descripcion: 'Sube una imagen de ticket para extraer datos mediante OCR (Baja eficiencia).',
      accion: 'Cargar',
      ruta: '/protected/tool/ticketimagen'
    },
  ];

  // Lista de herramientas para crear nuevos registros (ticket, gasto, suscripción)
  herramientas1 = [
    {
      nombre: 'Crear Ticket',
      icono: '/icon/icons8-crear-nuevo-100.png',
      descripcion: 'Ingresa los datos de un ticket manualmente.',
      accion: 'Crear',
      ruta: '/protected/tool/crearticket'
    },
    {
      nombre: 'Crear Gasto',
      icono: '/icon/icons8-crear-nuevo-100.png',
      descripcion: 'Registra un gasto sin ticket asociado.',
      accion: 'Crear',
      ruta: '/protected/tool/creargasto'
    },
    {
      nombre: 'Crear Subscripciones',
      icono: '/icon/icons8-crear-nuevo-100.png',
      descripcion: 'Añade una nueva suscripción periódica (como Netflix o Spotify).',
      accion: 'Crear',
      ruta: '/protected/tool/crearsubscripcion'
    },
  ];

  // Lista de herramientas para gestionar registros ya creados
  herramientas2 = [
    {
      nombre: 'Gestionar Subscripciones',
      icono: '/icon/icons8-buscar-100.png',
      descripcion: 'Consulta, edita o elimina tus suscripciones existentes.',
      accion: 'Gestionar',
      ruta: '/protected/tool/editarsubscripcion'
    },
    {
      nombre: 'Gestionar Gastos',
      icono: '/icon/icons8-buscar-100.png',
      descripcion: 'Visualiza y edita todos los gastos registrados manual o automáticamente.',
      accion: 'Gestionar',
      ruta: '/protected/tool/editargasto'
    },
    {
      nombre: 'Gestionar Tickets',
      icono: '/icon/icons8-buscar-100.png',
      descripcion: 'Consulta, edita o elimina tickets registrados.',
      accion: 'Gestionar',
      ruta: '/protected/tool/editarticket'
    }
  ];

  /**
   * Método para redirigir al usuario a la ruta especificada al hacer clic en una herramienta.
   * @param ruta Ruta relativa dentro de la aplicación a la cual se debe navegar.
   */
  navegar(ruta: string) {
    this.router.navigate([ruta]);
  }

}
