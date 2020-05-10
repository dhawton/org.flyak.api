package org.nzvirtual.api.dto;

public class BookingRequest {
    private Long routeId;

    public BookingRequest(Long routeId) {
        this.routeId = routeId;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }
}
