import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-carrousel',
  standalone: false,
  templateUrl: './carrousel.component.html',
  styleUrl: './carrousel.component.css'
})
export class CarrouselComponent {
  @Input() carr : string[] = [
    "/Optimiza-tu-gestion-financiera-con-SAP-Business-One.jpg",
    "/pexels-photo-7680742.jpeg",
    "/pexels-photo-7926655.jpeg"]

  constructor(){}

}
