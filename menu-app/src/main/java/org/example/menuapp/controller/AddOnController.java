package org.example.menuapp.controller;

import org.example.menuapp.dto.request.AddonRequest;
import org.example.menuapp.dto.request.AddonResponse;
import org.example.menuapp.service.AddonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/addon")
public class AddOnController {

    private final AddonService addonService;
    public AddOnController(AddonService addonService) {
        this.addonService = addonService;
    }

    @GetMapping
    public ResponseEntity<List<AddonResponse>> getAllAddons() {
        return ResponseEntity.ok(addonService.getAllAddons());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createAddon(
            @RequestPart(name = "addon")AddonRequest addonRequest,
            @RequestPart(name = "file") MultipartFile file) {
        addonService.createAddOn(addonRequest, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
