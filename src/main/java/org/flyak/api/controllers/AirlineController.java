package org.flyak.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.flyak.api.data.entity.Airline;
import org.flyak.api.data.repository.AirlineRepository;
import org.flyak.api.dto.AirlineRequest;
import org.flyak.api.dto.GeneralStatusResponse;
import org.flyak.api.exception.GeneralException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/airline")
public class AirlineController {
    private Logger log = LoggerFactory.getLogger(AirlineController.class);
    private AirlineRepository airlineRepository;

    public AirlineController(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    @GetMapping("/all")
    @Operation(description = "Get all airlines", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Airline.class)
                            )
                    )
            )
    })
    public Iterable<Airline> getAirlines() {
        return this.airlineRepository.findAll();
    }

    @DeleteMapping("/{icao}")
    @Operation(
            description = "Delete airline",
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
    public ResponseEntity<GeneralStatusResponse> deleteAirline(@PathVariable String icao) {
        Optional<Airline> optionalAirline = airlineRepository.findByIcao(icao);
        if (optionalAirline.isEmpty())
            throw new GeneralException("Not Found", HttpStatus.NOT_FOUND);

        airlineRepository.delete(optionalAirline.get());
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
            description = "Add/Edit Airline",
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
    public ResponseEntity<? extends Object> putAirline(@RequestBody AirlineRequest airlineRequest) {
        Airline airline;
        Optional<Airline> optionalAirline = airlineRepository.findByIcao(airlineRequest.getIcao());
        if (optionalAirline.isEmpty()) {
            airline = new Airline();
            airline.setIcao(airlineRequest.getIcao());
        } else {
            airline = optionalAirline.get();
        }

        if (airlineRequest.getNewIcao() != null) {
            airline.setIcao(airlineRequest.getNewIcao());
        }
        airline.setName(airlineRequest.getName());

        airlineRepository.save(airline);

        return new ResponseEntity<>(new GeneralStatusResponse("Created"), HttpStatus.CREATED);
    }
}
