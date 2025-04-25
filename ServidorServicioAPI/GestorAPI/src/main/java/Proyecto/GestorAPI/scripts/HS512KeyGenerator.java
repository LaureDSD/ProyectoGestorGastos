package Proyecto.GestorAPI.scripts;

import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;

/**
 * Clase que genera una clave secreta para firmar JWT usando el algoritmo HS512 (HMAC con SHA-512).
 * Esta clave será utilizada para firmar y verificar los tokens JWT en la aplicación.
 *
 * El algoritmo HS512 utiliza una clave secreta compartida para firmar los tokens, lo que significa
 * que tanto el emisor como el receptor deben conocer la misma clave para validar los tokens.
 */
public class HS512KeyGenerator {

    /**
     * Método principal que genera una clave secreta para el algoritmo HS512 y la imprime en formato Base64.
     * La clave generada se utiliza para firmar y verificar JWTs con el algoritmo HMAC-SHA512.
     *
     * @param args Argumentos de la línea de comandos (no utilizados en este caso).
     */
    public static void main(String[] args) {
        // Genera una clave secreta segura utilizando el algoritmo HS512 (HMAC con SHA-512)
        SecretKey key = Jwts.SIG.HS512.key().build();

        // Convierte la clave secreta a formato Base64 para almacenarla de manera segura o compartirla
        String base64Key = java.util.Base64.getEncoder().encodeToString(key.getEncoded());

        // Imprime la clave en Base64 para su uso en la configuración del JWT
        System.out.println("Clave HS512 (Base64): " + base64Key);
    }
}
