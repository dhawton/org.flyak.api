package org.nzvirtual.api.dto;

import org.nzvirtual.api.data.entity.Airline;

public class AirlineRequest {
    private String icao;
    private String newIcao;
    private String name;

    public AirlineRequest(Airline airline) {
        this.icao = airline.getIcao();
        this.name = airline.getName();
    }

    public AirlineRequest(String icao, String newIcao, String name) {
        this.icao = icao;
        this.newIcao = newIcao;
        this.name = name;
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
}
