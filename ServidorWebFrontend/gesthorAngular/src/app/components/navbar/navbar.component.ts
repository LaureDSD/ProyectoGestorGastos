/**
 * Componente Angular NavbarComponent
 *
 * Componente que representa una barra de navegación (navbar) superior fija y responsiva.
 * Utiliza Bootstrap para el estilo y maneja navegación interna con Angular Router.
 * Presenta enlaces dinámicos y controles de acceso según el estado de autenticación y rol del usuario.
 *
 * Funcionalidades principales:
 * - Muestra un logo/icono fijo en la navbar.
 * - Muestra un título con enlace a una ruta por defecto configurable.
 * - Presenta enlaces dinámicos en el menú, definidos por el arreglo `enlaces`.
 * - Muestra opciones de login, logout, y enlaces especiales (como Admin o MiCuenta) dependiendo del estado de autenticación.
 * - Carga y muestra la imagen de perfil del usuario autenticado.
 * - Controla acceso a opciones en función del rol (ejemplo: muestra enlace Admin solo para usuarios con rol ADMIN).
 * - Al cerrar sesión, elimina el token y redirige a la página pública de inicio.
 *
 * Propiedades de entrada (@Input):
 * - server: URL base del servidor para obtener imágenes y recursos (por defecto usa environment.apiUrl).
 * - titulo: Texto del título mostrado en la navbar.
 * - default: Ruta por defecto para el enlace del título.
 * - icono: URL del icono/logo (no usado directamente en la plantilla, el src está hardcodeado).
 * - enlaces: Lista de enlaces a mostrar en el menú principal, en formato [texto, ruta].
 *
 * Variables internas:
 * - esAdminUsuario: bandera booleana que indica si el usuario autenticado es administrador.
 * - profile: ruta o nombre de la imagen de perfil del usuario.
 *
 * Métodos:
 * - hayToken(): boolean - verifica si existe token de autenticación mediante el servicio AuthService.
 * - getProfile(): void - solicita al servicio AuthService los datos del usuario actual y actualiza profile y esAdminUsuario.
 * - esAdmin(): boolean - retorna true si el usuario autenticado es administrador.
 * - borrarToken(): void - realiza logout eliminando token y redirigiendo a la página pública.
 *
 * Ciclo de vida:
 * - ngOnInit(): si hay token, carga el perfil del usuario.
 */

import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-navbar',
  standalone: false,
  template: `
    <nav class="navbar navbar-expand-lg bg-body-tertiary fixed-top" data-bs-theme="dark">

      <!-- Icono/logo fijo en la barra -->
      <img class="collapse navbar-collapse" src="/icon/icon.png" style="width: 40px; margin-left: 1%">

      <div class="container-fluid">
        <!-- Título con enlace por defecto -->
        <a class="navbar-brand" href="#" [routerLink]="[default]">{{ titulo }}</a>

        <!-- Botón toggler para navbar responsive -->
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarSupportedContent"
          aria-controls="navbarSupportedContent"
          aria-expanded="false"
          aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Menú principal visible solo si hay token (usuario autenticado) -->
        <div *ngIf="hayToken()" class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <!-- Renderizado dinámico de enlaces -->
            <li *ngFor="let e of enlaces" class="nav-item">
              <a
                class="nav-link"
                aria-current="page"
                href="#"
                [routerLinkActive]="'active'"
                [routerLink]="[e[1]]">
                {{ e[0] | titlecase }}
              </a>
            </li>
          </ul>
        </div>
      </div>

      <div style="margin-left: 10px;">
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0" style="padding-right: 10px;">
            <!-- Enlace para logearse si NO hay token -->
            <li *ngIf="!hayToken()">
              <a class="nav-link" aria-current="page" href="#" [routerLink]="'/login'">Logearse</a>
            </li>

            <!-- Enlace administrador visible solo si el usuario es admin -->
            <li *ngIf="esAdmin()">
              <a class="nav-link" aria-current="page" href="#" [routerLink]="'/private/admindashboard'">Admin</a>
            </li>

            <!-- Enlace para desconectarse visible si hay token -->
            <li *ngIf="hayToken()">
              <a class="nav-link" aria-current="page" (click)="borrarToken()">Desconectarse</a>
            </li>

            <!-- Enlace a cuenta personal visible si hay token -->
            <li *ngIf="hayToken()">
              <a class="nav-link" aria-current="page" href="#" [routerLink]="'/private/dashboard'">MiCuenta</a>
            </li>

            <!-- Imagen de perfil visible si hay token y profile cargado -->
            <li *ngIf="hayToken() && profile != null">
              <img
                [src]="profile ? (server + 'uploads/' + profile) : '/icon/icons8-cuenta-de-prueba-100.png'"
                alt="Preview"
                class="ms-2 mt-1"
                style="border-radius: 50%; height:35px; width:35px"
                [routerLink]="'/private/dashboard'" />
            </li>
          </ul>
        </div>
      </div>

    </nav>
  `
})
export class NavbarComponent {

  /** URL base para cargar imágenes y recursos del servidor */
  @Input() server: any = `${environment.apiUrl}/`;

  /** Texto del título en la navbar */
  @Input() titulo: string = "WEB-GESTHOR";

  /** Ruta por defecto para el enlace del título */
  @Input() default: string = "/public/home";

  /** URL del icono/logo (no utilizado directamente en el template) */
  @Input() icono: string = "/icon.png";

  /** Arreglo de enlaces para el menú principal; cada enlace es un arreglo [texto, ruta] */
  @Input() enlaces: string[][] = [
    ["Inicio", "/protected/home"],
    ["Filtros", "/protected/gastos"],
    ["Herramientas", "/protected/tools"]
  ];

  /** Indica si el usuario autenticado es administrador */
  esAdminUsuario: boolean = false;

  /** Nombre o ruta de la imagen de perfil del usuario */
  profile: any;

  /**
   * Constructor que inyecta el servicio de autenticación y el router para navegación.
   * @param authService Servicio para manejar autenticación y usuario
   * @param router Servicio Angular Router para navegación
   */
  constructor(private authService: AuthService, private router: Router) {}

  /**
   * Verifica si hay un token válido de autenticación.
   * @returns true si el usuario está autenticado; false en caso contrario.
   */
  hayToken(): boolean {
    return this.authService.isAuthenticated();
  }

  /**
   * Solicita los datos del usuario autenticado y actualiza las propiedades internas
   * como la imagen de perfil y la bandera que indica si es administrador.
   */
  getProfile() {
    this.authService.getLoadUser().subscribe(u => {
      this.profile = u.imageUrl;
      this.esAdminUsuario = u.role === "ADMIN";
    });
  }

  /**
   * Retorna true si el usuario autenticado es administrador.
   * @returns boolean
   */
  esAdmin(): boolean {
    return this.esAdminUsuario;
  }

  /**
   * Elimina el token de autenticación (logout) y redirige a la página pública de inicio.
   */
  borrarToken(): void {
    this.authService.logout();
    this.router.navigate(['/public/home']);
  }

  /**
   * Ciclo de vida del componente: al inicializar, si hay token, carga el perfil del usuario.
   */
  ngOnInit() {
    if (this.hayToken()) {
      this.getProfile();
    }
  }
}
