import { Component, Input } from '@angular/core';

/**
 * Interfaz que representa un ítem de FAQ (pregunta frecuente).
 * - pregunta: texto de la pregunta.
 * - respuesta: texto de la respuesta.
 */
interface FaqItem {
  pregunta: string;
  respuesta: string;
}

/**
 * Componente AcordionComponent
 *
 * Muestra una lista de preguntas frecuentes en un acordeón con estilo Bootstrap.
 *
 * Funcionalidad:
 * - Permite mostrar múltiples preguntas con respuestas plegables.
 * - Solo una respuesta está abierta a la vez (funcionalidad de acordeón).
 * - Título configurable mediante la propiedad `titulo`.
 * - Lista de preguntas y respuestas configurable mediante la propiedad `faqs`.
 *
 * Uso:
 * <app-acordion [titulo]="'Soporte'" [faqs]="listaFaqs"></app-acordion>
 *
 * Propiedades:
 * - titulo: título principal del acordeón, por defecto "Centro de Soporte".
 * - faqs: arreglo de objetos con preguntas y respuestas que se muestran.
 */
@Component({
  selector: 'app-acordion',  // Selector para uso en templates
  standalone: false,          // No es standalone (depende de módulo)
  template: `
    <div class="container">
      <div class="mb-5" style="max-width: 800px; margin: auto;">
        <h4 class="text-center mb-4">{{titulo || 'Centro de Soporte'}}</h4>

        <div class="accordion" id="accordionSoporte">
          <!-- Recorre el arreglo de preguntas frecuentes y genera un ítem acordeón por cada uno -->
          <div class="accordion-item bg-dark text-white border-secondary" *ngFor="let item of faqs; let i = index">
            <h2 class="accordion-header">
              <!-- Botón que despliega o pliega la respuesta -->
              <button class="accordion-button bg-dark text-white" type="button" data-bs-toggle="collapse"
                      [attr.data-bs-target]="'#faq' + i" [class.collapsed]="i !== 0">
                {{item.pregunta}}
              </button>
            </h2>
            <!-- Contenido desplegable de la respuesta -->
            <div [id]="'faq' + i" class="accordion-collapse collapse" [class.show]="i === 0"
                 data-bs-parent="#accordionSoporte">
              <div class="accordion-body">
                {{item.respuesta}}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class AcordionComponent {

  /**
   * Título que aparece arriba del acordeón.
   * Por defecto es 'Centro de Soporte'.
   */
  @Input() titulo: string = 'Centro de Soporte';

  /**
   * Arreglo con preguntas y respuestas que se mostrarán en el acordeón.
   * Contiene objetos que cumplen con la interfaz FaqItem.
   * Se provee un conjunto de preguntas frecuentes por defecto.
   */
  @Input() faqs: FaqItem[] = [
    {
      pregunta: '¿Cómo puedo registrar mis gastos?',
      respuesta: 'Puedes registrar tus gastos escaneando tickets con la cámara, cargando imágenes o introduciéndolos manualmente desde la sección "Nuevo gasto".'
    },
    {
      pregunta: '¿Cómo exporto mis gastos en CSV o PDF?',
      respuesta: 'En el menú de gastos encontrarás la opción "Descargar fromato", donde puedes elegir entre los formatos CSV y PDF para guardar tus registros localmente o enviarlos.'
    },
    {
      pregunta: '¿Qué hago si los datos del ticket se reconocen mal?',
      respuesta: 'Puedes editar cualquier gasto manualmente tras el escaneo. Además, te recomendamos revisar la confianza de los datos y activar la revisión automática desde ajustes.'
    }
  ];
}
