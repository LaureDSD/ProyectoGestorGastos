package Proyecto.GestorAPI.controllers.securityController;

import java.util.regex.Pattern;

public class MySecurityrController {

    /**
     * Oculta todos los dígitos de un número, excepto los últimos 'mostrar' dígitos.
     *
     * @param numero El número que se quiere ocultar.
     * @param mostrar El número de dígitos que se deben mostrar al final del número.
     * @return El número con los dígitos ocultos, dejando visibles solo los últimos 'mostrar' dígitos.
     */
    public static String ocultarNumero(String numero, int mostrar) {
        if (numero == null || numero.length() <= mostrar || mostrar < 0) {
            return numero;  // Retorna el número tal cual si es nulo, la longitud es menor o igual que los caracteres a mostrar, o 'mostrar' es negativo.
        }
        return "*".repeat(numero.length() - mostrar) + numero.substring(numero.length() - mostrar);
    }

    /**
     * Oculta parte de un correo electrónico, dejando visibles los últimos 'mostrar' caracteres antes del '@' y el dominio.
     *
     * @param email El correo electrónico que se quiere ocultar.
     * @param mostrar El número de caracteres a mostrar antes del '@'.
     * @return El correo con la parte inicial oculta y solo la parte después del '@' visible.
     */
    public static String ocultarEmail(String email, int mostrar) {
        if (email == null || !email.contains("@") || mostrar < 0) {
            return email;  // Retorna el correo tal cual si es nulo, no contiene '@' o 'mostrar' es negativo.
        }

        String username = email.substring(0, email.indexOf("@"));
        String domain = email.substring(email.indexOf("@"));

        if (username.length() <= mostrar) {
            return email;  // Si el nombre de usuario tiene menos caracteres que los a mostrar, no ocultamos nada.
        }

        return "*".repeat(username.length() - mostrar) + username.substring(username.length() - mostrar) + domain;
    }

    /**
     * Valida si el string proporcionado tiene el formato de un correo electrónico válido.
     *
     * Este método utiliza una expresión regular para comprobar si el string cumple con el formato estándar de un correo electrónico.
     * La expresión regular valida caracteres alfanuméricos y algunos caracteres especiales permitidos antes de la "@" y el dominio después de ella.
     *
     * @param user El string que se desea verificar si es un correo electrónico.
     * @return `true` si el string tiene el formato de un correo electrónico válido, `false` en caso contrario.
     */
    public static boolean isEmail(String user) {
        // Expresión regular para validar un correo electrónico
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern emailPattern = Pattern.compile(emailRegex);

        // Verifica si el 'user' coincide con el formato de correo electrónico utilizando la expresión regular
        return emailPattern.matcher(user).matches();
    }

}
