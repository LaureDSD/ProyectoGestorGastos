import { Component, Input, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { ChartConfiguration, ChartOptions, ChartType, CoreChartOptions, DatasetChartOptions, DoughnutControllerChartOptions, ElementChartOptions, PluginChartOptions, ScaleChartOptions } from 'chart.js';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent  {
 token: string | null = null;
  secciones : any = [
    ["mapas","mapa",true]
    ,["monstruos","monstruo",true]
    ,["items","item",true]]

     // Donut chart: Estimado vs Real
  donutChartData: ChartConfiguration<'doughnut'>['data'] = {
    labels: ['Medio', 'Total'],
    datasets: [{
      data: [400, 520],
      backgroundColor: ['#ffc107', '#dc3545'],
      hoverOffset: 10
    }]
  };

  // Monthly bar chart
  monthlyChartData: ChartConfiguration<'bar'>['data'] = {
    labels: ['Cat1', 'Cat2', 'Cat3', 'Cat4', 'Cat5'],
    datasets: [
      { label: 'Gastos (€)', data: [20, 50, 3, 10, 10], backgroundColor: '#0d6efd' }
    ]
  };

  // Weekly line chart
  weeklyChartData: ChartConfiguration<'line'>['data'] = {
    labels: ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'],
    datasets: [
      {
        label: 'Gasto diario (€)',
        data: [40, 60, 50, 30, 90, 120, 100],
        fill: false,
        borderColor: '#20c997',
        tension: 0.4
      }
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

  // Fake gastos list
  ultimosGastos = [
    { tienda: 'Mercadona', fecha: new Date(), total: 34.75 },
    { tienda: 'Amazon', fecha: new Date(), total: 89.99 },
    { tienda: 'IKEA', fecha: new Date(), total: 125.00 },
    { tienda: 'Gasolinera', fecha: new Date(), total: 50.00 },
    { tienda: 'Zara', fecha: new Date(), total: 42.30 },
    { tienda: 'Lidl', fecha: new Date(), total: 27.40 },
    { tienda: 'Fnac', fecha: new Date(), total: 75.00 },
    { tienda: 'Decathlon', fecha: new Date(), total: 60.50 },
    { tienda: 'Mercadona', fecha: new Date(), total: 34.75 },
    { tienda: 'Amazon', fecha: new Date(), total: 89.99 },
    { tienda: 'IKEA', fecha: new Date(), total: 125.00 },
    { tienda: 'Gasolinera', fecha: new Date(), total: 50.00 },
    { tienda: 'Zara', fecha: new Date(), total: 42.30 },
    { tienda: 'Lidl', fecha: new Date(), total: 27.40 },
    { tienda: 'Fnac', fecha: new Date(), total: 75.00 },
    { tienda: 'Decathlon', fecha: new Date(), total: 60.50 },
    { tienda: 'Mercadona', fecha: new Date(), total: 34.75 },
    { tienda: 'Amazon', fecha: new Date(), total: 89.99 },
    { tienda: 'IKEA', fecha: new Date(), total: 125.00 },
    { tienda: 'Gasolinera', fecha: new Date(), total: 50.00 },
    ];


    //PETICION DE DATOS DE LA ULTIMA SEMANA Y LA MEDIA DE  GASTO

    pulsarBoton(buttonName: string) {
      if (buttonName === 'todo') {
        this.secciones.forEach((seccion: any) => {
          seccion[2] = true;
        });
      } else {
        this.secciones.forEach((seccion: any) => {
          seccion[2] = false;
        });
        const selectedSection = this.secciones.find((seccion: any) => seccion[0] === buttonName);
        if (selectedSection) {
          selectedSection[2] = true;
        }
      }
    }
}
