/**
 * Archivo de configuración de variables de entorno para la aplicación Angular
 * en modo producción.
 *
 * Contiene parámetros globales que pueden cambiar según el entorno
 * (desarrollo, producción, testing, etc.).
 */
export const environment = {
  /**
   * Indica si la aplicación está en modo producción.
   * Utilizado para habilitar/deshabilitar funcionalidades o logs específicos.
   */
  production: true,

  /**
   * URL base del API a la que la aplicación realizará las solicitudes HTTP.
   * En este caso, apunta al servidor local en el puerto 8080.
   */
  apiUrl: 'http://localhost:8080',

  /**
   * Nombre identificativo del API o proyecto.
   * Puede ser utilizado para mostrar en la UI o para fines de configuración.
   */
  apiName: "GesThor-01"
};
