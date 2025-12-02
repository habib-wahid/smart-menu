package org.example.menuapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.menuapp.dto.request.AddonRequest;
import org.example.menuapp.dto.response.AddonResponse;
import org.example.menuapp.dto.response.ApiResponse;
import org.example.menuapp.service.AddonService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/addon")
@RequiredArgsConstructor
public class AddonController {

    private final AddonService addonService;

    @GetMapping
    public ResponseEntity<List<AddonResponse>> getAllAddons() {
        return ResponseEntity.ok(addonService.getAllAddons());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Void> createAddon(
            @RequestPart(name = "addon")AddonRequest addonRequest,
            @RequestPart(name = "file", required = false) MultipartFile file) {
        addonService.createAddon(addonRequest, file);
        return ApiResponse.success("Addon Created Successfully", null);
    }
}
