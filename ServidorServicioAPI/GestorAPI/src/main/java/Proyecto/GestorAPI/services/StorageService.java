package Proyecto.GestorAPI.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface StorageService {

    String saveImageData(String folderPath, MultipartFile file) throws IOException;

    void deleteImageData(String filePath);

    Path saveFile(MultipartFile file) throws IOException;
}
