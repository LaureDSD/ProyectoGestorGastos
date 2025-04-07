package Proyecto.GestorAPI.services;

import java.io.File;
import java.io.IOException;

public interface PythonService {
    String sendFileForOCR(File tempFile) throws IOException;
}
