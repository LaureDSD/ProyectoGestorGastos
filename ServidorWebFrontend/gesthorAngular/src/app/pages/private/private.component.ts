import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-private',
  standalone: false,
  templateUrl: './private.component.html'
})
export class PrivateComponent  implements OnInit {
  constructor(private router: Router) { }
  ngOnInit(): void {
    //this.router.navigate(['/login']);
  }
}
