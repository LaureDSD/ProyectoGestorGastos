import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { ChartOptions, CoreChartOptions, DatasetChartOptions, DoughnutControllerChartOptions, ElementChartOptions, PluginChartOptions, ScaleChartOptions } from 'chart.js';

@Component({
  selector: 'app-admin-dashboard',
  standalone: false,
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent {
  token: string | null = null;

  userl: any;

  metrics = [
    { label: 'Gastos', value: '2,2 mil', bg: 'bg-primary' },
    { label: 'Ficheros', value: '1,6 mil', bg: 'bg-info' },
    { label: 'Usuarios', value: '215,0', bg: 'bg-secondary' },
    { label: 'Accesos', value: '4,8', bg: 'bg-warning' }
  ];

  // Almacenamiento
  storageValue = 2;
  donutData = {
    labels: ['Usado', 'Libre'],
    datasets: [{
      data: [2, 98],
      backgroundColor: ['#C2185B', '#ECEFF1']
    }]
  };

  // Accesos
  lineData = {
    labels: ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'],
    datasets: [
      {
        label: 'Correctos',
        data: [10, 25, 40, 30, 20, 15, 30],
        fill: false,
        borderColor: '#42A5F5'
      },
      {
        label: 'Fallidos',
        data: [5, 10, 20, 10, 15, 5, 10],
        fill: false,
        borderColor: '#EC407A'
      }
    ]
  };
  lineOptions: ChartOptions = {
    responsive: true,
    plugins: { legend: { display: true } }
  };

  // Actividad
  barData = {
    labels: ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'],
    datasets: [
      {
        label: 'Eventos A',
        data: [200, 180, 150, 130, 100, 90, 80],
        backgroundColor: '#8E24AA'
      },
      {
        label: 'Eventos B',
        data: [160, 170, 140, 120, 110, 70, 60],
        backgroundColor: '#1E88E5'
      }
    ]
  };
  barOptions: ChartOptions = {
    responsive: true,
    plugins: { legend: { display: true } },
    scales: { x: {}, y: { beginAtZero: true } }
  };
donutOptions: Partial<CoreChartOptions<"doughnut"> & ElementChartOptions<"doughnut"> & PluginChartOptions<"doughnut"> & DatasetChartOptions<"doughnut"> & ScaleChartOptions<"doughnut"> & DoughnutControllerChartOptions> | undefined;

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.token = this.authService.getToken();
    this.authService.getLoadUser();
  }

}
