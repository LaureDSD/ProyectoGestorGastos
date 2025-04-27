import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiserviceService } from '../../services/apiservice.service';

@Component({
  selector: 'app-detail',
  standalone: false,
  templateUrl: './detail.component.html',
  styleUrl: './detail.component.css'
})
export class DetailComponent {
  elemento: any = {};
  objectKeys = Object.keys;
  seccion: string = '';
  id: string = '';
  errorMessage: string = '';
  listaItems : any[] = []

  constructor(
    private servicio: ApiserviceService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.seccion = params.get('seccion')!;
      this.id = params.get('id')!;
      /*this.cargarDetalles(this.seccion, this.id);*/
    });
  }

  /*
  cargarDetalles(seccion: string, id: string): void {
    this.servicio.obtenerSeccion(`${seccion}/${id}`).subscribe({
      next: (response) => {
        console.log(seccion,id)
        this.elemento = response;
         this.listaItems = this.elemento.drops ?? this.elemento.recompensas ?? []
      },
      error: (err) => {
        this.errorMessage = 'Error de carga';
        console.error('Error en detalles:', err);
      }
    });
  }*/
}
