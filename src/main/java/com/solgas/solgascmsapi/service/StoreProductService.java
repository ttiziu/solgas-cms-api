package com.solgas.solgascmsapi.service;

import com.solgas.solgascmsapi.dto.CreateProductRequest;
import com.solgas.solgascmsapi.dto.ProductResponse;
import com.solgas.solgascmsapi.dto.UpdateProductRequest;
import com.solgas.solgascmsapi.entity.ImageAsset;
import com.solgas.solgascmsapi.entity.Site;
import com.solgas.solgascmsapi.entity.StoreProduct;
import com.solgas.solgascmsapi.exception.DuplicateProductKeyException;
import com.solgas.solgascmsapi.exception.ProductNotFoundException;
import com.solgas.solgascmsapi.repository.ImageAssetRepository;
import com.solgas.solgascmsapi.repository.StoreProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StoreProductService {

    private final StoreProductRepository productRepository;
    private final ImageAssetRepository imageRepository;
    private final SiteService siteService;
    private final ImageService imageService;

    public StoreProductService(
            StoreProductRepository productRepository,
            ImageAssetRepository imageRepository,
            SiteService siteService,
            ImageService imageService) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.siteService = siteService;
        this.imageService = imageService;
    }

    public List<ProductResponse> listPublic(String siteSlug) {
        siteService.require(siteSlug);
        return productRepository.findBySite_SlugAndActiveTrueOrderBySortOrderAsc(siteSlug).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ProductResponse> listAll(String siteSlug) {
        siteService.require(siteSlug);
        return productRepository.findBySite_SlugOrderBySortOrderAsc(siteSlug).stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductResponse create(String siteSlug, CreateProductRequest request) {
        Site site = siteService.require(siteSlug);
        if (productRepository.findBySite_SlugAndProductKey(siteSlug, request.productKey()).isPresent()) {
            throw new DuplicateProductKeyException(request.productKey());
        }

        StoreProduct product = new StoreProduct();
        product.setSite(site);
        product.setProductKey(request.productKey());
        product.setName(request.name());
        product.setDescription(request.description());
        product.setWhatsappMessage(request.whatsappMessage());
        product.setFallbackImageUrl(normalizeFallback(request.fallbackImageUrl()));
        product.setSortOrder(request.sortOrder() != null
                ? request.sortOrder()
                : productRepository.countBySite_Slug(siteSlug) + 1);

        return toResponse(productRepository.save(product));
    }

    public ProductResponse update(String siteSlug, String productKey, UpdateProductRequest request) {
        StoreProduct product = requireProduct(siteSlug, productKey);
        product.setName(request.name());
        product.setDescription(request.description());
        product.setWhatsappMessage(request.whatsappMessage());
        product.setFallbackImageUrl(normalizeFallback(request.fallbackImageUrl()));
        if (request.sortOrder() != null) {
            product.setSortOrder(request.sortOrder());
        }
        if (request.active() != null) {
            product.setActive(request.active());
        }
        return toResponse(productRepository.save(product));
    }

    @Transactional
    public void delete(String siteSlug, String productKey) {
        StoreProduct product = requireProduct(siteSlug, productKey);
        for (ImageAsset image : imageRepository.findBySite_SlugAndSectionOrderByCreatedAtDesc(siteSlug, productKey)) {
            imageService.deleteByAsset(image);
        }
        productRepository.delete(product);
    }

    @Transactional
    public void reorder(String siteSlug, List<String> productKeys) {
        siteService.require(siteSlug);
        List<StoreProduct> existing = productRepository.findBySite_SlugOrderBySortOrderAsc(siteSlug);

        if (existing.size() != productKeys.size()) {
            throw new IllegalArgumentException("Debes enviar todos los productos del sitio en el nuevo orden.");
        }

        Map<String, StoreProduct> byKey = existing.stream()
                .collect(Collectors.toMap(StoreProduct::getProductKey, Function.identity()));

        for (String key : productKeys) {
            if (!byKey.containsKey(key)) {
                throw new ProductNotFoundException();
            }
        }

        for (int index = 0; index < productKeys.size(); index++) {
            byKey.get(productKeys.get(index)).setSortOrder(index + 1);
        }
    }

    private StoreProduct requireProduct(String siteSlug, String productKey) {
        siteService.require(siteSlug);
        return productRepository.findBySite_SlugAndProductKey(siteSlug, productKey)
                .orElseThrow(ProductNotFoundException::new);
    }

    private ProductResponse toResponse(StoreProduct product) {
        String siteSlug = product.getSite().getSlug();
        var cmsImages = imageRepository.findBySite_SlugAndSectionOrderByCreatedAtDesc(
                siteSlug, product.getProductKey());
        var cmsImage = cmsImages.stream().findFirst();

        String imageUrl = cmsImage.map(ImageAsset::getUrl)
                .orElseGet(() -> normalizeFallback(product.getFallbackImageUrl()));
        Long cmsImageId = cmsImage.map(ImageAsset::getId).orElse(null);

        return new ProductResponse(
                product.getProductKey(),
                product.getName(),
                product.getDescription(),
                product.getWhatsappMessage(),
                imageUrl,
                product.getFallbackImageUrl(),
                product.getSortOrder(),
                product.isActive(),
                cmsImageId
        );
    }

    private static String normalizeFallback(String url) {
        return url == null ? "" : url.trim();
    }
}
