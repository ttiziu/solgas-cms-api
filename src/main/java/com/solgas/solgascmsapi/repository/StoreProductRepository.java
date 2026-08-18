package com.solgas.solgascmsapi.repository;

import com.solgas.solgascmsapi.entity.StoreProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreProductRepository extends JpaRepository<StoreProduct, Long> {

    List<StoreProduct> findBySite_SlugAndActiveTrueOrderBySortOrderAsc(String siteSlug);

    List<StoreProduct> findBySite_SlugOrderBySortOrderAsc(String siteSlug);

    Optional<StoreProduct> findBySite_SlugAndProductKey(String siteSlug, String productKey);

    int countBySite_Slug(String siteSlug);
}
