import { Component } from '@angular/core';

@Component({
  selector: 'app-spinning2',
  standalone: false,
  template: `
      <div class="spinner-border text-primary" role="status" style="width: 4rem; height: 4rem;">
        <span class="visually-hidden">Cargando...</span>
      </div>
  `,
  styles: ``
})
export class Spinning2Component {

}
