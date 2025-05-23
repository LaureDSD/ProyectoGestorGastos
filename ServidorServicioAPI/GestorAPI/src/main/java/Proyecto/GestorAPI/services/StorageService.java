package Proyecto.GestorAPI.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface StorageService {

    /**
     * Guarda un archivo de imagen en la ruta especificada.
     *
     * @param folderPath Ruta de la carpeta donde se guardará la imagen.
     * @param file Archivo de imagen a guardar.
     * @return La ruta o nombre con el que se guardó la imagen.
     * @throws IOException Si ocurre un error durante el guardado.
     */
    String saveImageData(String folderPath, MultipartFile file) throws IOException;

    /**
     * Elimina un archivo de imagen o archivo en la ruta especificada.
     *
     * @param filePath Ruta del archivo que se desea eliminar.
     */
    void deleteImageData(String filePath);

    /**
     * Guarda un archivo cualquiera y devuelve la ruta donde fue guardado.
     *
     * @param file Archivo a guardar.
     * @return Path con la ruta donde fue guardado el archivo.
     * @throws IOException Si ocurre un error durante el guardado.
     */
    Path saveFile(MultipartFile file) throws IOException;
}
