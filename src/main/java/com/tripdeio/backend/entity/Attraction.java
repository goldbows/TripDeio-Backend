package com.tripdeio.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tripdeio.backend.entity.enums.AttractionStatus;
import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Attraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double lat;
    private Double lng;
    private Integer visitDurationMinutes;
    private Double ticketPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttractionStatus status = AttractionStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "submitted_by")
    private AppUser submittedBy;

    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AttractionImage> images;

    @Column(columnDefinition = "geometry(Point, 4326)")
    @JsonBackReference
    private Point geom;

    private LocalDateTime lastModifiedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "original_id")
    private Attraction originalAttraction;

    @ManyToOne
    private AppUser approvedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getVisitDurationMinutes() {
        return visitDurationMinutes;
    }

    public void setVisitDurationMinutes(Integer visitDurationMinutes) {
        this.visitDurationMinutes = visitDurationMinutes;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public AttractionStatus getStatus() {
        return status;
    }

    public void setStatus(AttractionStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public AppUser getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(AppUser submittedBy) {
        this.submittedBy = submittedBy;
    }

    public List<AttractionImage> getImages() {
        return images;
    }

    public void setImages(List<AttractionImage> images) {
        this.images = images;
    }

    public Point getGeom() {
        return geom;
    }

    public void setGeom(Point geom) {
        this.geom = geom;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public Attraction getOriginalAttraction() {
        return originalAttraction;
    }

    public void setOriginalAttraction(Attraction originalAttraction) {
        this.originalAttraction = originalAttraction;
    }

    public AppUser getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(AppUser approvedBy) {
        this.approvedBy = approvedBy;
    }
}