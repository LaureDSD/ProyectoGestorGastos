import { Component, Input, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent  {
 token: string | null = null;
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
