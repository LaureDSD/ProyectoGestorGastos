package Proyecto.GestorAPI.security;

import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;

public class HS512KeyGenerator {
    public static void main(String[] args) {
        // Genera una clave segura para HS512 (512 bits)
        SecretKey key = Jwts.SIG.HS512.key().build();

        // Convierte la clave a Base64 para almacenarla o usarla
        String base64Key = java.util.Base64.getEncoder().encodeToString(key.getEncoded());

        System.out.println("Clave HS512 (Base64): " + base64Key);
    }
}