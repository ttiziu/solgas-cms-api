package com.solgas.solgascmsapi.repository;

import com.solgas.solgascmsapi.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SiteRepository extends JpaRepository<Site, String> {

    List<Site> findAllByOrderByNameAsc();
}
