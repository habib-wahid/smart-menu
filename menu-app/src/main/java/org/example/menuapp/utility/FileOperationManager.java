package org.example.menuapp.utility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.menuapp.config.FileStorageConfig;
import org.example.menuapp.error.custom_exceptions.SmFileStorageException;
import org.example.menuapp.error.messages.ExceptionMessages;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileOperationManager {

    private final FileStorageConfig fileStorageConfig;

    public String copyFile(MultipartFile file) {

        Path targetPath;

        try {
            Path uploadPath = ensurePathExists(fileStorageConfig.getFilepath());
            String fileName = generateFileName(file.getOriginalFilename());
            targetPath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.info("Could not copy file: {}", e.getMessage());
            throw new SmFileStorageException(ExceptionMessages.FILE_NOT_ABLE_TO_SAVE);
        }

        return targetPath.toString();
    }

    private Path ensurePathExists(Path uploadPath) throws IOException{
        if (!Files.exists(uploadPath)) {
            return Files.createDirectories(uploadPath);
        }

        return uploadPath;
    }

    private String generateFileName(String originalFileName) {
        String fileName = originalFileName != null ? originalFileName : "unknown";
        return "sm_files" + "_" + System.currentTimeMillis() +  fileName;
    }

}
