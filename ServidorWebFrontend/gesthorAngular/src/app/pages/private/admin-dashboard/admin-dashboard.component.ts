import { Component, Pipe, PipeTransform } from '@angular/core';
import { ServerInfoDto } from '../../../models/api-models/api-models.component';
import { ServerStatsService } from '../../../services/server-stats.service';
import { interval } from 'rxjs';
import { ChartOptions } from 'chart.js';

@Component({
  selector: 'app-admin-dashboard',
  standalone: false,
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']  // corregido a styleUrls (plural y array)
})
export class AdminDashboardComponent {
  serverInfo!: ServerInfoDto;
  load: boolean = false;

  // Histórico dinámico de métricas para gráficos
  cpuHistory: number[] = [];
  ramUsedHistory: number[] = [];
  ramFreeHistory: number[] = [];
  timestamps: string[] = [];
  maxPoints = 10;  // límite de puntos en el historial para gráficos

  // Métricas principales mostradas en cards u otro UI
  metrics: any[] = [];

  // Datos para gráfico de almacenamiento (donut)
  storageValue = 0;
  donutData: any = {
    labels: ['Usado', 'Libre'],
    datasets: [{ data: [], backgroundColor: ['#14effe', '#14effe'] }]
  };
  donutOptions: ChartOptions = { responsive: true };

  // Datos y opciones para gráfico de CPU (línea)
  lineData: any = {};
  lineOptions: ChartOptions = {
    responsive: true,
    plugins: { legend: { display: true } }
  };

  // Datos y opciones para gráfico de RAM (línea)
  barData: any = {};
  barOptions: ChartOptions = {
    responsive: true,
    plugins: { legend: { display: true } },
    scales: { x: {}, y: { beginAtZero: true } }
  };

  constructor(private serverStatsService: ServerStatsService) {}

  ngOnInit() {
    this.load = true;

    // Carga inicial de datos
    this.serverStatsService.getServerInfo().subscribe(info => {
      this.serverInfo = info;
      this.updateMetrics();
      this.updateDonut(info);
      this.updateHistory(info);
      this.load = false;
    });

    // Actualización periódica cada 2.5 segundos (2500 ms)
    interval(2500).subscribe(() => {
      this.fetchStats(false);
    });
  }

  /**
   * Solicita info del servidor y actualiza los gráficos y métricas.
   * @param updateDonut boolean para decidir si actualizar gráfico donut de disco
   */
  fetchStats(updateDonut = true) {
    this.serverStatsService.getServerInfo().subscribe(info => {
      this.serverInfo = info;
      this.updateMetrics();
      if (updateDonut) this.updateDonut(info);
      this.updateHistory(info);
      this.load = false;
    });
  }

  /**
   * Actualiza las métricas principales visibles (Gastos, Usuarios, APIs)
   */
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
        label: 'API PYTHON',
        value: this.serverInfo.activeapi ? 'ACTIVO' : 'SIN SEÑAL',
        bg: this.serverInfo.activeapi ? 'bg-success' : 'bg-danger'
      },
      {
        label: 'OCR',
        value: this.serverInfo.activeocr ? 'ACTIVO' : 'INACTIVO',
        bg: this.serverInfo.activeocr ? 'bg-success' : 'bg-danger'
      }
    ];
  }

  /**
   * Actualiza el gráfico donut con datos de uso y espacio libre en disco
   * @param info Datos actuales del servidor
   */
  updateDonut(info: ServerInfoDto) {
    const usedGB = this.bytesToGB(info.usedDisk);
    const totalGB = this.bytesToGB(info.totalDisk);

    this.donutData = {
      labels: ['Usado', 'Libre'],
      datasets: [{
        data: [usedGB, totalGB - usedGB],
        backgroundColor: ['#10cee0', '#f6f6f6']
      }]
    };

    this.storageValue = usedGB;
  }

  /**
   * Actualiza la historia temporal para los gráficos de CPU y RAM
   * @param info Datos actuales del servidor
   */
  updateHistory(info: ServerInfoDto) {
    const now = new Date();
    const label = now.toLocaleTimeString();

    const usedGB = this.bytesToGB(info.usedMemory);
    const totalGB = this.bytesToGB(info.totalMemory);

    // Mantener el tamaño máximo del historial
    if (this.cpuHistory.length >= this.maxPoints) {
      this.cpuHistory.shift();
      this.ramUsedHistory.shift();
      this.ramFreeHistory.shift();
      this.timestamps.shift();
    }

    // Añadir nuevos datos al historial
    this.cpuHistory.push(Number(info.cpuLoad.toFixed(2)));
    this.ramUsedHistory.push(usedGB);
    this.ramFreeHistory.push(totalGB - usedGB);
    this.timestamps.push(label);

    // Actualizar datos para gráfico de CPU (línea)
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

    // Actualizar datos para gráfico de RAM (línea)
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

  /**
   * Convierte bytes a gigabytes, con 2 decimales
   * @param bytes Cantidad en bytes
   * @returns Número en GB
   */
  bytesToGB(bytes: number): number {
    return Math.round((bytes / (1024 ** 3)) * 100) / 100;
  }
}

/**
 * Pipe personalizado para transformar segundos en formato horas, minutos y segundos (e.g. "1h 12m 35s")
 */
@Pipe({ name: 'secondsToHms' })
export class SecondsToHmsPipe implements PipeTransform {
  transform(value: number): string {
    if (!value) return '0s';

    const secNum = Number(value);
    const hours = Math.floor(secNum / 3600);
    const minutes = Math.floor((secNum % 3600) / 60);
    const seconds = secNum % 60;

    // Construir string condicional para evitar mostrar "0h" o "0m"
    let result = '';
    if (hours > 0) result += `${hours}h `;
    if (minutes > 0 || hours > 0) result += `${minutes}m `;
    result += `${seconds}s`;

    return result.trim();
  }
}
