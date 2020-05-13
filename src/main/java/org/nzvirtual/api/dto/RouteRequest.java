package org.nzvirtual.api.dto;

public class RouteRequest {
    private Long id;
    private String airline;
    private int flightNumber;
    private String atcIdent;
    private String departure;
    private String arrival;
    private String days;
    private String equipment;
    private String departureTime;
    private String arrivalTime;
    private String duration;
    private String route;
    private int altitude;
    private String notes;

    public RouteRequest(Long id, String airline, int flightNumber, String AtcIdent, String departure, String arrival, String days, String equipment, String departureTime, String arrivalTime, String duration, String route, int altitude, String notes) {
        this.id = 0L;
        if (id != null) {
            this.id = id;
        }
        this.airline = airline;
        this.flightNumber = flightNumber;
        this.atcIdent = AtcIdent;
        this.departure = departure;
        this.arrival = arrival;
        this.days = days;
        this.equipment = equipment;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
        this.route = route;
        this.altitude = altitude;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getAtcIdent() {
        return atcIdent;
    }

    public void setAtcIdent(String atcIdent) {
        this.atcIdent = atcIdent;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
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
