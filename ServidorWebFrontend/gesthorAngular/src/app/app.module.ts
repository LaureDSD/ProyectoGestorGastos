// Duplicate import removed
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './security/login/login.component';
import { RegisterComponent } from './security/register/register.component';
import { DashboardComponent } from './pages/private/dashboard/dashboard.component';
import { OAuth2RedirectComponent } from './security/oauth2-redirect/oauth2-redirect.component';
import { TokenInterceptor } from './interceptors/token.interceptor';
import { HomeComponent } from './pages/protected/home/home.component';
import { ForgotPasswordComponent } from './security/forgot-password/forgot-password.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { ContactComponent } from './pages/public/contact/contact.component';
import { TabvarComponent } from './components/tabvar/tabvar.component';
import { FooterComponent } from './components/footer/footer.component';
import { CarrouselComponent } from './components/carrousel/carrousel.component';
import { CardComponent } from './components/card/card.component';
import { NavtabComponent } from './components/navtab/navtab.component';
import { SeccionComponent } from './components/seccion/seccion.component';
import { SpinningComponent } from './components/spinning/spinning.component';
import { DetailComponent } from './pages/protected/detail/detail.component';
import { PublicComponent } from './pages/public/public.component';
import { ToolsComponent } from './pages/protected/tools/tools.component';
import { AdminDashboardComponent } from './pages/private/admin-dashboard/admin-dashboard.component';
import { ProtectedComponent } from './pages/protected/protected.component';
import { PrivateComponent } from './pages/private/private.component';
import { HeroComponent } from './components/hero/hero.component';
import { ImageComponent } from './components/image/image.component';
import { SeparatorComponent } from './components/separator/separator.component';
import { CallButtomComponent } from './components/call-buttom/call-buttom.component';
import { SectionLeftComponent } from './components/section-left/section-left.component';
import { SectionRightComponent } from './components/section-right/section-right.component';
import { SectionCardsComponent } from './components/section-cards/section-cards.component';
import { SectionRight2Component } from './components/section-right2/section-right2.component';
import { SectionCardComponent } from './components/section-card/section-card.component';
import { IndexComponent } from './pages/public/index/index.component';
import { BackButtonComponent } from './components/back-button/back-button.component';
import { PrivacityComponent } from './pages/public/privacity/privacity.component';
import { FormComponent } from './components/form/form.component';
import { AcordionComponent } from './components/acordion/acordion.component';
import { AiButtonComponent } from './components/ai-button/ai-button.component';
import { EditFormComponent } from './components/edit-form/edit-form.component';
import { EditFormProfileComponent } from './components/edit-form-profile/edit-form-profile.component';
import { EditFormPasswordComponent } from './components/edit-form-password/edit-form-password.component';
import { GastosComponent } from './pages/protected/gastos/gastos.component';
import { ChatComponent } from './components/chat/chat.component';
import { FormHerramientaComponent } from './components/form-herramienta/form-herramienta.component';
import { NgChartsModule } from 'ng2-charts';
import { HerramientaComponent } from './components/herramienta/herramienta.component';
import { FiltroHerramientaComponent } from './components/filtro-herramienta/filtro-herramienta.component';

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
    BackButtonComponent,
    PrivacityComponent,
    FormComponent,
    AcordionComponent,
    AiButtonComponent,
    EditFormComponent,
    EditFormProfileComponent,
    EditFormPasswordComponent,
    GastosComponent,
    ChatComponent,
    HerramientaComponent,
    FiltroHerramientaComponent,
    FormHerramientaComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule,
    AppRoutingModule,
    FormsModule,
    NgChartsModule
],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
