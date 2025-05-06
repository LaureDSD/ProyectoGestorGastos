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


    @Override
    public String createImageData(String folderPath, MultipartFile imagen) throws IOException {
        String nombreArchivo = System.currentTimeMillis() + "-" + imagen.getOriginalFilename();
        String rutaDestino = folderPath + nombreArchivo;
        File archivoDestino = new File(rutaDestino);
        archivoDestino.getParentFile().mkdirs();
        imagen.transferTo(archivoDestino);
        return folderPath + nombreArchivo;
    }

    @Override
    public void deleteImageData(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public Path saveFile(MultipartFile file) throws IOException {
        Path tempFile = Files.createTempFile("ocr_", "_"+file.getOriginalFilename());
        file.transferTo(tempFile);
        return tempFile;
    }
}
