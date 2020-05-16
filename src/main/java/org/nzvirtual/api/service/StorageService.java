package org.nzvirtual.api.service;

import org.nzvirtual.api.data.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    void save(MultipartFile multipartFile) throws IOException;
    void delete(String key) throws Exception;
}
