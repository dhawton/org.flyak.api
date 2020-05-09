package org.nzvirtual.api.dto;

import org.nzvirtual.api.data.entity.Equipment;

public class EquipmentRequest {
    private String icao;
    private String newIcao;
    private String name;

    public EquipmentRequest(Equipment equipment) {
        this.icao = equipment.getIcao();
        this.name = equipment.getName();
    }

    public EquipmentRequest(String icao, String newIcao, String name) {
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
