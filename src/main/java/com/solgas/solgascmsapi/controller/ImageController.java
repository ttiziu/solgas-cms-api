package com.solgas.solgascmsapi.controller;

import com.solgas.solgascmsapi.dto.ImageResponse;
import com.solgas.solgascmsapi.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/sites/{site}/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public List<ImageResponse> list(
            @PathVariable String site,
            @RequestParam(value = "section", required = false) String section) {
        return imageService.list(site, section).stream()
                .map(ImageResponse::from)
                .toList();
    }

    @PostMapping(consumes = "multipart/form-data")
    public ImageResponse upload(
            @PathVariable String site,
            @RequestParam("file") MultipartFile file,
            @RequestParam("section") String section) {
        return ImageResponse.from(imageService.upload(file, site, section));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String site, @PathVariable Long id) {
        imageService.delete(site, id);
    }
}
