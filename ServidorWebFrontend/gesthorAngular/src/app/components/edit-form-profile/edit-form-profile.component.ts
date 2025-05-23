/**
 * EditFormProfileComponent
 *
 * Componente Angular para la edición y visualización de la imagen de perfil de un usuario o imagen asociada a un gasto.
 * Permite mostrar la imagen actual, cambiarla mediante carga de un archivo, y visualizarla en un modal ampliado con opciones
 * para cerrar, descargar o editar la imagen.
 *
 * Entradas (@Input):
 * - img: URL o nombre del archivo de la imagen actual que se muestra.
 * - field: Nombre del campo relacionado con la imagen que se editará (se emite al guardar).
 * - defaultImg: Imagen por defecto a mostrar cuando no hay imagen definida.
 * - icon: URL o path del icono que aparece en el botón para editar la imagen.
 * - spent: Id numérico opcional que indica si la imagen pertenece a un gasto. Si es null, se asume imagen de perfil.
 *
 * Salidas (@Output):
 * - save: Evento que emite un objeto con la propiedad `field` y el nuevo valor `value` (la URL de la imagen subida).
 *
 * Comportamiento:
 * - Muestra la imagen actual o la imagen por defecto.
 * - Permite seleccionar un archivo de imagen desde el sistema.
 * - Al seleccionar un archivo, sube la imagen al servidor llamando al servicio correspondiente (usuario o gasto).
 * - Abre un modal con la imagen ampliada y opciones para cerrar, descargar o editar la imagen.
 */

import { Component, EventEmitter, Input, Output } from '@angular/core';
import { UserserviceService } from '../../services/userservice.service';
import { environment } from '../../environments/environment';
import { SpentService } from '../../services/spent.service';

@Component({
  selector: 'app-edit-form-profile',
  standalone: false,
  template: `
    <!-- Contenedor principal para la imagen de perfil con estilos oscuros -->
    <li class="list-group-item bg-dark text-light d-flex justify-content-center align-items-center border-secondary">
      <div class="position-relative mt-4 mb-4" style="width: 250px; height: 250px;">

        <!-- Imagen visible, clickeable para abrir el modal de imagen ampliada -->
        <img
          *ngIf="profile !== undefined"
          [src]="img ? (server + 'uploads/' + img) : defaultImg"
          alt="Perfil"
          class="w-100 h-100 rounded shadow"
          style="object-fit: cover; border: 4px solid white; cursor: pointer;"
          (click)="openImageModal()" />

        <!-- Botón para disparar el input file para seleccionar nueva imagen -->
        <button
          type="button"
          class="btn btn-light btn-sm position-absolute bottom-0 end-0 m-3 p-2 rounded-circle shadow"
          (click)="fileInput.click()"
          title="Editar foto de perfil">
          <img [src]="icon" style="height: 25px;" />
        </button>

        <!-- Input file oculto para cargar la imagen nueva -->
        <input type="file" #fileInput hidden (change)="onFileSelected($event)" accept="image/*" />
      </div>
    </li>

    <!-- Modal para mostrar imagen ampliada con opciones -->
    <div *ngIf="isImageModalOpen" class="image-modal-overlay" (click)="closeImageModal()">
      <div class="image-modal-content" (click)="$event.stopPropagation()">

        <!-- Controles del modal: cerrar, descargar y editar -->
        <div class="image-modal-controls">
          <button type="button" class="btn btn-sm btn-light me-2" (click)="closeImageModal()">Cerrar</button>

          <a
            [href]="img ? (server + 'uploads/' + img) : defaultImg"
            download
            class="btn btn-sm btn-light me-2"
            (click)="$event.stopPropagation()">
            Descargar
          </a>

          <button type="button" class="btn btn-sm btn-light" (click)="fileInput.click(); $event.stopPropagation()">
            Editar
          </button>
        </div>

        <!-- Imagen ampliada en modal -->
        <img [src]="img ? (server + 'uploads/' + img) : defaultImg" alt="Imagen ampliada" class="image-modal-img" />

        <!-- Mostrar nombre o path de la imagen (opcional para debug) -->
        <p>{{ img }}</p>

        <!-- Input file oculto duplicado para el modal (permite cargar imagen desde modal) -->
        <input type="file" #fileInput hidden (change)="onFileSelected($event)" accept="image/*" />
      </div>
    </div>
  `
})
export class EditFormProfileComponent {
  /**
   * URL base del servidor API para construir rutas de imágenes subidas.
   */
  server: string = `${environment.apiUrl}/`;

  /**
   * URL o nombre de la imagen actual cargada (se actualiza tras subir una nueva imagen).
   */
  profile: string = "";

  /**
   * Nombre o ruta relativa de la imagen recibida desde el componente padre.
   */
  @Input() img: string = '';

  /**
   * Nombre del campo asociado a la imagen para emitir al guardar.
   */
  @Input() field = '';

  /**
   * Imagen por defecto que se muestra cuando no hay imagen definida.
   */
  @Input() defaultImg = '/icon/icons8-upload-100.png';

  /**
   * Icono que se muestra en el botón para editar la imagen.
   */
  @Input() icon = '';

  /**
   * ID del gasto asociado a la imagen. Si es null, se asume que la imagen es perfil de usuario.
   */
  @Input() spent: number | null = null;

  /**
   * Evento que emite un objeto con el campo y la nueva URL de la imagen tras subirla correctamente.
   */
  @Output() save = new EventEmitter<{ field: string; value: string }>();

  /**
   * Bandera que controla la visibilidad del modal para mostrar la imagen ampliada.
   */
  isImageModalOpen = false;

  /**
   * Constructor con inyección de servicios para usuario y gasto.
   * @param userService Servicio para gestión de usuario, subida de foto de perfil.
   * @param spentService Servicio para gestión de gastos, subida de foto de gasto.
   */
  constructor(
    private userService: UserserviceService,
    private spentService: SpentService
  ) {}

  /**
   * Método que se dispara cuando se selecciona un archivo en el input file.
   * Realiza la subida del archivo al servidor usando el servicio correspondiente según si es imagen de perfil o de gasto.
   * Emite el evento 'save' con el nuevo valor para actualizar datos en el componente padre.
   * @param event Evento del input file con la imagen seleccionada.
   */
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    const file = input.files[0];
    const formData = new FormData();
    formData.append('image', file);

    if (this.spent == null) {
      // Subida imagen de perfil usuario
      this.userService.subirFotoPerfil(formData).subscribe({
        next: (res) => {
          this.profile = res.url;
          this.save.emit({ field: this.field, value: this.profile });
        },
        error: () => {
          alert('Error al subir la imagen.');
        }
      });
    } else {
      // Subida imagen asociada a gasto
      this.spentService.subirFotoGasto(this.spent, file).subscribe({
        next: (res) => {
          console.log("Subiendo imagen con gasto ID:", this.spent);
          this.profile = res.url;
          this.save.emit({ field: this.field, value: this.profile });
        },
        error: () => {
          alert('Error al subir la imagen.');
        }
      });
    }
  }

  /**
   * Abre el modal para mostrar la imagen ampliada.
   */
  openImageModal() {
    this.isImageModalOpen = true;
  }

  /**
   * Cierra el modal de imagen ampliada.
   */
  closeImageModal() {
    this.isImageModalOpen = false;
  }
}
