import { NgChartsModule } from 'ng2-charts';
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
import { SearchFiltersComponent } from './pages/protected/search-filters/search-filters.component';
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
import { IndexComponent } from './pages/public/index/index.component';
import { BackButtonComponent } from './components/public/back-button/back-button.component';
import { PrivacityComponent } from './pages/public/privacity/privacity.component';
import { FormComponent } from './components/public/form/form.component';
import { AcordionComponent } from './components/public/acordion/acordion.component';
import { AiButtonComponent } from './components/ai-button/ai-button.component';
import { EditFormComponent } from './components/edit-form/edit-form.component';
import { EditFormProfileComponent } from './components/edit-form-profile/edit-form-profile.component';
import { EditFormPasswordComponent } from './components/edit-form-password/edit-form-password.component';


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
    BackButtonComponent,
    PrivacityComponent,
    FormComponent,
    AcordionComponent,
    AiButtonComponent,
    EditFormComponent,
    EditFormProfileComponent,
    EditFormPasswordComponent
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
