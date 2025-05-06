import { ApiserviceService } from './../../../services/apiservice.service';

import { Component, OnInit } from '@angular/core';
import { UserserviceService } from '../../../services/userservice.service';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'

})
export class DashboardComponent {

  server : any = {};
  logs : any = {};
  user : any = {};

  preguntas: { pregunta: string; respuesta: string; class: string }[] = [];

  constructor(
    private userService: UserserviceService,
    private authservice : AuthService,
    private apiserviceService : ApiserviceService,
    private route : Router) {
    this.cargarUsuario()
    this.cargarLogs()
  }

  cargarUsuario(){
    this.user = this.userService.getFullCurrentUser().subscribe( u => this.user = u);
  }

  guardarCampo(event: { field: string; value: string }) {
    this.user[event.field] = event.value;
    this.user.server = this.apiserviceService.getApiServer().name
    this.userService.actualizarUsuario(this.user).subscribe({
      next: (res) => {
        alert('Usuario actualizado correctamente');
      },
      error: (err) => {
        alert('Error al actualizar el usuario: ' + err);
        this.cargarUsuario()
        this.cargarLogs()
      }
    });
  }

  cambiarPassword(data: { current: string; newPassword: string }) {
      this.userService.cambiarPassword(data.current, data.newPassword).subscribe({
        next: () => alert('Contraseña actualizada con éxito.')
      });
  }

  cargarLogs() {
    this.userService.getLogs().subscribe(l => this.logs = l.slice(0, 10));
  }

  borrarToken() {
    this.authservice.logout()
    this.route.navigate(["/login"])
  }

  redirectTo() {
    this.route.navigate(["/private/setings"])
  }

}
