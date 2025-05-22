import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-navbar',
  standalone: false,
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  @Input() server : any = `${environment.apiUrl}/`;
  @Input() titulo : string = "WEB-GESTHOR"
  @Input() default : string = "/public/home"
  @Input() icono : string = "/icon.png"
  @Input() enlaces : string[][] = [["Inicio","/protected/home"],["Filtros","/protected/gastos"],["Herramientas","/protected/tools"]]
  esAdminUsuario: boolean = false;
  profile: any ;

  constructor(private authService: AuthService, private router: Router) {}

  hayToken(): boolean {
    return this.authService.isAuthenticated();
  }

  getProfile(){
    this.authService.getLoadUser().subscribe(u => {
      this.profile = u.imageUrl;
      this.esAdminUsuario = u.role === "ADMIN";
    });

  }

  esAdmin(){
    return this.esAdminUsuario;
  }

  borrarToken(): void {
    this.authService.logout();
    this.router.navigate(['/public/home']);
  }

  ngOnInit() {
    if (this.hayToken()) {
      this.getProfile()
    }
  }

}
