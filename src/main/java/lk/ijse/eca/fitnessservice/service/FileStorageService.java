package lk.ijse.eca.fitnessservice.service;

import org.springframework.core.io.Resource;
import java.io.InputStream;

public interface FileStorageService {
    String storeFile(String filename, InputStream inputStream, String contentType);
    Resource getFileAsResource(String fileIdentifier);
}
