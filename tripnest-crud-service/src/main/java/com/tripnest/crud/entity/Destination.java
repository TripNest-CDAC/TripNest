package com.tripnest.crud.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "destination")
public class Destination {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "destination_id") private Integer destinationId;
    @Column(name = "city_name", nullable = false) private String cityName;
    @Column(name = "state_name", nullable = false) private String stateName;
    @Column(name = "active", nullable = false) private boolean active;

    public Integer getDestinationId() { return destinationId; }
    public String getCityName() { return cityName; }
    public String getStateName() { return stateName; }
    public boolean isActive() { return active; }
    public void setCityName(String cityName) { this.cityName = cityName; }
    public void setStateName(String stateName) { this.stateName = stateName; }
    public void setActive(boolean active) { this.active = active; }
}
