import { Injectable } from '@angular/core';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserserviceService {
  updateImage(file: any) {
    return of({ name: 'John Doe', imageUrl: 'default.jpg' });
  }
  updateName(name: any) {
    return of({ name: 'John Doe', imageUrl: 'default.jpg' });
  }
  getUserData() {
    return of({ name: 'John Doe', imageUrl: 'default.jpg' });
  }

  constructor() { }
}
