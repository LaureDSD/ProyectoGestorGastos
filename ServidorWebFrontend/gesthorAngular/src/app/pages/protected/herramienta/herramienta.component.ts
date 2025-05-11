import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OcrService } from '../../../services/ocr.service';

@Component({
  selector: 'app-herramienta',
  standalone: false,
  templateUrl: './herramienta.component.html'
})
export class HerramientaComponent {
 tipo: string = '';
  file?: File;
  imagePreview: string | ArrayBuffer | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private ocrService : OcrService) {}

  ngOnInit() {
    this.tipo = this.route.snapshot.paramMap.get('tipo') || '';

    if (['crearticket', 'creargasto', 'crearsubscripcion'].includes(this.tipo)) {
      this.router.navigate(['/protected/form', this.tipo.replace('crear', ''), 0]);
    }

    if (['editarticket', 'editargasto', 'editarsubscripcion'].includes(this.tipo)) {
      this.router.navigate(['/protected/filter', this.tipo.replace('editar', '')]);
    }
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    this.file = file;

    if (file.type.match('image.*')) {
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
      };
      reader.readAsDataURL(file);
    } else {
      this.imagePreview = null;
    }
  }

  subirArchivoYEditar() {
    if (!this.file) return;

    if (this.tipo === 'ticketimagen') {
      this.ocrService.procesarTicketImagen(this.file).subscribe(
        (response: any) => {
          const ticketId = response.id;
          this.router.navigate(['/protected/form', 'ticket', ticketId]);
        },
        (error) => {
          console.error('Error al procesar el archivo de imagen:', error);
        }
      );
    } else if (this.tipo === 'ticketdigital') {
      this.ocrService.procesarTicketDigital(this.file).subscribe(
        (response: any) => {
          const ticketId = response.id;
          this.router.navigate(['/protected/form', 'ticketdigital', ticketId]);
        },
        (error) => {
          console.error('Error al procesar el archivo digital:', error);
        }
      );
    }
  }

}

