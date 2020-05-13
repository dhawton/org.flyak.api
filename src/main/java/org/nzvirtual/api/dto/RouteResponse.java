package org.nzvirtual.api.dto;

import org.nzvirtual.api.data.entity.Airline;
import org.nzvirtual.api.data.entity.Airport;
import org.nzvirtual.api.data.entity.Equipment;
import org.nzvirtual.api.data.entity.Route;
import org.nzvirtual.api.data.misc.Days;

public class RouteResponse {
    private Long id;
    private Airline airline;
    private int flightNumber;
    private Airport departure;
    private Airport arrival;
    private Days days;
    private Equipment equipment;
    private String departureTime;
    private String arrivalTime;
    private String duration;
    private String route;
    private int altitude;
    private String notes;

    public RouteResponse(Route route) {
        this.id = route.getId();
        this.airline = route.getAirline();
        this.flightNumber = route.getFlightNumber();
        this.departure = route.getDeparture();
        this.arrival = route.getArrival();
        this.days = new Days(route.isMonday(), route.isTuesday(), route.isWednesday(), route.isThursday(), route.isFriday(), route.isSaturday(), route.isSunday());
        this.equipment = route.getEquipment();
        this.departureTime = route.getDepartureTime();
        this.arrivalTime = route.getArrivalTime();
        this.duration = route.getDuration();
        this.route = route.getRouting();
        this.altitude = route.getAltitude();
        this.notes = route.getNotes();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
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

    public Days getDays() {
        return days;
    }

    public void setDays(Days days) {
        this.days = days;
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

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
