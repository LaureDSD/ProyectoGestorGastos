/**
 * Módulo principal de la aplicación Angular.
 *
 * Este módulo centraliza la configuración principal de la app,
 * declarando todos los componentes, pipes y módulos necesarios,
 * además de proveer servicios globales y configuraciones de interceptores HTTP.
 *
 * NOTA: Se eliminó un import duplicado para mantener la limpieza.
 */

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module'; // Módulo de rutas de la aplicación

// Componentes principales de la aplicación
import { AppComponent } from './app.component';
import { LoginComponent } from './security/login/login.component';
import { RegisterComponent } from './security/register/register.component';
import { DashboardComponent } from './pages/private/dashboard/dashboard.component';
import { OAuth2RedirectComponent } from './security/oauth2-redirect/oauth2-redirect.component';
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
import { AdminDashboardComponent, SecondsToHmsPipe } from './pages/private/admin-dashboard/admin-dashboard.component';
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
import { FormHerramientaComponent } from './pages/protected/form-herramienta/form-herramienta.component';
import { NgChartsModule } from 'ng2-charts'; // Módulo para gráficos (charts)
import { HerramientaComponent } from './pages/protected/herramienta/herramienta.component';
import { FiltroHerramientaComponent } from './pages/protected/filtro-herramienta/filtro-herramienta.component';
import { ToolGroupComponent } from './components/tool-group/tool-group.component';
import { ApiModelsComponent } from './models/api-models/api-models.component';
import { ModelsComponent } from './models/models/models.component';
import { FormProductosComponent } from './components/form-productos/form-productos.component';
import { GastoDetalleComponent } from './components/gasto-detalle/gasto-detalle.component';
import { Spinning2Component } from './components/spinning2/spinning2.component';

// Interceptor para añadir tokens de autenticación en las peticiones HTTP
import { TokenInterceptor } from './interceptors/token.interceptor';

@NgModule({
  /**
   * Declaraciones:
   * Lista todos los componentes, directivas y pipes que pertenecen a este módulo.
   * Estos elementos podrán ser usados en las plantillas de los componentes declarados.
   */
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
    FormHerramientaComponent,
    ToolGroupComponent,
    ApiModelsComponent,
    ModelsComponent,
    FormProductosComponent,
    GastoDetalleComponent,
    Spinning2Component
  ],

  /**
   * Imports:
   * Importa módulos externos necesarios para que los componentes de este módulo funcionen correctamente.
   */
  imports: [
    BrowserModule,       // Módulo necesario para ejecutar la app en navegador
    HttpClientModule,    // Para hacer peticiones HTTP
    ReactiveFormsModule, // Para formularios reactivos (FormBuilder, validaciones, etc)
    AppRoutingModule,    // Módulo de rutas definido por el usuario
    FormsModule,         // Formularios template-driven
    NgChartsModule,      // Módulo para gráficos
    NgChartsModule,      // Módulo para gráficos
    SecondsToHmsPipe,    // Importar el pipe standalone
  ],
  /**
   * Providers:
   * Servicios e interceptores inyectables globalmente en la app.
   * Aquí se registra el interceptor que añade el token de autenticación a las peticiones HTTP.
   */
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor, // Clase interceptor para manejar tokens JWT
      multi: true                 // Permite múltiples interceptores
    }
  ],

  /**
   * Bootstrap:
   * Componente raíz que Angular arranca al iniciar la aplicación.
   */
  bootstrap: [AppComponent]
})
export class AppModule { }
