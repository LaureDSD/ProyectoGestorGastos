import { Injectable, signal } from '@angular/core';

/**
 * Servicio para la gestión del tema visual de la aplicación.
 * Permite alternar entre temas 'light' y 'dark', persistiendo
 * la elección en el localStorage y aplicando la clase CSS correspondiente al body.
 */
@Injectable({
  providedIn: 'root',
})
export class ThemeService {

  /**
   * Signal que mantiene el estado actual del tema.
   * Puede ser 'light' o 'dark'. Inicialmente 'light' por defecto.
   */
  private themeSignal = signal<'light' | 'dark'>('light');

  /**
   * Getter para exponer el estado del tema como un signal de solo lectura.
   * Esto permite que componentes y servicios se suscriban a los cambios de tema
   * sin poder modificar el valor directamente.
   */
  get theme() {
    return this.themeSignal.asReadonly();
  }

  /**
   * Método para alternar el tema actual.
   * Cambia de 'light' a 'dark' o viceversa,
   * actualizando la señal y el almacenamiento local.
   */
  toggleTheme() {
    const newTheme = this.themeSignal() === 'light' ? 'dark' : 'light';
    this.setTheme(newTheme);
  }

  /**
   * Método para establecer un tema específico.
   * Actualiza la señal, guarda la preferencia en localStorage
   * y cambia las clases CSS del body para reflejar el tema.
   *
   * @param theme Tema a establecer: 'light' o 'dark'.
   */
  setTheme(theme: 'light' | 'dark') {
    this.themeSignal.set(theme);
    localStorage.setItem('theme', theme);

    // Remueve ambas clases para evitar conflictos
    document.body.classList.remove('light', 'dark');
    // Agrega la clase CSS correspondiente al nuevo tema
    document.body.classList.add(theme);
  }

  /**
   * Constructor que carga el tema guardado en localStorage (si existe)
   * y lo aplica automáticamente al iniciar la aplicación.
   */
  constructor() {
    const saved = localStorage.getItem('theme') as 'light' | 'dark' | null;
    if (saved) {
      this.setTheme(saved);
    }
  }
}


/**
 * Ejemplo de uso en un componente:
 *
 * private themeService = inject(ThemeService);
 * theme = computed(() => this.themeService.theme());
 *
 * constructor() {
 *   effect(() => {
 *     const current = this.theme();
 *     console.log('Tema actual:', current);
 *     // Aquí puedes ejecutar lógica adicional,
 *     // como aplicar estilos dinámicos o actualizar componentes
 *   });
 * }
 */
