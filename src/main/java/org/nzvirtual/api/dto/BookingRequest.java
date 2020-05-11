package org.nzvirtual.api.dto;

public class BookingRequest {
    private Long routeId;

    public BookingRequest(long routeId) {
        this.routeId = Long.valueOf(routeId);
    }

    public BookingRequest() {}

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }
}
