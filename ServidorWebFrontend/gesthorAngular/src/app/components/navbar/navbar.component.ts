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
  @Input() default : string = "/public/home"
  @Input() icono : string = "/icon.png"
  @Input() enlaces : string[][] = [["Inicio","/protected/home"],["Herramientas","/protected/tools"],["Contacto","/public/contact"]]

  constructor(private authService: AuthService, private router: Router) {}


  hayToken(): boolean {
    return this.authService.isAuthenticated();
  }

  esAdmin(){
    return this.authService.isAdmin()
  }

  borrarToken(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }


  esAdminUsuario: boolean = false;

  ngOnInit() {
    if (this.hayToken()) {
      this.esAdminUsuario = this.authService.isAdmin();
    }
  }
  
}
