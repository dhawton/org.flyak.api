package org.flyak.api.dto;

import org.flyak.api.data.entity.Equipment;

public class EquipmentRequest {
    private String icao;
    private String newIcao;
    private String name;

    public EquipmentRequest(Equipment equipment) {
        this.icao = equipment.getIcao();
        this.name = equipment.getName();
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
