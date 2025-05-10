import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-separator',
  standalone: false,
  templateUrl: './separator.component.html',
  styleUrl: './separator.component.css'
})
export class SeparatorComponent {
  @Input() color: string = '#dcdcdc';
  @Input() height: string = '2px';
  @Input() margin: string = '4rem 0';
  @Input() opacity: number = 1;
}
