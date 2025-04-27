import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

//Auth components
import { LoginComponent } from './security/login/login.component';
import { RegisterComponent } from './security/register/register.component';
import { OAuth2RedirectComponent } from './security/oauth2-redirect/oauth2-redirect.component';
import { ForgotPasswordComponent } from './security/forgot-password/forgot-password.component';
import { AuthGuard } from './services/auth-guard.service';

//Pages components
import { HomeComponent } from './pages/home/home.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { PublicComponent } from './pages/public/public.component';
import { AdminDashboardComponent } from './pages/admin-dashboard/admin-dashboard.component';
import { ToolsComponent } from './pages/tools/tools.component';
import { SearchFiltersComponent } from './pages/search-filters/search-filters.component';
import { ContactComponent } from './pages/contact/contact.component';
import { ProtectedComponent } from './pages/protected/protected.component';
import { PrivateComponent } from './pages/private/private.component';



const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  //Rutas seguridad
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'oauth2/redirect', component: OAuth2RedirectComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  //Rutas publicas
  { path: 'public', component: PublicComponent,
    children : []
  },
  //Rutas protegidas
  { path: 'protected', component: ProtectedComponent ,
    children : [
      { path: 'home', component: HomeComponent },
      { path: 'dashboard', component: DashboardComponent },
      { path: 'tools', component: ToolsComponent },
      { path: 'search', component: SearchFiltersComponent },
      { path: 'contact', component: ContactComponent },
  ],
      canActivate: [AuthGuard]},
  //Rutas privadas
  { path: 'private', component: PrivateComponent,
    children : [
      { path: 'admindashboard', component: AdminDashboardComponent },
    ],
      canActivate: [AuthGuard]},

  { path: '**', redirectTo: '/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
