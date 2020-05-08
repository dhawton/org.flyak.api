package org.nzvirtual.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.nzvirtual.api.data.entity.Equipment;
import org.nzvirtual.api.data.repository.EquipmentRepository;
import org.nzvirtual.api.dto.EquipmentRequest;
import org.nzvirtual.api.dto.GeneralStatusResponse;
import org.nzvirtual.api.exception.GeneralException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {
    private Logger log = LoggerFactory.getLogger(EquipmentController.class);
    private EquipmentRepository equipmentRepository;

    public EquipmentController(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @GetMapping("/all")
    @Operation(description = "Get all equipment", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Equipment.class)
                            )
                    )
            )
    })
    public Iterable<Equipment> getEquipment() {
        return this.equipmentRepository.findAll();
    }

    @DeleteMapping("/{icao}")
    @Operation(
            description = "Delete equipment",
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
    public ResponseEntity<GeneralStatusResponse> deleteEquipment(@PathVariable String icao) {
        Optional<Equipment> optionalEquipment = equipmentRepository.findByIcao(icao);
        if (optionalEquipment.isEmpty())
            throw new GeneralException("Not Found", HttpStatus.NOT_FOUND);

        equipmentRepository.delete(optionalEquipment.get());
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
            description = "Add/Edit Equipment",
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
    public ResponseEntity<? extends Object> putEquipment(@RequestBody EquipmentRequest equipmentRequest) {
        Equipment equipment;
        Optional<Equipment> optionalAirline = equipmentRepository.findByIcao(equipmentRequest.getIcao());
        if (optionalAirline.isEmpty()) {
            equipment = new Equipment();
            equipment.setIcao(equipmentRequest.getIcao());
        } else {
            equipment = optionalAirline.get();
        }

        if (equipmentRequest.getNewIcao() != null) {
            equipment.setIcao(equipmentRequest.getNewIcao());
        }
        equipment.setName(equipmentRequest.getName());

        equipmentRepository.save(equipment);

        return new ResponseEntity<>(new GeneralStatusResponse("Created"), HttpStatus.CREATED);
    }
}
