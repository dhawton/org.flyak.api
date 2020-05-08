package org.flyak.api.dto;

import org.flyak.api.data.entity.Airline;

public class AirlineRequest {
    private String icao;
    private String newIcao;
    private String name;

    public AirlineRequest(Airline airline) {
        this.icao = airline.getIcao();
        this.name = airline.getName();
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
