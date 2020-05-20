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
import org.nzvirtual.api.dto.GeneralStatusResponse;
import org.nzvirtual.api.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/cdn")
public class CDNController {
    private FileRepository fileRepository;
    @Value("${aws.s3.endpoint}")
    private String cdnBase;
    private StorageService storageService;

    public CDNController(FileRepository fileRepository, StorageService storageService) {
        this.fileRepository = fileRepository;
        this.storageService = storageService;
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

    @PostMapping("{key}")
    @CacheEvict(value="files")
    public ResponseEntity<GeneralStatusResponse> postFile(@PathVariable(required = true) String key, @RequestParam(value="file", required=true)MultipartFile multipartFile) throws IOException {
        storageService.upload(key, multipartFile);

        return new ResponseEntity<>(new GeneralStatusResponse("OK"), HttpStatus.CREATED);
    }

    @DeleteMapping("{key}")
    @CacheEvict(value="files")
    public ResponseEntity<GeneralStatusResponse> deleteFile(@PathVariable(required = true) String key) throws Exception {
        storageService.delete(key);

        return new ResponseEntity<>(new GeneralStatusResponse("OK"), HttpStatus.ACCEPTED);
    }
}
