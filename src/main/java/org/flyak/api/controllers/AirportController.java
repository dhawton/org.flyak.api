package org.flyak.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.flyak.api.data.entity.Airport;
import org.flyak.api.data.repository.AirportRepository;
import org.flyak.api.dto.AirportRequest;
import org.flyak.api.dto.GeneralStatusResponse;
import org.flyak.api.exception.GeneralException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/airport")
public class AirportController {
    private Logger log = LoggerFactory.getLogger(AirportController.class);
    private AirportRepository airportRepository;

    public AirportController(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @GetMapping("/all")
    @Operation(description = "Get all airports", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Airport.class)
                            )
                    )
            )
    })
    public Iterable<Airport> getAirports() {
        return this.airportRepository.findAll();
    }

    @GetMapping("/{icao}")
    @Operation(
            description = "Get airport details",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    schema = @Schema(implementation = Airport.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content()
                    )
            }
    )
    public Airport getAirport(@PathVariable String icao) {
        Optional<Airport> airport = airportRepository.findByIcao(icao);
        if (airport.isEmpty()) {
            throw new GeneralException("Not Found", "", HttpStatus.NOT_FOUND);
        }

        return airport.get();
    }

    @DeleteMapping("/{icao}")
    @Operation(
            description = "Delete airport",
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
    public ResponseEntity<GeneralStatusResponse> deleteAirport(@PathVariable String icao) {
        Optional<Airport> optionalAirport = airportRepository.findByIcao(icao);
        if (optionalAirport.isEmpty())
            throw new GeneralException("Not Found", HttpStatus.NOT_FOUND);

        airportRepository.delete(optionalAirport.get());
        return new ResponseEntity<>(new GeneralStatusResponse("OK"), HttpStatus.OK);
    }

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
            description = "Add/Edit Airport",
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
    public ResponseEntity<? extends Object> putAirport(@RequestBody AirportRequest airportRequest) {
        Airport airport;
        Optional<Airport> optionalAirport = airportRepository.findByIcao(airportRequest.getIcao());
        if (optionalAirport.isEmpty()) {
            airport = new Airport();
            airport.setIcao(airportRequest.getIcao());
        } else {
            airport = optionalAirport.get();
        }

        if (airportRequest.getNewIcao() != null) {
            airport.setIcao(airportRequest.getNewIcao());
        }
        airport.setName(airportRequest.getName());
        airport.setLat(airportRequest.getLat());
        airport.setLon(airportRequest.getLon());

        airportRepository.save(airport);

        return new ResponseEntity<>(new GeneralStatusResponse("Created"), HttpStatus.CREATED);
    }
}
