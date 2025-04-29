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
import { HeroComponent } from './components/public/hero/hero.component';
import { ImageComponent } from './components/public/image/image.component';
import { SeparatorComponent } from './components/public/separator/separator.component';
import { CallButtomComponent } from './components/public/call-buttom/call-buttom.component';
import { SectionLeftComponent } from './components/public/section-left/section-left.component';
import { SectionRightComponent } from './components/public/section-right/section-right.component';
import { SectionCardsComponent } from './components/public/section-cards/section-cards.component';
import { SectionRight2Component } from './components/public/section-right2/section-right2.component';
import { SectionCardComponent } from './components/public/section-card/section-card.component';
import { IndexComponent } from './pages/index/index.component';
import { BackButtonComponent } from './components/public/back-button/back-button.component';


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
    PrivateComponent,
    HeroComponent,
    ImageComponent,
    SeparatorComponent,
    CallButtomComponent,
    SectionLeftComponent,
    SectionRightComponent,
    SectionCardsComponent,
    SectionRight2Component,
    SectionCardComponent,
    IndexComponent,
    BackButtonComponent
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
