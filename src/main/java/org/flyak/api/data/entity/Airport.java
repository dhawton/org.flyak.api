package org.flyak.api.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name="airports")
public class Airport {
    @Id
    @JsonIgnore
    private String icao;
    @Column(name="name")
    private String name;
    @Column(name="lat")
    private Double lat;
    @Column(name="lon")
    private Double lon;

    public String getId() {
        return icao;
    }

    public void setId(String id) {
        this.icao = icao;
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
