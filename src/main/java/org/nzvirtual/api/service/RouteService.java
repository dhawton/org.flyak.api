package org.nzvirtual.api.service;

import org.nzvirtual.api.data.entity.Route;
import org.nzvirtual.api.data.repository.RouteRepository;
import org.nzvirtual.api.dto.RouteResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class RouteService {
    private RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Transactional
    public List<RouteResponse> getRoutes() {
        Iterator<Route> routeIterable = routeRepository.findAll().iterator();
        List<RouteResponse> routeResponses = new ArrayList<>();
        while (routeIterable.hasNext()) {
            RouteResponse response = new RouteResponse(routeIterable.next());
            routeResponses.add(response);
        }
        
        return routeResponses;
    }
}
