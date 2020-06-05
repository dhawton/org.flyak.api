package org.nzvirtual.api.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookingRequest {
    private Long routeId;
    private LocalDateTime plannedTime;

    public BookingRequest(long routeId, String plannedTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        this.plannedTime = LocalDateTime.parse(plannedTime, dateTimeFormatter);
        this.routeId = Long.valueOf(routeId);
    }

    public BookingRequest() {}

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public LocalDateTime getPlannedTime() {
        return plannedTime;
    }

    public void setPlannedTime(LocalDateTime plannedTime) {
        this.plannedTime = plannedTime;
    }
}
