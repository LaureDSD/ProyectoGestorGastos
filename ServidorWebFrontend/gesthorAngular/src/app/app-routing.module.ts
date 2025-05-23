import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// Importación de componentes relacionados con autenticación y seguridad
import { LoginComponent } from './security/login/login.component';
import { RegisterComponent } from './security/register/register.component';
import { OAuth2RedirectComponent } from './security/oauth2-redirect/oauth2-redirect.component';
import { ForgotPasswordComponent } from './security/forgot-password/forgot-password.component';
import { AuthGuard } from './services/auth-guard.service';  // Guardia para proteger rutas que requieren autenticación

// Importación de componentes de páginas públicas, protegidas y privadas
import { HomeComponent } from './pages/protected/home/home.component';
import { DashboardComponent } from './pages/private/dashboard/dashboard.component';
import { PublicComponent } from './pages/public/public.component';
import { AdminDashboardComponent } from './pages/private/admin-dashboard/admin-dashboard.component';
import { ToolsComponent } from './pages/protected/tools/tools.component';
import { ContactComponent } from './pages/public/contact/contact.component';
import { ProtectedComponent } from './pages/protected/protected.component';
import { PrivateComponent } from './pages/private/private.component';
import { IndexComponent } from './pages/public/index/index.component';
import { PrivacityComponent } from './pages/public/privacity/privacity.component';
import { GastosComponent } from './pages/protected/gastos/gastos.component';
import { FormHerramientaComponent } from './pages/protected/form-herramienta/form-herramienta.component';
import { HerramientaComponent } from './pages/protected/herramienta/herramienta.component';
import { FiltroHerramientaComponent } from './pages/protected/filtro-herramienta/filtro-herramienta.component';

// Definición de las rutas de la aplicación
const routes: Routes = [
  // Ruta raíz: redirige a la página de login por defecto
  { path: '', redirectTo: '/login', pathMatch: 'full' },

  // Rutas de seguridad (autenticación)
  { path: 'login', component: LoginComponent },                        // Página de login
  { path: 'register', component: RegisterComponent },                  // Página de registro de usuario
  { path: 'oauth2/redirect', component: OAuth2RedirectComponent },    // Redirección para OAuth2
  { path: 'forgot-password', component: ForgotPasswordComponent },    // Página para recuperación de contraseña

  // Rutas públicas accesibles sin autenticación
  {
    path: 'public', component: PublicComponent,
    children: [
      { path: 'home', component: IndexComponent },                    // Página principal pública
      { path: 'contact', component: ContactComponent },               // Página de contacto
      { path: 'privacity', component: PrivacityComponent },           // Página de política de privacidad
    ]
  },

  // Rutas protegidas que requieren autenticación (usando AuthGuard)
  {
    path: 'protected', component: ProtectedComponent,
    children: [
      { path: 'home', component: HomeComponent },                     // Página principal de usuario autenticado
      { path: 'tools', component: ToolsComponent },                   // Herramientas disponibles para el usuario
      { path: 'gastos', component: GastosComponent },                 // Gestión de gastos
      { path: 'tool/:tipo', component: HerramientaComponent },        // Visualización de herramienta según tipo (parametrizable)
      { path: 'filter/:tipo', component: FiltroHerramientaComponent },// Filtro de herramientas según tipo
      { path: 'form/:tipo/:id', component: FormHerramientaComponent },// Formulario para herramienta, con tipo e ID parametrizados
    ],
    canActivate: [AuthGuard]  // Protección: solo usuarios autenticados pueden acceder
  },

  // Rutas privadas para usuarios con roles o permisos especiales, también protegidas
  {
    path: 'private', component: PrivateComponent,
    children: [
      { path: 'admindashboard', component: AdminDashboardComponent },  // Panel de administración
      { path: 'dashboard', component: DashboardComponent },            // Dashboard privado de usuario
    ],
    canActivate: [AuthGuard]  // Protección de acceso mediante guardia de autenticación
  },

  // Ruta comodín para manejar rutas no definidas: redirige al login
  { path: '**', redirectTo: '/login' }
];

// Configuración del módulo de rutas
@NgModule({
  imports: [RouterModule.forRoot(routes)],  // Configura el enrutador principal con las rutas definidas
  exports: [RouterModule]                    // Exporta RouterModule para que esté disponible en toda la app
})
export class AppRoutingModule { }
