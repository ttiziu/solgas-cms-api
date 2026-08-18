package com.solgas.solgascmsapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sites")
public class Site {

    @Id
    @Column(length = 64)
    private String slug;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "public_url", length = 255)
    private String publicUrl;

    public String getSlug() {
        return slug;
    }

    public String getName() {
        return name;
    }

    public String getPublicUrl() {
        return publicUrl;
    }
}
