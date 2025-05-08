import { Component, Input } from '@angular/core';
import { Route, Router } from '@angular/router';

@Component({
  selector: 'app-call-buttom',
  standalone: false,
  templateUrl: './call-buttom.component.html',
  styleUrl: './call-buttom.component.css'
})
export class CallButtomComponent {
  @Input() title = ""
  @Input() text = ""
  @Input() buttomtext = ""
  @Input() buttomstyle = 1
  @Input() buttonredirect = ""

  constructor(private router : Router){}

  redirectTo(text : string){
    this.router.navigate([text])
  }

}
