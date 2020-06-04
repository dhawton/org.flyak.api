package org.nzvirtual.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.nzvirtual.api.data.entity.Booking;
import org.nzvirtual.api.data.entity.Route;
import org.nzvirtual.api.data.entity.User;
import org.nzvirtual.api.data.repository.BookingRepository;
import org.nzvirtual.api.data.repository.RouteRepository;
import org.nzvirtual.api.data.repository.UserRepository;
import org.nzvirtual.api.dto.BookingPersist;
import org.nzvirtual.api.dto.BookingRequest;
import org.nzvirtual.api.dto.GeneralStatusResponse;
import org.nzvirtual.api.exception.GeneralException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/booking")
public class BookingController {
    private Logger log = LoggerFactory.getLogger(BookingController.class);
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private RouteRepository routeRepository;

    public BookingController(BookingRepository bookingRepository, UserRepository userRepository, RouteRepository routeRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.routeRepository = routeRepository;
    }

    @GetMapping("")
    @Operation(description = "Get user bookings", security = { @SecurityRequirement(name = "bearerAuth") }, responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Booking.class)
                            )
                    )
            )
    })
    public Iterable<Booking> getBookings(Principal principal) {
        Optional<User> optionalUser = userRepository.findByEmail(principal.getName());
        return bookingRepository.findByUser(optionalUser.get());
    }

    @DeleteMapping("/{bookingId}")
    @Operation(
            description = "Delete booking",
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
    public ResponseEntity<GeneralStatusResponse> deleteBooking(@PathVariable Long bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty())
            throw new GeneralException("Not Found", HttpStatus.NOT_FOUND);

        bookingRepository.delete(optionalBooking.get());
        return new ResponseEntity<>(new GeneralStatusResponse("OK"), HttpStatus.OK);
    }

    @RequestMapping(
            value = {
                ""
            },
            method = {
                    RequestMethod.POST
            }
    )
    @Operation(
            description = "Add Booking",
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
    public ResponseEntity<? extends Object> postBooking(@RequestBody BookingRequest bookingRequest, Principal principal) {
        Booking booking = new Booking();
        Optional<User> optUser = userRepository.findByEmail(principal.getName());
        Optional<Route> optRoute = routeRepository.findById(bookingRequest.getRouteId());
        if (optRoute.isEmpty())
            throw new GeneralException("Not Found", HttpStatus.NOT_FOUND);

        BookingPersist bookingPersist = new BookingPersist(optUser.get(), optRoute.get(), bookingRequest.getPlannedTime());
        booking.setUser(bookingPersist.getUser());
        booking.setAirline(bookingPersist.getAirline().getIcao());
        booking.setFlightNumber(bookingPersist.getFlight_number());
        booking.setDeparture(bookingPersist.getDeparture());
        booking.setArrival(bookingPersist.getArrival());
        booking.setEquipment(bookingPersist.getEquipment());
        booking.setDepartureTime(bookingPersist.getDepartureTime());
        booking.setArrivalTime(bookingPersist.getArrivalTime());
        booking.setDuration(bookingPersist.getDuration());
        booking.setAtcident(bookingPersist.getAtcident());
        booking.setPlannedDeparture(bookingPersist.getPlannedTime());
        bookingRepository.save(booking);

        return new ResponseEntity<>(new GeneralStatusResponse("Created"), HttpStatus.CREATED);
    }
}
