import { Component } from '@angular/core';

@Component({
  selector: 'app-gastos',
  standalone: false,
  templateUrl: './gastos.component.html',
  styleUrl: './gastos.component.css'
})
export class GastosComponent {

  public barChartData = [{ data: [150, 170, 200, 220, 180, 190], label: 'Gastos Mensuales' }];
  public barChartLabels = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun'];

  public lineChartData = [{ data: [50, 80, 60, 100, 120, 130, 90], label: 'Gastos' }];
  public lineChartLabels = ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'];

}
