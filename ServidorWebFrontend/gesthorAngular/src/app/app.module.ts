import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './security/login/login.component';
import { RegisterComponent } from './security/register/register.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { OAuth2RedirectComponent } from './security/oauth2-redirect/oauth2-redirect.component';
import { TokenInterceptor } from './interceptors/token.interceptor';
import { HomeComponent } from './pages/home/home.component';
import { ForgotPasswordComponent } from './security/forgot-password/forgot-password.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { ContactComponent } from './pages/contact/contact.component';
import { TabvarComponent } from './components/tabvar/tabvar.component';
import { FooterComponent } from './components/footer/footer.component';
import { CarrouselComponent } from './components/carrousel/carrousel.component';
import { CardComponent } from './components/card/card.component';
import { NavtabComponent } from './components/navtab/navtab.component';
import { SeccionComponent } from './components/seccion/seccion.component';
import { SpinningComponent } from './components/spinning/spinning.component';
import { DetailComponent } from './pages/detail/detail.component';
import { PublicComponent } from './pages/public/public.component';
import { ToolsComponent } from './pages/tools/tools.component';
import { AdminDashboardComponent } from './pages/admin-dashboard/admin-dashboard.component';
import { SearchFiltersComponent } from './pages/search-filters/search-filters.component';
import { ProtectedComponent } from './pages/protected/protected.component';
import { PrivateComponent } from './pages/private/private.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    OAuth2RedirectComponent,
    DashboardComponent,
    HomeComponent,
    ForgotPasswordComponent,
    NavbarComponent,
    ContactComponent,
    TabvarComponent,
    FooterComponent,
    CarrouselComponent,
    CardComponent,
    NavtabComponent,
    SeccionComponent,
    SpinningComponent,
    DetailComponent,
    PublicComponent,
    ToolsComponent,
    AdminDashboardComponent,
    SearchFiltersComponent,
    ProtectedComponent,
    PrivateComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule,
    AppRoutingModule
],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
