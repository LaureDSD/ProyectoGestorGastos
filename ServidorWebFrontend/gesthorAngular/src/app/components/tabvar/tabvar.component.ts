/**
 * Componente Angular TabvarComponent
 *
 * Este componente representa una barra de pestañas (tab bar) con funcionalidad
 * para navegar entre rutas internas y para alternar la visibilidad de un chat.
 *
 * Funcionalidades principales:
 * - Navegación programática a rutas definidas en la aplicación Angular.
 * - Control del estado visible/invisible del componente de chat mediante el servicio ChatserviceService.
 */

import { ChatserviceService } from './../../services/chatservice.service';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-tabvar',                   // Selector para usar el componente en HTML
  standalone: false,                        // No es standalone, debe ser declarado en un módulo
  template: `
  <div class="navbar-container" style="z-index: 1000; ">
  <nav class="navbar  fixed-bottom  ">
      <div class="container-fluid d-flex justify-content-center align-items-center" style=" background-color: #1b1b1bd0; box-shadow: 0px 0px 10px 5px Black;  ">

          <div class="nav-column" style="flex: 1; display: flex; justify-content: center; text-align: center;">
              <button class="nav-button btn" (click)="navigateTo('/protected/home')">
                  <img src="icon/icons8-home-48 (1).png" alt="Inicio" class="nav-icon" style="width: 30px; height: 30px;">
                  <span class="nav-text" style="display: block; margin-top: 0px; font-size: 11px; color: white;">Inicio</span>
              </button>
          </div>

          <div class="nav-column" style="flex: 1; display: flex; justify-content: center; text-align: center;">
              <button class="nav-button btn" (click)="navigateTo('/protected/gastos')">
                  <img src="icon/icons8-statistics-48 (1).png" alt="Filtros" class="nav-icon" style="width: 30px; height: 30px;">
                  <span class="nav-text" style="display: block; margin-top: 0px; font-size: 11px; color: white;">Filtros</span>
              </button>
          </div>

          <div class="nav-column" style="flex: 1; display: flex; justify-content: center; text-align: center;">
              <button class="center-button btn" (click)="navigateTo('/protected/tools')"
              style="position: absolute; top: -5px; width: 60px; height: 60px; border-radius: 50%; background-color: #525151c5; z-index: 1001; box-shadow: 0px 0px 15px 5px Black;">
                  <img src="icon/icon.png" alt="Menú" class="nav-icon" style="width: 30px; height: 30px;">
              </button>
          </div>

          <div class="nav-column" style="flex: 1; display: flex; justify-content: center; text-align: center; ">
              <button class="nav-button btn " (click)="toggleChat()">
                  <img src="icon/icons8-ai-50.png" alt="Favoritos" class="nav-icon" style="width: 30px; height: 30px; ">
                  <span class="nav-text" style="display: block; margin-top: 0px; font-size: 11px; color: white; ">AI Chat</span>
              </button>
          </div>

          <div class="nav-column" style="flex: 1; display: flex; justify-content: center; text-align: center;">
              <button class="nav-button btn" (click)="navigateTo('/private/dashboard')">
                  <img src="icon/icons8-male-user-48 (1).png" alt="Perfil" class="nav-icon" style="width: 30px; height: 30px;">
                  <span class="nav-text" style="display: block; margin-top: 0px; font-size: 11px; color: white;">Perfil</span>
              </button>
          </div>

      </div>
  </nav>
</div>

  `
})
export class TabvarComponent {

  /**
   * Constructor del componente
   *
   * @param router Servicio Router para la navegación interna en la app
   * @param chatService Servicio para controlar la visibilidad y estado del chat
   */
  constructor(private router: Router, private chatService: ChatserviceService) {}

  /**
   * Método para navegar programáticamente a una ruta interna
   *
   * @param route Ruta destino como string, debe existir en la configuración del Router
   */
  navigateTo(route: string): void {
    this.router.navigate([route]);
  }

  /**
   * Método para alternar (mostrar/ocultar) el componente de chat usando el servicio ChatserviceService
   */
  toggleChat(): void {
    this.chatService.toggle();
  }
}
