package com.urbanwastehub.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "collection_point")
public class CollectionPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Point code is required")
    @Column(name = "point_code", nullable = false, unique = true)
    private String pointCode;

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;

    private Double latitude;
    private Double longitude;

    @Column(nullable = false)
    private String status = "ACTIVE"; // ACTIVE, INACTIVE, MAINTENANCE

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "collectionPoint", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("collectionPoint")
    private List<WasteBin> bins = new ArrayList<>();

    public CollectionPoint() {
    }

    public CollectionPoint(String pointCode, String location, Double latitude, Double longitude, String status) {
        this.pointCode = pointCode;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status != null ? status : "ACTIVE";
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = "ACTIVE";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPointCode() {
        return pointCode;
    }

    public void setPointCode(String pointCode) {
        this.pointCode = pointCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<WasteBin> getBins() {
        return bins;
    }

    public void setBins(List<WasteBin> bins) {
        this.bins = bins;
    }
}
