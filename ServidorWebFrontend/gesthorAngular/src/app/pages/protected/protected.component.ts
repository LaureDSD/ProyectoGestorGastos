// Importación de los módulos necesarios desde Angular core y router
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service'; // Servicio de autenticación (aunque no se utiliza en este componente)
import { Router } from '@angular/router';

// Decorador que define el componente y sus metadatos
@Component({
  selector: 'app-protected',              // Selector utilizado en las plantillas HTML
  standalone: false,                      // Indica que este componente no es standalone
  templateUrl: './protected.component.html' // Ruta al archivo de plantilla HTML asociado
})
export class ProtectedComponent implements OnInit {

  // Constructor con inyección de dependencias
  // Inyecta el Router para permitir la navegación programática
  constructor(private router: Router) { }

  /**
   * Hook de ciclo de vida que se ejecuta después de la inicialización del componente.
   * Aquí se podría redirigir al usuario si no está autenticado.
   */
  ngOnInit(): void {
    // Línea comentada que, de activarse, redirigiría al usuario a la ruta de login.
    // this.router.navigate(['/login']);
  }
}
