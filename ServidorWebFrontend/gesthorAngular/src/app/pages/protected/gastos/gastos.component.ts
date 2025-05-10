import { Component } from '@angular/core';
import { ChartConfiguration } from 'chart.js';

@Component({
  selector: 'app-gastos',
  standalone: false,
  templateUrl: './gastos.component.html',
  styleUrl: './gastos.component.css'
})
export class GastosComponent {

    // Monthly bar chart
    monthlyChartData: ChartConfiguration<'bar'>['data'] = {
      labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May',"Jun","Jul","Ago","Sep","Nov","Dic"],
      datasets: [
        { label: 'Gastos (€)', data: [20, 50, 3, 10, 10], backgroundColor: '#0d6efd' }
      ]
    };

    // Dark mode chart options
  darkChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: {
      legend: {
        labels: {
          color: '#ffffff'
        }
      }
    },
    scales: {
      x: {
        ticks: { color: '#ffffff' },
        grid: { color: '#444' }
      },
      y: {
        ticks: { color: '#ffffff' },
        grid: { color: '#444' },
        beginAtZero: true
      }
    }
  };


  //PETICIONDE TODOS LOS GATSO DEL USUARIO DEL ULTIMO ANO (DEFAULT)
  //PEICIONES AL APLICAR FILTROS

}
