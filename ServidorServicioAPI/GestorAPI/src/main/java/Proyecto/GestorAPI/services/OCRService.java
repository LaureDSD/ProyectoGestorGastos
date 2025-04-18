package Proyecto.GestorAPI.services;

import java.io.File;
import java.io.IOException;

public interface OCRService {

    /**
     * Envía un archivo al servicio de Python para realizar el proceso de OCR (Reconocimiento Óptico de Caracteres).
     *
     * Este método permite enviar un archivo (generalmente una imagen o documento escaneado) al servicio de Python
     * que ejecuta un algoritmo de OCR para extraer texto del archivo. Se espera que el servicio de Python
     * devuelva el texto extraído del archivo en formato de cadena.
     *
     * @param tempFile El archivo que se enviará para ser procesado por el servicio OCR.
     * @return Una cadena de texto que representa los resultados del OCR del archivo procesado.
     * @throws IOException Si ocurre un error durante la lectura del archivo o al comunicarse con el servicio de Python.
     */
    String sendFileForOCR(File tempFile) throws IOException;

}
