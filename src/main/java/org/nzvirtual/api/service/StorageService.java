package org.nzvirtual.api.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    boolean upload(String key, MultipartFile multipartFile);
    void delete(String key) throws Exception;
}
