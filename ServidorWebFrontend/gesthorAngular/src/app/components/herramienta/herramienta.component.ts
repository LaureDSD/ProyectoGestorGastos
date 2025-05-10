import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-herramienta',
  standalone: false,
  templateUrl: './herramienta.component.html'
})
export class HerramientaComponent {
 tipo: string = '';
  file?: File;
  imagePreview: string | ArrayBuffer | null = null;

  constructor(private route: ActivatedRoute, private router: Router) {}

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

    // Mostrar vista previa solo si es imagen
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
    var Id = '';
    switch (this.tipo) {
      case 'ticketdigital':
        // Lógica para ticket digital
        break;
      case 'ticketimagen':
        // Lógica para ticket imagen
        break;
      default:
        console.log("No válido");
        break;
    }
    setTimeout(() => {
      this.router.navigate(['/protected/form', this.tipo.replace('imagen', '').replace('digital', ''), Id]);
    }, 1000);
  }
}

