import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-section-card',
  standalone: false,
  template:`
  <div style="background-color: #1a1a1a; border: 1px solid #ffffff; border-radius: 15px; padding: 2rem; width: 280px;">
  <h3>{{title}}</h3>
  <p>{{text}}</p>
</div>

  `
})
export class SectionCardComponent {
  @Input() title = ""
  @Input() text = ""
}
