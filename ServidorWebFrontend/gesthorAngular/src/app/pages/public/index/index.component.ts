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
