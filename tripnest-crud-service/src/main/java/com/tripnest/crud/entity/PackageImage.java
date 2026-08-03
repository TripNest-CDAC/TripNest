package com.tripnest.crud.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "package_image")
public class PackageImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id") private Integer imageId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "package_id") private TravelPackage travelPackage;
    @Column(name = "image_path", nullable = false) private String imagePath;
    @Column(name = "is_thumbnail", nullable = false) private boolean thumbnail;
    public Integer getImageId() { return imageId; }
    public TravelPackage getTravelPackage() { return travelPackage; }
    public String getImagePath() { return imagePath; }
    public boolean isThumbnail() { return thumbnail; }
    public void setTravelPackage(TravelPackage value) { travelPackage = value; }
    public void setImagePath(String value) { imagePath = value; }
    public void setThumbnail(boolean value) { thumbnail = value; }
}
