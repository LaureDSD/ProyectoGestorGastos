import { SpentService } from '../../../services/spent.service';
import { NavbarComponent } from './../../../components/navbar/navbar.component';
import { ApiserviceService } from './../../../services/apiservice.service';

import { Component, OnInit } from '@angular/core';
import { UserserviceService } from '../../../services/userservice.service';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { LoginAttemptDto, UserDto } from '../../../models/api-models/api-models.component';

/**
 * Componente DashboardComponent
 *
 * Este componente representa el panel principal o "dashboard" de la aplicación,
 * mostrando información relevante del usuario actual, sus logs de acceso y el total de gastos registrados.
 *
 * Funcionalidades principales:
 * - Carga y muestra los datos completos del usuario actual.
 * - Muestra el historial de intentos de login (logs).
 * - Calcula y muestra el total de gastos del usuario.
 * - Permite actualizar campos del usuario, incluida la imagen de perfil.
 * - Permite cambiar la contraseña del usuario.
 * - Gestiona el cierre de sesión y redirección a la página de login.
 * - Soporta navegación programática dentro de la app.
 *
 * Servicios inyectados:
 * - UserserviceService: para obtener y actualizar información del usuario, logs y gastos.
 * - AuthService: para gestionar autenticación y cierre de sesión.
 * - Router: para navegación interna.
 */
@Component({
  selector: 'app-dashboard',        // Selector HTML para usar el componente
  standalone: false,                // No es standalone, depende de módulo Angular
  templateUrl: './dashboard.component.html', // Archivo HTML asociado
  styleUrl: './dashboard.component.css'      // Archivo CSS asociado (nota: debe ser 'styleUrls')
})
export class DashboardComponent {

  /** Total acumulado de gastos del usuario */
  totalspents: number = 0;

  /** Listado de logs o intentos de login del usuario */
  logs: LoginAttemptDto[] = [];

  /** Datos completos del usuario actual */
  user: UserDto = {
    id: 0,
    name: '',
    username: '',
    email: '',
    phone: '',
    address: '',
    imageUrl: '',
    server: '',
    role: '',
    active: false,
    fv2: false,
    createdAt: '',
    updatedAt: ''
  };

  /** Array de preguntas frecuentes o similares con estilo */
  preguntas: { pregunta: string; respuesta: string; class: string }[] = [];

  /**
   * Constructor
   * Inyecta servicios para obtener datos del usuario, autenticación y navegación.
   * Además, inicia la carga de usuario, logs y gastos.
   *
   * @param userService Servicio para manejo de usuarios
   * @param authservice Servicio para autenticación
   * @param route Servicio de navegación de Angular Router
   */
  constructor(
    private userService: UserserviceService,
    private authservice: AuthService,
    private route: Router
  ) {
    this.cargarUsuario();
    this.cargarLogs();
    this.cargarGastos();
  }

  /**
   * Método para cargar los datos completos del usuario actual.
   * Se suscribe al observable para asignar la respuesta al objeto user.
   */
  cargarUsuario() {
    this.userService.getFullCurrentUser().subscribe(u => this.user = u);
  }

  /**
   * Actualiza la imagen del usuario cuando se recibe un evento con el campo 'imageUrl'.
   *
   * @param event Objeto con propiedad 'field' y 'value'
   */
  updateImg(event: { field: string; value: string }) {
    if (event.field === 'imageUrl') {
      this.user.imageUrl = event.value;
    }
  }

  /**
   * Guarda un campo modificado del usuario y actualiza en backend.
   * Además actualiza el campo server con el nombre de la API desde environment.
   *
   * En caso de éxito muestra alerta de confirmación.
   * En caso de error muestra alerta con el error y recarga datos para revertir cambios.
   *
   * @param event Objeto con propiedades 'field' y 'value' indicando el campo a actualizar
   */
  guardarCampo(event: { field: string; value: string }) {
    if (true) { // Condición siempre verdadera, posible placeholder para futuras validaciones
      (this.user as any)[event.field] = event.value;
      this.user.server = `${environment.apiName}`;
      this.userService.actualizarUsuario(this.user).subscribe({
        next: (res) => {
          alert('Usuario actualizado correctamente');
        },
        error: (err) => {
          alert('Error al actualizar el usuario: ' + err);
          this.cargarUsuario();
          this.cargarLogs();
          this.cargarGastos();
        }
      });
    }
  }

  /**
   * Cambia la contraseña del usuario.
   * Se recibe un objeto con la contraseña actual y la nueva.
   * Muestra alerta al completar la actualización exitosamente.
   *
   * @param data Objeto con 'current' (contraseña actual) y 'newPassword' (nueva contraseña)
   */
  cambiarPassword(data: { current: string; newPassword: string }) {
    this.userService.cambiarPassword(data.current, data.newPassword).subscribe({
      next: () => alert('Contraseña actualizada con éxito.')
    });
  }

  /**
   * Carga el historial de logs o intentos de login del usuario.
   * Asigna la respuesta al arreglo 'logs'.
   */
  cargarLogs() {
    this.userService.getLogs().subscribe(l => this.logs = l);
  }

  /**
   * Carga el total de gastos registrados por el usuario.
   * Asigna el valor numérico a la variable 'totalspents'.
   */
  cargarGastos() {
    this.userService.getTotalSpents().subscribe(ts => this.totalspents = ts);
  }

  /**
   * Cierra la sesión del usuario eliminando el token de autenticación.
   * Redirige al usuario a la página de login.
   */
  borrarToken() {
    this.authservice.logout();
    this.route.navigate(['/login']);
  }

  /**
   * Método para redirigir programáticamente a una ruta interna.
   *
   * @param ruta Ruta a la que se quiere navegar
   */
  redirectTo(ruta: string) {
    this.route.navigate([ruta]);
  }
}
