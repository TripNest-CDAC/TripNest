package com.tripnest.crud.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "trips")
public class Trip {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id") private Integer tripId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "package_id") private TravelPackage travelPackage;
    @Column(name = "start_date", nullable = false) private LocalDate startDate;
    @Column(name = "end_date", nullable = false) private LocalDate endDate;
    @Column(name = "seats_available", nullable = false) private Integer seatsAvailable;
    @Enumerated(EnumType.STRING) @Column(name = "trip_status", nullable = false)
    private TripStatus tripStatus = TripStatus.UPCOMING;
    public Integer getTripId(){return tripId;} public TravelPackage getTravelPackage(){return travelPackage;}
    public LocalDate getStartDate(){return startDate;} public LocalDate getEndDate(){return endDate;}
    public Integer getSeatsAvailable(){return seatsAvailable;} public TripStatus getTripStatus(){return tripStatus;}
    public void setTravelPackage(TravelPackage value){travelPackage=value;} public void setStartDate(LocalDate value){startDate=value;}
    public void setEndDate(LocalDate value){endDate=value;} public void setSeatsAvailable(Integer value){seatsAvailable=value;}
    public void setTripStatus(TripStatus value){tripStatus=value;}
}
