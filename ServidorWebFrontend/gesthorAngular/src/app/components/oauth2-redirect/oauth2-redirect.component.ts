import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-oauth2-redirect',
  standalone: false,             
  template: `<div class="text-center mt-5"><div class="spinner-border" role="status"></div><p>Redirigiendo…</p></div>`
})
export class OAuth2RedirectComponent implements OnInit {
  constructor(private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token');
    if (token) {
      localStorage.setItem('token', token);
      this.router.navigate(['/home']);
    } else {
      this.router.navigate(['/login']);
    }
  }
}

