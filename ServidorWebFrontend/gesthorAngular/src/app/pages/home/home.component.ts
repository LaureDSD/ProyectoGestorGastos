import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
 token: string | null = null;

  userl: any;

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.token = this.authService.getToken();

    this.authService.getCurrentUser().subscribe({
      next: (user) => {
        this.userl = user
      },
      error: (error) => {
        console.error('Error obteniendo el usuario:', error);
      }
    });
  }

  secciones : any = [
    ["mapas","mapa",true]
    ,["monstruos","monstruo",true]
    ,["items","item",true]
    ,["misiones","mision",true]
    ,["grupos","grupos",true]
    ,["habilidades","habilidad",true]
    ,["npcs","npc",true]
    ,["efectos","efectos",true],]

    pulsarBoton(buttonName: string) {
      if (buttonName === 'todo') {
        this.secciones.forEach((seccion: any) => {
          seccion[2] = true;
        });
      } else {
        this.secciones.forEach((seccion: any) => {
          seccion[2] = false;
        });
        const selectedSection = this.secciones.find((seccion: any) => seccion[0] === buttonName);
        if (selectedSection) {
          selectedSection[2] = true;
        }
      }
    }
}
