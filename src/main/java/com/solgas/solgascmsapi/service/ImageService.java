package com.solgas.solgascmsapi.service;

import com.solgas.solgascmsapi.entity.ImageAsset;
import com.solgas.solgascmsapi.entity.Site;
import com.solgas.solgascmsapi.exception.ImageNotFoundException;
import com.solgas.solgascmsapi.repository.ImageAssetRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    private final S3Client s3Client;
    private final ImageAssetRepository repository;
    private final SiteService siteService;
    private final ImageOptimizationService imageOptimizationService;

    @Value("${r2.bucket}")
    private String bucket;

    @Value("${r2.public-url:}")
    private String publicUrl;

    public ImageService(
            S3Client s3Client,
            ImageAssetRepository repository,
            SiteService siteService,
            ImageOptimizationService imageOptimizationService) {
        this.s3Client = s3Client;
        this.repository = repository;
        this.siteService = siteService;
        this.imageOptimizationService = imageOptimizationService;
    }

    public ImageAsset upload(MultipartFile file, String siteSlug, String section) {
        Site site = siteService.require(siteSlug);
        replaceSection(siteSlug, section);

        ImageOptimizationService.OptimizedImage optimized;
        try {
            optimized = imageOptimizationService.optimize(file);
        } catch (IllegalArgumentException exception) {
            throw exception;
        } catch (IOException exception) {
            throw new IllegalArgumentException("No se pudo procesar la imagen. Usa JPG, PNG o WebP.", exception);
        } catch (RuntimeException exception) {
            throw new IllegalArgumentException("No se pudo procesar la imagen. Intenta con otra foto.", exception);
        }

        String key = site.getSlug() + "/" + section + "/" + UUID.randomUUID() + optimized.extension();

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType(optimized.contentType())
                        .build(),
                RequestBody.fromBytes(optimized.data())
        );

        ImageAsset asset = new ImageAsset();
        asset.setKey(key);
        asset.setSite(site);
        asset.setSection(section);
        asset.setUrl(publicUrl + "/" + key);
        return repository.save(asset);
    }

    public List<ImageAsset> list(String siteSlug, String section) {
        siteService.require(siteSlug);
        if (section == null || section.isBlank()) {
            return repository.findBySite_SlugOrderByCreatedAtDesc(siteSlug);
        }
        return repository.findBySite_SlugAndSectionOrderByCreatedAtDesc(siteSlug, section);
    }

    public void delete(String siteSlug, Long id) {
        siteService.require(siteSlug);
        ImageAsset asset = repository.findById(id)
                .orElseThrow(ImageNotFoundException::new);

        if (asset.getSite() == null || !siteSlug.equals(asset.getSite().getSlug())) {
            throw new ImageNotFoundException();
        }

        deleteByAsset(asset);
    }

    void deleteByAsset(ImageAsset asset) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(asset.getKey())
                .build());
        repository.delete(asset);
    }

    private void replaceSection(String siteSlug, String section) {
        for (ImageAsset existing : repository.findBySite_SlugAndSectionOrderByCreatedAtDesc(siteSlug, section)) {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(existing.getKey())
                    .build());
            repository.delete(existing);
        }
    }
}
