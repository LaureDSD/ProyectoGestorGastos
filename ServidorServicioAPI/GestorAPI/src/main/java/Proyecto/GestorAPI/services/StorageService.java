package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface StorageService {

    String createImageData(String folderPath,MultipartFile file) throws IOException;

    void deleteImageData(String filePath);

    Path saveFile(MultipartFile file) throws IOException;
}
