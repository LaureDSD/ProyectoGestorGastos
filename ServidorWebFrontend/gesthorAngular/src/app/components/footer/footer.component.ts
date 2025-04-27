import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-footer',
  standalone: false,
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.css'
})
export class FooterComponent {
  fecha : number = new Date().getFullYear()
  @Input() comania : string = "laureano.SL"
  @Input() enlaces : string[][] = [["Home","/inicio"],["Info","/info"],["Registrarse","/registro"],["Login","/login"]]

  constructor(){}
  
}
