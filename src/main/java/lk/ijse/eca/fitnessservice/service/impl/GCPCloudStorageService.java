package lk.ijse.eca.fitnessservice.service.impl;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lk.ijse.eca.fitnessservice.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class GCPCloudStorageService implements FileStorageService {

    private final Storage storage;
    private final String bucketName;

    public GCPCloudStorageService(Storage storage, @Value("${gcp.storage.bucket-name}") String bucketName) {
        this.storage = storage;
        this.bucketName = bucketName;
    }

    @Override
    public String storeFile(String filename, InputStream inputStream, String contentType) {
        try {
            BlobId blobId = BlobId.of(bucketName, filename);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(contentType)
                    .build();

            storage.createFrom(blobInfo, inputStream);
            return "https://storage.googleapis.com/" + bucketName + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file to GCP Cloud Storage", e);
        }
    }

    @Override
    public Resource getFileAsResource(String fileIdentifier) {
        String tempBlobName = fileIdentifier;
        String prefix = "https://storage.googleapis.com/" + bucketName + "/";
        if (fileIdentifier.startsWith(prefix)) {
            tempBlobName = fileIdentifier.substring(prefix.length());
        }
        final String finalBlobName = tempBlobName;

        byte[] content = storage.readAllBytes(BlobId.of(bucketName, finalBlobName));
        return new ByteArrayResource(content) {
            @Override
            public String getFilename() {
                return finalBlobName;
            }
        };
    }
}
