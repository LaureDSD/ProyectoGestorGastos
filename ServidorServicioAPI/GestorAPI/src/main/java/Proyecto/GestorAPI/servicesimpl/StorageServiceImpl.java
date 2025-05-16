package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RequiredArgsConstructor
@Service
public class StorageServiceImpl implements StorageService {

    private String STORAGE_PATH = "C:/uploads/";

    @Override
    public String saveImageData(String folderPath, MultipartFile imagen) throws IOException {
        String nombreArchivo = System.currentTimeMillis() + "-" + imagen.getOriginalFilename();
        File carpeta = new File(STORAGE_PATH+folderPath);
        carpeta.mkdirs();
        File archivoDestino = new File(carpeta, nombreArchivo);
        imagen.transferTo(archivoDestino);
        System.out.println("Subido: " + folderPath + nombreArchivo);
        return folderPath + nombreArchivo;
    }


    @Override
    public void deleteImageData(String publicUrlPath) {
        System.out.println( "Borrando1: " + publicUrlPath);
        String relativePath = publicUrlPath.startsWith("/") ? publicUrlPath.substring(1) : publicUrlPath;
        File file = new File(STORAGE_PATH + relativePath);
        System.out.println( "Borrando2: " + relativePath);
        if (file.exists()) {
            boolean deleted = file.delete();
            System.out.println("Eliminado: " + deleted);
        } else {
            System.out.println("No encontrado: " + file.getAbsolutePath());
        }
    }

    @Override
    public Path saveFile(MultipartFile file) throws IOException {
        Path tempFile = Files.createTempFile("ocr_", "_"+file.getOriginalFilename());
        file.transferTo(tempFile);
        return tempFile;
    }
}
