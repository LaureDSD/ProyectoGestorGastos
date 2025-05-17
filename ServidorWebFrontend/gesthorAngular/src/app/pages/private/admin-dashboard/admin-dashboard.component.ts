import { Component, Pipe, PipeTransform } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { ChartOptions, CoreChartOptions, DatasetChartOptions, DoughnutControllerChartOptions, ElementChartOptions, PluginChartOptions, ScaleChartOptions } from 'chart.js';
import { ServerInfoDto } from '../../../models/api-models/api-models.component';
import { ServerStatsService } from '../../../services/server-stats.service';
import { interval } from 'rxjs';

@Component({
  selector: 'app-admin-dashboard',
  standalone: false,
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent {
  serverInfo!: ServerInfoDto;
  load: boolean = false;

  // Historial dinámico
  cpuHistory: number[] = [];
  ramUsedHistory: number[] = [];
  ramFreeHistory: number[] = [];
  timestamps: string[] = [];
  maxPoints = 10;

  // Métricas principales
  metrics: any[] = [];

  // Almacenamiento
  storageValue = 0;
  donutData: {
    labels: string[],
    datasets: { data: number[], backgroundColor: string[] }[]
  } = {
    labels: ['Usado', 'Libre'],
    datasets: [{ data: [], backgroundColor: ['#ffce33', '#ECEFF1'] }]
  };
  donutOptions = { responsive: true };



  // Gráfico de línea (uso CPU)
  lineData: any = {};
  lineOptions: ChartOptions = {
    responsive: true,
    plugins: { legend: { display: true } }
  };

  // Gráfico de barras (uso RAM)
  barData: any = {};
  barOptions: ChartOptions = {
    responsive: true,
    plugins: { legend: { display: true } },
    scales: { x: {}, y: { beginAtZero: true } }
  };

  constructor(private serverStatsService: ServerStatsService) {}

  ngOnInit() {
    this.load = true;
    this.fetchStats();

    // Refrescar cada 10s
    interval(50).subscribe(() => {
      this.fetchStats();
    });
  }

  fetchStats() {
    this.serverStatsService.getServerInfo().subscribe(info => {
      this.serverInfo = info;
      this.updateMetrics();
      this.updateDonut(info);
      this.updateHistory(info);
      this.load = false;
    });
  }

  updateMetrics() {
    this.metrics = [
      { label: 'Gastos', value: this.serverInfo.spenses ?? 0, bg: 'bg-primary' },
      { label: 'Usuarios', value: this.serverInfo.users, bg: 'bg-secondary' },
      { label: 'SPRING', value: this.serverInfo.activeapi ? 'Activo' : 'Inactivo', bg: 'bg-info' },
      { label: 'OCR', value: this.serverInfo.activeocr ? 'Activo' : 'Inactivo', bg: 'bg-success' }
    ];
  }

updateDonut(info: ServerInfoDto) {
  const usedGB = this.bytesToGB(info.usedDisk);
  const totalGB = this.bytesToGB(info.totalDisk);

  this.donutData = {
    labels: ['Usado', 'Libre'],
    datasets: [{
      data: [usedGB, totalGB - usedGB],
      backgroundColor: ['#C2185B', '#ECEFF1']
    }]
  };

  this.storageValue = usedGB;
}

  

updateHistory(info: ServerInfoDto) {
  const now = new Date();
  const label = now.toLocaleTimeString();

  const usedGB = this.bytesToGB(info.usedMemory);
  const totalGB = this.bytesToGB(info.totalMemory);
  const freeGB = totalGB - usedGB;

  if (this.cpuHistory.length >= this.maxPoints) {
    this.cpuHistory.shift();
    this.ramUsedHistory.shift();
    this.ramFreeHistory.shift();
    this.timestamps.shift();
  }

  this.cpuHistory.push(Number(info.cpuLoad.toFixed(2)));
  this.ramUsedHistory.push(usedGB);
  this.ramFreeHistory.push(freeGB);
  this.timestamps.push(label);

  this.lineData = {
    labels: this.timestamps,
    datasets: [{
      label: 'Uso CPU (%)',
      data: this.cpuHistory,
      fill: false,
      borderColor: '#000000'
    }]
  };

  this.barData = {
    labels: this.timestamps,
    datasets: [
      {
        label: 'RAM usada (GB)',
        data: this.ramUsedHistory,
        backgroundColor: '#8E24AA'
      },
      {
        label: 'RAM libre (GB)',
        data: this.ramFreeHistory,
        backgroundColor: '#4CAF50'
      },
      {
        label: 'RAM total (GB)',
        data: Array(this.timestamps.length).fill(totalGB),
        type: 'line',
        borderColor: '#FF9800',
        borderWidth: 2,
        fill: false,
        pointRadius: 0,
        tension: 0.1
      }
    ]
  };
}
  // Convertir bytes a GB
  bytesToGB(bytes: number): number {
    return Math.round((bytes / (1024 ** 3)) * 100) / 100;
  }
}

// Pipe para convertir segundos a formato h:m:s
@Pipe({name: 'secondsToHms'})
export class SecondsToHmsPipe implements PipeTransform {
  transform(value: number): string {
    if (!value) return '0s';
    const secNum = Number(value);
    const hours = Math.floor(secNum / 3600);
    const minutes = Math.floor((secNum % 3600) / 60);
    const seconds = secNum % 60;

    return `${hours}h ${minutes}m ${seconds}s`;
  }
}