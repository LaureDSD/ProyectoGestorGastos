/**
 * Archivo de configuración de variables de entorno para la aplicación Angular
 * en modo desarrollo (no producción).
 *
 * Define parámetros globales específicos para el entorno de desarrollo,
 * permitiendo comportamientos diferentes respecto a producción.
 */
export const environment = {
  /**
   * Indica que la aplicación NO está en modo producción.
   * Usado para activar logs detallados, herramientas de debugging, etc.
   */
  production: false,

  /**
   * URL base del API para desarrollo.
   * Apunta al servidor local en el puerto 8080.
   */
  apiUrl: 'http://localhost:8080',

  /**
   * Nombre identificativo del API o proyecto.
   * Útil para mostrar información del entorno o configuración interna.
   */
  apiName: "GesThor-01"
};
