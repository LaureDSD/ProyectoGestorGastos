import { Component, OnInit } from '@angular/core';
import { ApiserviceService } from '../../../services/apiservice.service';

@Component({
  selector: 'app-index',
  standalone: false,
  templateUrl: './index.component.html',
  styleUrl: './index.component.css'
})
export class IndexComponent implements OnInit{
  usuarios : number = 2;

  list = [
    "/capturas/c0.png",
    "/capturas/c1.png",
    "/capturas/c2.png",
    "/capturas/c3.png",

    "/capturas/c4.png",
    "/capturas/c5.png",
    "/capturas/c6.png",
    "/capturas/c7.png",

    "/capturas/c8.png",
    "/capturas/c9.png",
    "/capturas/c10.png",
    "/capturas/c11.png",
  ]

  list2 = [
    "/capturas/e0.png",
    "/capturas/e1.png",
    "/capturas/e2.png",
    "/capturas/e3.png",

    "/capturas/e4.png",
    "/capturas/e5.png",
    "/capturas/e6.png",
    "/capturas/e7.png",
  ]

  cardList: { title: string; text: string }[] = [
    { title: "Accesible", text: "Acceso a tu infomración desde cualqier lugar y en cualquier momento." },
    { title: "OCR Inteligente", text: "Escanea tickets y facturas automáticamente gracias a la inteligencia artificial." },
    { title: "Control Total", text: "Administra tus categorías, ingresos y gastos con filtros y estadísticas avanzadas." }
  ]

  constructor(private apiservice : ApiserviceService){}
  ngOnInit(): void {
    this.apiservice.getTotalUsers().subscribe({
      next: (total: any) => {
        this.usuarios = Number(total);
        console.log('Usuarios:', this.usuarios);
      },
      error: (err) => {
        console.error('Error al obtener usuarios', err);
      }
    });
  }


}
