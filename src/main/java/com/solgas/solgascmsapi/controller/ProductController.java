package com.solgas.solgascmsapi.controller;

import com.solgas.solgascmsapi.dto.CreateProductRequest;
import com.solgas.solgascmsapi.dto.ProductResponse;
import com.solgas.solgascmsapi.dto.ReorderProductsRequest;
import com.solgas.solgascmsapi.dto.UpdateProductRequest;
import com.solgas.solgascmsapi.service.StoreProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/sites/{site}/products")
public class ProductController {

    private final StoreProductService productService;

    public ProductController(StoreProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponse> list(
            @PathVariable String site,
            @RequestParam(value = "all", defaultValue = "false") boolean all,
            Authentication authentication) {
        if (all) {
            if (authentication == null
                    || !authentication.isAuthenticated()
                    || authentication instanceof AnonymousAuthenticationToken) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
            return productService.listAll(site);
        }
        return productService.listPublic(site);
    }

    @PutMapping("/reorder")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reorder(@PathVariable String site, @Valid @RequestBody ReorderProductsRequest request) {
        productService.reorder(site, request.productKeys());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@PathVariable String site, @Valid @RequestBody CreateProductRequest request) {
        return productService.create(site, request);
    }

    @PutMapping("/{productKey}")
    public ProductResponse update(
            @PathVariable String site,
            @PathVariable String productKey,
            @Valid @RequestBody UpdateProductRequest request) {
        return productService.update(site, productKey, request);
    }

    @DeleteMapping("/{productKey}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String site, @PathVariable String productKey) {
        productService.delete(site, productKey);
    }
}
