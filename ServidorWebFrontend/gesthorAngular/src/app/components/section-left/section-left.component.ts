import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-section-left',
  standalone: false,
  templateUrl: './section-left.component.html',
  styleUrl: './section-left.component.css'
})
export class SectionLeftComponent {
  @Input() title = ""
  @Input() text = ""
  @Input() img = ""
}
