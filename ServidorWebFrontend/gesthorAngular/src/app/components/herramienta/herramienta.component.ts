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

  constructor(private route: ActivatedRoute, private router: Router) {}

  ngOnInit() {
    this.tipo = this.route.snapshot.paramMap.get('tipo') || '';

    // Redirección directa para crear o editar
    if (['crearticket', 'creargasto', 'crearsubscripcion'].includes(this.tipo)) {
      this.router.navigate(['/protected/create', this.tipo]);
    }

    if (['editarticket', 'editargasto', 'editarsubscripcion'].includes(this.tipo)) {
      const fakeId = '123'; // Esto se reemplazará con el ID real en el futuro
      this.router.navigate(['/protected/edit', this.tipo.replace('editar', ''), fakeId]);
    }
  }

  onFileSelected(event: any) {
    this.file = event.target.files[0];
  }

  subirArchivoYEditar() {
    if (!this.file) return;

    // Simulación de subida de archivo
    setTimeout(() => {
      const fakeId = '123'; // Simula la respuesta del backend
      this.router.navigate(['/protected/edit', this.tipo, fakeId]);
    }, 1000);
  }
}
