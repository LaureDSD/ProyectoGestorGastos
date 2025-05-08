import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-section-right',
  standalone: false,
  templateUrl: './section-right.component.html',
  styleUrl: './section-right.component.css'
})
export class SectionRightComponent {
  @Input() title = ""
  @Input() text = ""
  @Input() img = ""
}
