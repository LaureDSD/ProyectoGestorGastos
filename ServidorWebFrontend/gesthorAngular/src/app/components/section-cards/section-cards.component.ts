import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-section-cards',
  standalone: false,
  templateUrl: './section-cards.component.html',
  styleUrl: './section-cards.component.css'
})
export class SectionCardsComponent {
  @Input() cardList : { title: string; text: string }[] = [];
}
