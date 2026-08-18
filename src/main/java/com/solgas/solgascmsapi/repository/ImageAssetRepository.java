package com.solgas.solgascmsapi.repository;

import com.solgas.solgascmsapi.entity.ImageAsset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageAssetRepository extends JpaRepository<ImageAsset, Long> {

    List<ImageAsset> findBySite_SlugOrderByCreatedAtDesc(String siteSlug);

    List<ImageAsset> findBySite_SlugAndSectionOrderByCreatedAtDesc(String siteSlug, String section);
}
