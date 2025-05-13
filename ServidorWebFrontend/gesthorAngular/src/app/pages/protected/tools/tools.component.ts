import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-tools',
  standalone: false,
  templateUrl: './tools.component.html',
  styleUrl: './tools.component.css'
})
export class ToolsComponent {
  constructor(private router: Router) {}

  herramientas = [
    {
      nombre: 'Cargar Ticket Digital',
      icono: '/icon/icons8-upload-100.png',
      descripcion: 'Sube un ticket digital (PDF o XML) para registrar un gasto automáticamente.',
      accion: 'Cargar',
      ruta: '/protected/tool/ticketdigital'
    },
    {
      nombre: 'Cargar Ticket Imagen',
      icono: '/icon/icons8-upload-100.png',
      descripcion: 'Sube una imagen de ticket para extraer datos mediante OCR (Baja eficiencia).',
      accion: 'Cargar',
      ruta: '/protected/tool/ticketimagen'
    },]


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
      nombre: 'Crear Subs',
      icono: '/icon/icons8-crear-nuevo-100.png',
      descripcion: 'Añade una nueva suscripción periódica (como Netflix o Spotify).',
      accion: 'Crear',
      ruta: '/protected/tool/crearsubscripcion'
    },]


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

  navegar(ruta: string) {
    this.router.navigate([ruta]);
  }

}
