package org.nzvirtual.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.nzvirtual.api.data.entity.File;
import org.nzvirtual.api.data.entity.Route;
import org.nzvirtual.api.data.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cdn")
public class CDNController {
    private FileRepository fileRepository;
    @Value("${do.spaces.endpoint}")
    private String cdnBase;

    public CDNController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @GetMapping("")
    @Cacheable(value="files")
    @Operation(tags = { "cdn" }, description = "Get list of files in cdn", security = { @SecurityRequirement(name = "bearerAuth") }, responses = {
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
    public Iterable<File> getFiles() {
        return this.fileRepository.findAll();
    }

    @PostMapping("/{key}")
    @CacheEvict(value="files")
    public void postFile() {

    }

    @DeleteMapping("/{key}")
    @CacheEvict(value="files")
    public void deleteFile() {

    }
}
