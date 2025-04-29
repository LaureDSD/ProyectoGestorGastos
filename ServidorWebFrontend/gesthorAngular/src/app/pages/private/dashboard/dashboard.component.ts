
import { Component, OnInit } from '@angular/core';
import { UserserviceService } from '../../../services/userservice.service';


@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'

})
export class DashboardComponent implements OnInit {
agregarCategoria() {
throw new Error('Method not implemented.');
}
borrarCategoria(_t90: any) {
throw new Error('Method not implemented.');
}
editarCategoria(_t90: any) {
throw new Error('Method not implemented.');
}
editarFotoPerfil() {
throw new Error('Method not implemented.');
}

  server : any = {

  }

  user : any = {
    profile: '/img/_fddad796-df42-4d46-9d79-5558fa9b7a1f.jpg',
    name: 'Juan Pérez',
    username: 'JP_GamerX',
    server: 'API-Gesthot-1',
    status: "true",
    role: 'USER',
    password: 'USER123',
    activity: '12:12:12',
    email: 'juan.perez@example.com',
    phone: '+34 600 123 456',
    address: 'Calle Ejemplo 123, Madrid',
    fv2: 'Activada',
    totalspents: "12",
    id: 'ID-12345-GT',
    payments: [
      { provider: 'Visa', ref: '**** 1234' },
      { provider: 'PayPal', ref: 'juan.perez@paypal.com' }
    ],
    categories: [
      { name: 'Casa', color: '#3343' },
      { name: 'Patio', color: '#7645' }
    ],
    logs: [
      { date: '12:12:12', status: 'true' },
      { date: '12:12:12', status: 'false' }
    ]
  };

  preguntas = [
    {
      pregunta: '¿Cómo cambio mi nombre público?',
      respuesta: 'Ve a tu perfil y haz clic en el botón actualizar junto al nombre público.',
      class: 'accordion-collapse collapse'
    },
    {
      pregunta: '¿Puedo cambiar mi servidor?',
      respuesta: 'No, el servidor no se puede cambiar una vez registrado.',
      class: 'accordion-collapse collapse'
    }


  ];





























logout() {
throw new Error('Method not implemented.');
}
  token: string | null = null;


  constructor(private userService: UserserviceService) {}

  ngOnInit() {
    //this.userService.getUserData().subscribe(data => this.user = data);
  }

  updateName() {
    this.userService.updateName(this.user.name).subscribe(updated => {
      this.user.name = updated.name;
    });
  }

  onImageChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.userService.updateImage(file).subscribe(updated => {
        this.user.imageUrl = updated.imageUrl;
      });
    }
  }

}
