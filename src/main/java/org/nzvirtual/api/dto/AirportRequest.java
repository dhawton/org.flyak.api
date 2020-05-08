package org.nzvirtual.api.dto;

import org.nzvirtual.api.data.entity.Airport;

public class AirportRequest {
    private String icao;
    private String newIcao;
    private String name;
    private Double lat;
    private Double lon;

    public AirportRequest(Airport airport) {
        this.icao = airport.getIcao();
        this.name = airport.getName();
        this.lat = airport.getLat();
        this.lon = airport.getLon();
    }

    public String getNewIcao() {
        return newIcao;
    }

    public void setNewIcao(String newIcao) {
        this.newIcao = newIcao;
    }

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
