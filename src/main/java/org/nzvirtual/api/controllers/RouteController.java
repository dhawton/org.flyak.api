package org.nzvirtual.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.nzvirtual.api.data.entity.Airline;
import org.nzvirtual.api.data.entity.Airport;
import org.nzvirtual.api.data.entity.Equipment;
import org.nzvirtual.api.data.entity.Route;
import org.nzvirtual.api.data.repository.AirlineRepository;
import org.nzvirtual.api.data.repository.AirportRepository;
import org.nzvirtual.api.data.repository.EquipmentRepository;
import org.nzvirtual.api.data.repository.RouteRepository;
import org.nzvirtual.api.dto.GeneralStatusResponse;
import org.nzvirtual.api.dto.RouteRequest;
import org.nzvirtual.api.exception.GeneralException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/route")
public class RouteController {
    private Logger log = LoggerFactory.getLogger(RouteController.class);
    private RouteRepository routeRepository;
    private AirportRepository airportRepository;
    private EquipmentRepository equipmentRepository;
    private AirlineRepository airlineRepository;

    public RouteController(RouteRepository routeRepository, AirportRepository airportRepository, EquipmentRepository equipmentRepository, AirlineRepository airlineRepository) {
        this.routeRepository = routeRepository;
        this.equipmentRepository = equipmentRepository;
        this.airlineRepository = airlineRepository;
        this.airportRepository = airportRepository;
    }

    @Cacheable(value = "routes")
    @GetMapping("")
    @Operation(description = "Get all routes", security = { @SecurityRequirement(name = "bearerAuth") }, responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Route.class)
                            )
                    )
            )
    })
    public Iterable<Route> getRoutes() {
        return this.routeRepository.findAll();
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "routes")
    @Operation(
            description = "Delete route",
            security = { @SecurityRequirement(name = "bearerAuth") },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    schema = @Schema(implementation = GeneralStatusResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content()
                    )
            }
    )
    public ResponseEntity<GeneralStatusResponse> deleteEquipment(@PathVariable Long id) {
        Optional<Route> routeOptional = routeRepository.findById(id);
        if (routeOptional.isEmpty())
            throw new GeneralException("Not Found", HttpStatus.NOT_FOUND);

        routeRepository.delete(routeOptional.get());
        return new ResponseEntity<>(new GeneralStatusResponse("OK"), HttpStatus.OK);
    }

    @CacheEvict(value = "routes")
    @RequestMapping(
            value = {
                ""
            },
            method = {
                    RequestMethod.POST,
                    RequestMethod.PUT
            }
    )
    @Operation(
            description = "Add/Edit Route",
            security = { @SecurityRequirement(name = "bearerAuth") },
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    schema = @Schema(implementation = GeneralStatusResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content()
                    )
            }
    )
    public ResponseEntity<? extends Object> putRoute(@RequestBody RouteRequest routeRequest) {
        Airline airline;
        Route route;

        Optional<Airline> airlineOptional = airlineRepository.findByIcao(routeRequest.getAirline());
        if (airlineOptional.isEmpty()) {
            throw new GeneralException("Airline not found", HttpStatus.NOT_FOUND);
        }

        Optional<Airport> departureAirportOptional = airportRepository.findByIcao(routeRequest.getDeparture());
        if (departureAirportOptional.isEmpty()) {
            throw new GeneralException("Departure airport not found", HttpStatus.NOT_FOUND);
        }

        Optional<Airport> arrivalAirportOptional = airportRepository.findByIcao(routeRequest.getArrival());
        if (arrivalAirportOptional.isEmpty()) {
            throw new GeneralException("Arrival airport not found", HttpStatus.NOT_FOUND);
        }

        Optional<Equipment> equipmentOptional = equipmentRepository.findByIcao(routeRequest.getEquipment());
        if (equipmentOptional.isEmpty()) {
            throw new GeneralException("Equipment not found", HttpStatus.NOT_FOUND);
        }

        Optional<Route> routeOptional = this.routeRepository.findById(routeRequest.getId());
        if (routeOptional.isEmpty()) {
            route = new Route();
        } else {
            route = routeOptional.get();
        }

        route.setAirline(airlineOptional.get());
        route.setFlightNumber(routeRequest.getFlightNumber());
        route.setDeparture(departureAirportOptional.get());
        route.setArrival(arrivalAirportOptional.get());
        route.setEquipment(equipmentOptional.get());
        route.setDays(routeRequest.getDays());
        route.setDepartureTime(routeRequest.getDepartureTime());
        route.setDuration(routeRequest.getDuration());
        route.setRouting(routeRequest.getRoute());
        route.setAltitude(routeRequest.getAltitude());
        route.setNotes(routeRequest.getNotes());

        routeRepository.save(route);

        return new ResponseEntity<>(new GeneralStatusResponse("Created"), HttpStatus.CREATED);
    }
}
