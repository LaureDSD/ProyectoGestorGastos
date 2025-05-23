/**
 * Componente Angular SeparatorComponent
 *
 * Componente visual que renderiza un divisor (línea separadora) con un gradiente horizontal.
 * Permite personalizar el color, altura, margen y opacidad mediante inputs.
 *
 * Inputs:
 * - color: color central del gradiente (por defecto '#dcdcdc')
 * - height: altura de la línea (por defecto '2px')
 * - margin: margen vertical u horizontal alrededor del divisor (por defecto '4rem 0')
 * - opacity: opacidad del divisor (por defecto 1, completamente opaco)
 *
 * Uso:
 * <app-separator [color]="'#ff0000'" [height]="'3px'" [margin]="'2rem 0'" [opacity]="0.8"></app-separator>
 */

import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-separator',    // Selector para usar el componente en templates
  standalone: false,             // No es standalone, debe declararse en un módulo
  template: `
    <div class="gradient-divider"
      [style.background]="'linear-gradient(to right, transparent, ' + color + ', transparent)'"
      [style.height]="height"
      [style.margin]="margin"
      [style.opacity]="opacity">
    </div>
  `                             // Div con estilo inline que genera gradiente horizontal
})
export class SeparatorComponent {
  @Input() color: string = '#dcdcdc';  // Color central del gradiente
  @Input() height: string = '2px';     // Altura del divisor
  @Input() margin: string = '4rem 0';  // Margen alrededor del divisor
  @Input() opacity: number = 1;         // Opacidad del divisor (0-1)
}
