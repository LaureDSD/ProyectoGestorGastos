package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Implementación del servicio de almacenamiento de archivos e imágenes.
 *
 * Proporciona métodos para guardar imágenes en disco, eliminar imágenes y
 * guardar archivos temporales para procesamiento, por ejemplo OCR.
 */
@RequiredArgsConstructor
@Service
public class StorageServiceImpl implements StorageService {

    /**
     * Ruta base donde se almacenan los archivos y carpetas de subida.
     * Puede configurarse o modificarse según el entorno.
     */
    private String STORAGE_PATH = "C:/uploads/";

    /**
     * Guarda una imagen recibida en un directorio específico dentro de la ruta base.
     * El nombre del archivo se genera con un timestamp para evitar colisiones.
     *
     * @param folderPath Ruta relativa de la carpeta donde se guardará la imagen.
     * @param imagen     Archivo MultipartFile que contiene la imagen a guardar.
     * @return La ruta relativa donde fue guardada la imagen (folderPath + nombre archivo).
     * @throws IOException Si ocurre un error durante la transferencia del archivo.
     */
    @Override
    public String saveImageData(String folderPath, MultipartFile imagen) throws IOException {
        String nombreArchivo = System.currentTimeMillis() + "-" + imagen.getOriginalFilename();
        File carpeta = new File(STORAGE_PATH + folderPath);
        carpeta.mkdirs();  // Crea la carpeta si no existe
        File archivoDestino = new File(carpeta, nombreArchivo);
        imagen.transferTo(archivoDestino);  // Guarda el archivo físicamente en disco
        System.out.println("Subido: " + folderPath + nombreArchivo);
        return folderPath + nombreArchivo;
    }

    /**
     * Elimina un archivo de imagen basado en su ruta pública relativa.
     *
     * @param publicUrlPath Ruta pública relativa del archivo a eliminar.
     *                      Se elimina la barra inicial si existe y se concatena
     *                      con la ruta base para buscar el archivo en disco.
     */
    @Override
    public void deleteImageData(String publicUrlPath) {
        System.out.println("Borrando1: " + publicUrlPath);
        String relativePath = publicUrlPath.startsWith("/") ? publicUrlPath.substring(1) : publicUrlPath;
        File file = new File(STORAGE_PATH + relativePath);
        System.out.println("Borrando2: " + relativePath);
        if (file.exists()) {
            boolean deleted = file.delete();
            System.out.println("Eliminado: " + deleted);
        } else {
            System.out.println("No encontrado: " + file.getAbsolutePath());
        }
    }

    /**
     * Guarda un archivo MultipartFile temporalmente en disco,
     * generando un archivo temporal con prefijo "ocr_" y el nombre original.
     *
     * @param file Archivo MultipartFile a guardar temporalmente.
     * @return Path al archivo temporal creado.
     * @throws IOException Si falla la creación o transferencia del archivo.
     */
    @Override
    public Path saveFile(MultipartFile file) throws IOException {
        Path tempFile = Files.createTempFile("ocr_", "_" + file.getOriginalFilename());
        file.transferTo(tempFile);
        return tempFile;
    }
}
