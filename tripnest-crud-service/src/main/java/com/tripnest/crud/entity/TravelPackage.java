package com.tripnest.crud.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "travel_package")
public class TravelPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id")
    private Integer packageId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "package_name", nullable = false, length = 150)
    private String packageName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "source", nullable = false, length = 100)
    private String source;

    @Column(name = "destination", nullable = false, length = 100)
    private String destination;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destination_id", nullable = false)
    private Destination destinationInfo;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PackageStatus status = PackageStatus.ACTIVE;

    public Integer getPackageId() { return packageId; }
    public Company getCompany() { return company; }
    public String getPackageName() { return packageName; }
    public String getDescription() { return description; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public Destination getDestinationInfo() { return destinationInfo; }
    public BigDecimal getPrice() { return price; }
    public PackageStatus getStatus() { return status; }
    public void setPackageId(Integer packageId) { this.packageId = packageId; }
    public void setCompany(Company company) { this.company = company; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    public void setDescription(String description) { this.description = description; }
    public void setSource(String source) { this.source = source; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setDestinationInfo(Destination destinationInfo) { this.destinationInfo = destinationInfo; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setStatus(PackageStatus status) { this.status = status; }
}
