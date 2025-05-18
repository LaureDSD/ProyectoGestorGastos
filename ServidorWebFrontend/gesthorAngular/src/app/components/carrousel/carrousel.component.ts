import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-carrousel',
  standalone: false,
  templateUrl: './carrousel.component.html',
  styleUrl: './carrousel.component.css'
})
export class CarrouselComponent {
  @Input() imglist : string[] = []
  @Input() imgSlide : number = 4
  @Input() title = ""
  @Input() text = ""

  get chunkedImages() {
  const chunks = [];
  for (let i = 0; i < this.imglist.length; i += this.imgSlide) {
    chunks.push(this.imglist.slice(i, i + this.imgSlide));
  }
  return chunks;
}

  constructor(){}

}
