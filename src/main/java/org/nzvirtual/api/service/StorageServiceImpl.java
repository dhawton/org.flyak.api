package org.nzvirtual.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.nzvirtual.api.data.entity.File;
import org.nzvirtual.api.data.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class StorageServiceImpl implements StorageService {
    @Value("${do.spaces.bucket}")
    private String bucket;
    AmazonS3 s3client;
    private FileRepository fileRepository;
    private Logger log = LoggerFactory.getLogger(StorageServiceImpl.class);

    public StorageServiceImpl(AmazonS3 s3client, FileRepository fileRepository) {
        this.s3client = s3client;
        this.fileRepository = fileRepository;
    }

    @Override
    public void save(String path, MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String key = path + fileName;
        File file = new File();
        file.setName(fileName);
        file.setPath(path);
        fileRepository.save(file);
    }

    @Async
    @Override
    public void upload(String key, MultipartFile multipartFile, File file) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getInputStream().available());
            if (multipartFile.getContentType() != null && !"".equals(multipartFile.getContentType())) {
                metadata.setContentType(multipartFile.getContentType());
            }
            s3client.putObject(
                    new PutObjectRequest(bucket, key, multipartFile.getInputStream(), metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            fileRepository.delete(file);
            log.error("Error putting S3 Object, " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String key) {
        File file = fileRepository.findByKey(key);
        if (file == null) return;

        s3client.deleteObject(new DeleteObjectRequest(bucket, key));
        fileRepository.delete(file);
    }
}
