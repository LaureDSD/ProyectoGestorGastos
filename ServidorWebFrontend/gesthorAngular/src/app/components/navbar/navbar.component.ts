import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: false,
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {



  @Input() titulo : string = "GESTHOR"
  @Input() default : string = "/public"
  @Input() icono : string = "/icon.png"
  @Input() enlaces : string[][] = [["Inicio","/protected/home"],["Herramientas","/protected/tools"],["Contacto","/protected/contact"]]
  @Input() user: any = {role: "ADMIN"}


  constructor(private authService: AuthService, private router: Router) {}


  hayToken(): boolean {
    return this.authService.isAuthenticated();
  }


  borrarToken(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  esAdmin(){
    return this.user.role == "ADMIN"
  }

  buscar: string  = ""


  buscarGeneral(texto : string) {
    this.router.navigate(['/buscar/',texto]);
  }
}
