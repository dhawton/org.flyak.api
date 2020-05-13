package org.nzvirtual.api.data.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.Type;
import org.nzvirtual.api.data.misc.Days;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="routes")
public class Route implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne
    @JoinColumn(name = "airline", referencedColumnName = "icao")
    private Airline airline;
    @Column(name = "flight_number")
    private int flightNumber;
    @OneToOne
    @JoinColumn(name = "departure", referencedColumnName = "icao")
    private Airport departure;
    @OneToOne
    @JoinColumn(name = "arrival", referencedColumnName = "icao")
    private Airport arrival;
    @Column(name="monday")
    private boolean monday;
    @Column(name="tuesday")
    private boolean tuesday;
    @Column(name="wednesday")
    private boolean wednesday;
    @Column(name="thursday")
    private boolean thursday;
    @Column(name="friday")
    private boolean friday;
    @Column(name="saturday")
    private boolean saturday;
    @Column(name="sunday")
    private boolean sunday;
    @OneToOne
    @JoinColumn(name = "equipment", referencedColumnName = "icao")
    private Equipment equipment;
    @Column(name = "departure_time")
    private String departureTime;
    @Column(name = "arrival_time")
    private String arrivalTime;
    @Column(name = "duration")
    private String duration;
    @Column(name = "routing")
    private String routing;
    @Column(name = "altitude")
    private int altitude;
    @Column(name = "notes")
    private String notes;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public boolean isMonday() {
        return monday;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
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

    public String getRouting() {
        return routing;
    }

    public void setRouting(String routing) {
        this.routing = routing;
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
