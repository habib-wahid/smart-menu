package org.example.menuapp.service;

import org.example.menuapp.config.FileStorageConfig;
import org.example.menuapp.dto.CategoryRequest;
import org.example.menuapp.entity.Category;
import org.example.menuapp.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final FileStorageConfig fileStorageConfig;

    public CategoryService(CategoryRepository categoryRepository, FileStorageConfig fileStorageConfig) {
        this.categoryRepository = categoryRepository;
        this.fileStorageConfig = fileStorageConfig;
    }


    public void createCategory(
            CategoryRequest categoryRequest,
            MultipartFile uploadedFile
    ) {
        Path uploadPath = fileStorageConfig.getFilepath();

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID() + "_" + uploadedFile.getOriginalFilename();
            Path targetPath = uploadPath.resolve(fileName);
            Files.copy(uploadedFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            Category category = Category.builder()
                    .name(categoryRequest.getName())
                    .description(categoryRequest.getDescription())
                    .imageUrl(fileName)
                    .build();

            categoryRepository.save(category);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
