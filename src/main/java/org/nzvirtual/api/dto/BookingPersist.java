package org.nzvirtual.api.dto;

import org.nzvirtual.api.data.entity.*;

import java.time.LocalDateTime;

public class BookingPersist {
    private User user;
    private Airline airline;
    private String flight_number;
    private Airport departure;
    private Airport arrival;
    private Equipment equipment;
    private String departureTime;
    private String arrivalTime;
    private String duration;
    private String atcident;
    private LocalDateTime plannedTime;

    public BookingPersist(User user, Route route, LocalDateTime plannedTime) {
        this.user = user;
        this.airline = route.getAirline();
        this.flight_number = route.getFlightNumber();
        this.departure = route.getDeparture();
        this.arrival = route.getArrival();
        this.equipment = route.getEquipment();
        this.departureTime = route.getDepartureTime();
        this.arrivalTime = route.getArrivalTime();
        this.duration = route.getDuration();
        this.atcident = route.getAtcIdent();
        this.plannedTime = plannedTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    public String getFlight_number() {
        return flight_number;
    }

    public void setFlight_number(String flight_number) {
        this.flight_number = flight_number;
    }

    public Airport getDeparture() {
        return departure;
    }

    public void setDeparture(Airport departure) {
        this.departure = departure;
    }

    public Airport getArrival() {
        return arrival;
    }

    public void setArrival(Airport arrival) {
        this.arrival = arrival;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAtcident() {
        return atcident;
    }

    public void setAtcident(String atcident) {
        this.atcident = atcident;
    }

    public LocalDateTime getPlannedTime() {
        return plannedTime;
    }

    public void setPlannedTime(LocalDateTime plannedTime) {
        this.plannedTime = plannedTime;
    }
}
