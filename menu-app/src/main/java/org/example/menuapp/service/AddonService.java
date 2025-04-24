package org.example.menuapp.service;

import lombok.extern.slf4j.Slf4j;
import org.example.menuapp.config.FileStorageConfig;
import org.example.menuapp.dto.request.AddonRequest;
import org.example.menuapp.dto.request.AddonResponse;
import org.example.menuapp.entity.AddOn;
import org.example.menuapp.entity.Item;
import org.example.menuapp.error.custom_exceptions.SmFileStorageException;
import org.example.menuapp.error.messages.ExceptionMessages;
import org.example.menuapp.repository.AddOnRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class AddonService {

    private final AddOnRepository addOnRepository;
    private final FileStorageConfig fileStorageConfig;
    private final ItemService itemService;

    public AddonService(AddOnRepository addOnRepository, FileStorageConfig fileStorageConfig, ItemService itemService) {
        this.addOnRepository = addOnRepository;
        this.fileStorageConfig = fileStorageConfig;
        this.itemService = itemService;
    }

    public void createAddOn(AddonRequest addonRequest, MultipartFile file) {
        AddOn addOn = new AddOn();
        addOn.setName(addonRequest.getName());
        addOn.setDescription(addonRequest.getDescription());
        addOn.setPrice(addonRequest.getPrice());
        if (addonRequest.getItemIds() != null) {
           Set<Item> itemSet = itemService.getAllItemsByIds(addonRequest.getItemIds());
           addOn.setItem(itemSet);
        }

        Path uploadPath = fileStorageConfig.getFilepath();
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path targetPath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            addOn.setFilePath(fileName);
            addOn.setFullPathUrl(targetPath.toString());

        } catch (IOException e) {
            log.info("Could not copy file: {}", e.getMessage());
            throw new SmFileStorageException(ExceptionMessages.FILE_NOT_ABLE_TO_SAVE);
        }

        addOnRepository.save(addOn);
    }


    public List<AddonResponse> getAllAddons() {
        List<AddOn> addOnList = addOnRepository.findAll();
        return addOnList.stream()
                .map(this::mapToAddonResponse)
                .toList();
    }

    private AddonResponse mapToAddonResponse(AddOn addOn) {
        return AddonResponse.builder()
                .id(addOn.getId())
                .name(addOn.getName())
                .description(addOn.getDescription())
                .price(addOn.getPrice())
                .filePath(addOn.getFilePath())
                .fullPathUrl(addOn.getFullPathUrl())
                .rating(addOn.getRating())
                .build();
    }
}
