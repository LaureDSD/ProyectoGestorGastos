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


    //Falta comentar
    @Override
    public String createImageData(String folderPath, MultipartFile file) {
        return "";
    }

    @Override
    public String updateImageData(String folderPath, MultipartFile file) {
        return "";
    }

    @Override
    public void deleteImageData(String filePath) {

    }

    @Override
    public Path saveFile(MultipartFile file) throws IOException {
        Path tempFile = Files.createTempFile("ocr_", "_"+file.getOriginalFilename());
        file.transferTo(tempFile);
        return tempFile;
    }
}
