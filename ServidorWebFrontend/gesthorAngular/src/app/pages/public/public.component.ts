import { Route, Router } from '@angular/router';
import { ApiserviceService } from './../../services/apiservice.service';
import { Component, HostListener, Input, OnInit } from '@angular/core';

/**
 * Componente PublicComponent
 *
 * Componente encargado de manejar la vista pública de la aplicación.
 *
 * Características principales:
 * - Selector 'app-public' para ser utilizado en templates.
 * - No es standalone, depende de módulos externos para su funcionamiento.
 * - Al iniciarse, redirige automáticamente a la ruta '/public/home'.
 */
@Component({
  selector: 'app-public',
  standalone: false,
  templateUrl: './public.component.html'  // Archivo HTML asociado con la vista de este componente
})
export class PublicComponent implements OnInit {

  /**
   * Constructor que inyecta el servicio Router para controlar la navegación.
   *
   * @param router Servicio Router de Angular para realizar navegación programática.
   */
  constructor(private router: Router) { }

  /**
   * Método del ciclo de vida OnInit.
   * Se ejecuta una vez que el componente ha sido inicializado.
   *
   * Acción:
   * - Redirige inmediatamente a la ruta '/public/home' al cargar este componente.
   */
  ngOnInit(): void {
    this.router.navigate(['/public/home']);
  }
}
