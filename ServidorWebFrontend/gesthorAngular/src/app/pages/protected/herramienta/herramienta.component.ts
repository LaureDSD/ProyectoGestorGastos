/**
 * Componente HerramientaComponent
 *
 * Este componente Angular permite gestionar diferentes acciones relacionadas con la creación y edición
 * de tickets, gastos y suscripciones, así como el procesamiento de archivos mediante OCR para tickets
 * en formato imagen o digital.
 *
 * Funcionalidades principales:
 * - Redireccionar automáticamente a formularios o filtros según el parámetro 'tipo' recibido en la URL.
 * - Permitir la selección de archivos (imágenes o digitales) para su posterior procesamiento.
 * - Mostrar previsualización de imagen seleccionada.
 * - Enviar archivos al servicio OCR para extraer datos de tickets y redirigir al formulario de edición con el ID obtenido.
 * - Mostrar estados de carga y manejo de errores durante el procesamiento.
 */

import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OcrService } from '../../../services/ocr.service';

@Component({
  selector: 'app-herramienta',
  standalone: false,
  templateUrl: './herramienta.component.html'
})
export class HerramientaComponent {
  /** Tipo de acción que se quiere realizar, obtenido del parámetro de la ruta. */
  tipo: string = '';

  /** Archivo seleccionado por el usuario para procesar (imagen o archivo digital). */
  file?: File;

  /** Contenido base64 o ArrayBuffer para mostrar previsualización de imagen. */
  imagePreview: string | ArrayBuffer | null = null;

  /** Indicador booleano que representa si el sistema está procesando una petición. */
  loading: boolean = false;

  /** Mensaje de error para mostrar en caso de fallos durante el procesamiento. */
  error: string | null = null;

  /**
   * Constructor que inyecta servicios necesarios:
   * - ActivatedRoute para obtener parámetros de la ruta activa.
   * - Router para realizar redirecciones programáticas.
   * - OcrService para el procesamiento OCR de tickets.
   */
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private ocrService: OcrService
  ) {}

  /**
   * Método del ciclo de vida OnInit:
   * - Obtiene el parámetro 'tipo' de la ruta.
   * - Redirecciona automáticamente según el tipo recibido para crear o editar tickets, gastos o suscripciones.
   */
  ngOnInit() {
    this.tipo = this.route.snapshot.paramMap.get('tipo') || '';

    // Redirigir a formularios para creación de entidades
    if (['crearticket', 'creargasto', 'crearsubscripcion'].includes(this.tipo)) {
      this.router.navigate(['/protected/form', this.tipo.replace('crear', ''), 0]);
    }

    // Redirigir a filtros para edición de entidades
    if (['editarticket', 'editargasto', 'editarsubscripcion'].includes(this.tipo)) {
      this.router.navigate(['/protected/filter', this.tipo.replace('editar', '')]);
    }
  }

  /**
   * Evento que se ejecuta al seleccionar un archivo desde el input de archivos.
   * - Guarda el archivo seleccionado en la propiedad 'file'.
   * - Si el archivo es una imagen, crea una previsualización en base64 para mostrar.
   * @param event Evento del input file con el archivo seleccionado.
   */
  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    this.file = file;

    // Si el archivo es imagen, generar previsualización base64
    if (file.type.match('image.*')) {
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
      };
      reader.readAsDataURL(file);
    } else {
      // Si no es imagen, limpiar previsualización
      this.imagePreview = null;
    }
  }

  /**
   * Método para enviar el archivo seleccionado al servicio OCR y luego redirigir al formulario de edición.
   * - Cambia el estado 'loading' a true mientras procesa la solicitud.
   * - Dependiendo del tipo, llama al método correspondiente del servicio OCR.
   * - Al recibir respuesta, extrae el ID del ticket procesado y redirige.
   * - Maneja errores mostrando un mensaje adecuado y restablece el estado 'loading'.
   */
  subirArchivoYEditar() {
    if (!this.file) return;

    this.loading = true;

    if (this.tipo === 'ticketimagen') {
      this.ocrService.procesarTicketImagen(this.file).subscribe(
        (response: any) => {
          const ticketId = response.spentId;
          this.loading = false;

          if (ticketId) {
            console.log('Ticket ID:', ticketId);
            this.router.navigate(['/protected/form', 'ticket', ticketId]);
          } else {
            console.error('No se recibió un ID válido del ticket');
          }
        },
        (error) => {
          this.loading = false;
          this.error = 'Error al procesar la imagen. Verifica el archivo e inténtalo nuevamente.';
          console.error('Error al procesar el archivo de imagen:', error);
        }
      );
    } else if (this.tipo === 'ticketdigital') {
      this.ocrService.procesarTicketDigital(this.file).subscribe(
        (response: any) => {
          const ticketId = response.spentId;
          this.loading = false;

          if (ticketId) {
            console.log('Ticket Digital ID:', ticketId);
            this.router.navigate(['/protected/form', 'ticket', ticketId]);
          } else {
            console.error('No se recibió un ID válido del ticket digital');
          }
        },
        (error) => {
          this.loading = false;
          this.error = 'Error al procesar el ticket digital. Verifica el archivo e inténtalo nuevamente.';
          console.error('Error al procesar el archivo digital:', error);
        }
      );
    }
  }
}
