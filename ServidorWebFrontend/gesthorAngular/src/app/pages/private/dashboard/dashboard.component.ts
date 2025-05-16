import { SpentService } from '../../../services/spent.service';
import { NavbarComponent } from './../../../components/navbar/navbar.component';
import { ApiserviceService } from './../../../services/apiservice.service';

import { Component, OnInit } from '@angular/core';
import { UserserviceService } from '../../../services/userservice.service';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { LoginAttemptDto, UserDto } from '../../../models/api-models/api-models.component';


@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'

})
export class DashboardComponent {

  totalspents : number = 0 ;
  logs : LoginAttemptDto[] = []
  user : UserDto = {
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

  preguntas: { pregunta: string; respuesta: string; class: string }[] = [];

  constructor(
    private userService: UserserviceService,
    private authservice : AuthService,
    private route : Router) {
    this.cargarUsuario()
    this.cargarLogs()
    this.cargarGastos()
  }

  cargarUsuario(){
    this.userService.getFullCurrentUser().subscribe(u => this.user = u);
  }

  updateImg(event: { field: string; value: string }) {
    if (event.field === 'imageUrl') {
      this.user.imageUrl = event.value;
    }
  }

  guardarCampo(event: { field: string; value: string }) {
    if(true){
      (this.user as any)[event.field] = event.value;
      this.user.server = `${environment.apiName}`
      this.userService.actualizarUsuario(this.user).subscribe({
        next: (res) => {
          alert('Usuario actualizado correctamente');
        },
        error: (err) => {
          alert('Error al actualizar el usuario: ' + err);
          this.cargarUsuario()
          this.cargarLogs()
          this.cargarGastos()
        }
      });
    }
  }

  cambiarPassword(data: { current: string; newPassword: string }) {
      this.userService.cambiarPassword(data.current, data.newPassword).subscribe({
        next: () => alert('Contraseña actualizada con éxito.')
      });
  }

  cargarLogs() {
    this.userService.getLogs().subscribe(l => this.logs = l);
  }

  cargarGastos() {
    this.userService.getTotalSpents().subscribe(ts => this.totalspents = ts);
  }

  borrarToken() {
    this.authservice.logout()
    this.route.navigate(["/login"])
  }

  redirectTo() {
    this.route.navigate(["/private/setings"])
  }

}
