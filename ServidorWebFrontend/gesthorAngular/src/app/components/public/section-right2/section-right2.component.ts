import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-section-right2',
  standalone: false,
  templateUrl: './section-right2.component.html',
  styleUrl: './section-right2.component.css'
})
export class SectionRight2Component {
  @Input() title = ""
  @Input() text = ""
  @Input() img = ""
}
