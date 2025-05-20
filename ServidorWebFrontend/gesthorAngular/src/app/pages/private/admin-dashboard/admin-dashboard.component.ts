import { Component, Pipe, PipeTransform } from '@angular/core';
import { ServerInfoDto } from '../../../models/api-models/api-models.component';
import { ServerStatsService } from '../../../services/server-stats.service';
import { interval } from 'rxjs';
import { ChartOptions } from 'chart.js';

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

  // Almacenamiento (solo carga inicial)
  storageValue = 0;
  donutData: any = {
    labels: ['Usado', 'Libre'],
    datasets: [{ data: [], backgroundColor: ['#14effe', '#14effe'] }]
  };
  donutOptions: ChartOptions = { responsive: true };

  // Gráfico CPU (línea dinámica)
  lineData: any = {};
  lineOptions: ChartOptions = {
    responsive: true,
    plugins: { legend: { display: true } }
  };

  // Gráfico RAM (línea dinámica)
  barData: any = {};
  barOptions: ChartOptions = {
    responsive: true,
    plugins: { legend: { display: true } },
    scales: { x: {}, y: { beginAtZero: true } }
  };

  constructor(private serverStatsService: ServerStatsService) {}

  ngOnInit() {
    this.load = true;

    // Carga inicial: info + donut solo una vez
    this.serverStatsService.getServerInfo().subscribe(info => {
      this.serverInfo = info;
      this.updateMetrics();
      this.updateDonut(info);
      this.updateHistory(info);
      this.load = false;
    });

    // Actualizaciones periódicas sin tocar donut
    interval(5000).subscribe(() => {
      this.fetchStats(false);
    });
  }

  fetchStats(updateDonut = true) {
    this.serverStatsService.getServerInfo().subscribe(info => {
      this.serverInfo = info;
      this.updateMetrics();
      if (updateDonut) this.updateDonut(info);
      this.updateHistory(info);
      this.load = false;
    });
  }

  updateMetrics() {
  this.metrics = [
      {
        label: 'Gastos',
        value: this.serverInfo.spenses ?? 0,
        bg: 'bg-primary'
      },
      {
        label: 'Usuarios',
        value: this.serverInfo.users,
        bg: 'bg-secondary'
      },
      {
        label: 'SPRING',
        value: this.serverInfo.activeapi ? 'Activo' : 'Inactivo',
        bg: this.serverInfo.activeapi ? 'bg-success' : 'bg-danger'
      },
      {
        label: 'OCR',
        value: this.serverInfo.activeocr ? 'Activo' : 'Inactivo',
        bg: this.serverInfo.activeocr ? 'bg-success' : 'bg-danger'
      }
    ];
  }

  updateDonut(info: ServerInfoDto) {
    const usedGB = this.bytesToGB(info.usedDisk);
    const totalGB = this.bytesToGB(info.totalDisk);

    this.donutData = {
      labels: ['Usado', 'Libre'],
      datasets: [{
        data: [usedGB, totalGB - usedGB],
        backgroundColor: ['#ffad00', '#f6f6f6']
      }]
    };

    this.storageValue = usedGB;
  }

  updateHistory(info: ServerInfoDto) {
    const now = new Date();
    const label = now.toLocaleTimeString();

    const usedGB = this.bytesToGB(info.usedMemory);
    const totalGB = this.bytesToGB(info.totalMemory);

    if (this.cpuHistory.length >= this.maxPoints) {
      this.cpuHistory.shift();
      this.ramUsedHistory.shift();
      this.ramFreeHistory.shift();
      this.timestamps.shift();
    }

    this.cpuHistory.push(Number(info.cpuLoad.toFixed(2)));
    this.ramUsedHistory.push(usedGB);
    this.ramFreeHistory.push(totalGB - usedGB);
    this.timestamps.push(label);

    // CPU gráfico línea
    this.lineData = {
      labels: this.timestamps,
      datasets: [{
        label: 'Uso CPU (%)',
        data: this.cpuHistory,
        fill: false,
        borderColor: '#1ce7f5',
        tension: 0.4,
        pointRadius: 0
      }]
    };

    // RAM gráfico línea, solo usada y total (sin barra)
    this.barData = {
      labels: this.timestamps,
      datasets: [
        {
          label: 'RAM usada (GB)',
          data: this.ramUsedHistory,
          borderColor: '#8E24AA',
          tension: 0.4,
          pointRadius: 0,
          fill: false
        },
        {
          label: 'RAM total (GB)',
          data: Array(this.timestamps.length).fill(totalGB),
          borderColor: '#FF9800',
          borderWidth: 2,
          tension: 0.1,
          pointRadius: 0,
          fill: false
        }
      ]
    };
  }

  // Convertir bytes a GB
  bytesToGB(bytes: number): number {
    return Math.round((bytes / (1024 ** 3)) * 100) / 100;
  }
}

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
