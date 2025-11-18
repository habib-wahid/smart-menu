package org.example.menuapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.menuapp.dto.request.AddonRequest;
import org.example.menuapp.dto.response.AddonResponse;
import org.example.menuapp.entity.Addon;
import org.example.menuapp.entity.Item;
import org.example.menuapp.error.custom_exceptions.SmResourceNotFoundException;
import org.example.menuapp.error.messages.ExceptionMessages;
import org.example.menuapp.mapper.AddonMapper;
import org.example.menuapp.repository.AddonRepository;
import org.example.menuapp.utility.FileOperationManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddonService {

    private final AddonRepository addOnRepository;
    private final ItemService itemService;
    private final AddonMapper addonMapper;
    private final FileOperationManager fileOperationManager;


    public void createAddon(AddonRequest addonRequest, MultipartFile file) {
      //  System.out.println("File " + file.getOriginalFilename());
        Set<Item> itemSet = itemService.getAllItemsByIds(addonRequest.itemIds());
        if (itemSet.size() != addonRequest.itemIds().size()) {
            throw new SmResourceNotFoundException(ExceptionMessages.SOME_ITEMS_ARE_NOT_FOUND);
        }

        Addon addon = addonMapper.toAddon(addonRequest);
        addon.setItem(itemSet);

        if (file != null && !file.isEmpty()) {
            String filePath = fileOperationManager.copyFile(file);
            addon.setFileName(file.getOriginalFilename());
            addon.setFilePath(filePath);
        }

        addOnRepository.save(addon);
    }


    public List<AddonResponse> getAllAddons() {
        List<Addon> addonList = addOnRepository.findAll();
        return addonList.stream()
                .map(addonMapper::toAddonResponse)
                .toList();
    }

    public Addon getAddOnById(Long id) {
        return addOnRepository.findById(id).orElseThrow(
                () -> new SmResourceNotFoundException(
                        String.format(ExceptionMessages.RESOURCE_NOT_FOUND, "Addon", id)));
    }

    public Set<Addon> getAllAddonsByIds(Set<Long> ids) {
       return new HashSet<>(addOnRepository.findAllById(ids));
    }

}
