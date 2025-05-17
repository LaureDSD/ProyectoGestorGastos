// theme.service.ts
import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {

  //Gesion de tema, ultima modificacion
  private themeSignal = signal<'light' | 'dark'>('light');

  get theme() {
    return this.themeSignal.asReadonly();
  }

  toggleTheme() {
    const newTheme = this.themeSignal() === 'light' ? 'dark' : 'light';
    this.setTheme(newTheme);
  }

  setTheme(theme: 'light' | 'dark') {
    this.themeSignal.set(theme);
    localStorage.setItem('theme', theme);
    document.body.classList.remove('light', 'dark');
    document.body.classList.add(theme);
  }

  constructor() {
    const saved = localStorage.getItem('theme') as 'light' | 'dark' | null;
    if (saved) this.setTheme(saved);
  }
}


/*

private themeService = inject(ThemeService);
  theme = computed(() => this.themeService.theme());

  constructor() {
    effect(() => {
      const current = this.theme();
      console.log('Tema actual:', current);
      //CSS, estilos, etc.
    });

*/ 