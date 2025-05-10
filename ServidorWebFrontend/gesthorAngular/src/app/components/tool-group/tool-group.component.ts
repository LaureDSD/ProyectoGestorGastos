import { Router } from '@angular/router';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-tool-group',
  standalone: false,
  templateUrl: './tool-group.component.html',
  styleUrl: './tool-group.component.css'
})
export class ToolGroupComponent {

  @Input() herramientas = [
    {
      nombre: 'Nombre',
      icono: '/icon/icons8-pencil.gif',
      descripcion: 'Descripcion',
      accion: 'Cargar',
      ruta: '/ruta'
    }
  ]

  constructor(private router :Router){}

  navegar(ruta: string) {
    this.router.navigate([ruta]);
  }

}
