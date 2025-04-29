import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-carrousel',
  standalone: false,
  templateUrl: './carrousel.component.html',
  styleUrl: './carrousel.component.css'
})
export class CarrouselComponent {
  @Input() carr : string[] = [
    "/luca-romano-3ciMh7Ck92s-unsplash.jpg",
    "/pexels-photo-7680742.jpeg",
    "/pexels-photo-7926655.jpeg"]

  constructor(){}


}
