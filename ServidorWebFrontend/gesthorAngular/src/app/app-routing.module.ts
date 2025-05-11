import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

//Auth components
import { LoginComponent } from './security/login/login.component';
import { RegisterComponent } from './security/register/register.component';
import { OAuth2RedirectComponent } from './security/oauth2-redirect/oauth2-redirect.component';
import { ForgotPasswordComponent } from './security/forgot-password/forgot-password.component';
import { AuthGuard } from './services/auth-guard.service';

//Pages components
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
import {  FormHerramientaComponent } from './pages/protected/form-herramienta/form-herramienta.component';
import { HerramientaComponent } from './pages/protected/herramienta/herramienta.component';
import { FiltroHerramientaComponent } from './pages/protected/filtro-herramienta/filtro-herramienta.component';




const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  //Rutas seguridad
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'oauth2/redirect', component: OAuth2RedirectComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },

  //Rutas publicas
  { path: 'public', component: PublicComponent,
    children : [
      { path: 'home' , component: IndexComponent},
      { path: 'contact', component: ContactComponent },
      { path: 'privacity', component: PrivacityComponent },
    ]
  },

  //Rutas protegidas
  { path: 'protected', component: ProtectedComponent ,
    children : [
      { path: 'home', component: HomeComponent },
      { path: 'tools', component: ToolsComponent },
      { path: 'gastos', component: GastosComponent },
      { path: 'tool/:tipo', component: HerramientaComponent },
      { path: 'filter/:tipo', component: FiltroHerramientaComponent },
      { path: 'form/:tipo/:id', component: FormHerramientaComponent },
  ],
      canActivate: [AuthGuard]},

  //Rutas privadas
  { path: 'private', component: PrivateComponent,
    children : [
      { path: 'admindashboard', component: AdminDashboardComponent },
      { path: 'dashboard', component: DashboardComponent },
    ],
      canActivate: [AuthGuard]},

  { path: '**', redirectTo: '/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
