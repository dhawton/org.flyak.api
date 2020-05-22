package org.nzvirtual.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.nzvirtual.api.data.entity.File;
import org.nzvirtual.api.data.repository.FileRepository;
import org.nzvirtual.api.exception.GeneralException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageServiceImpl implements StorageService {
    @Value("${aws.s3.bucket}")
    private String bucket;
    AmazonS3 s3client;
    private FileRepository fileRepository;
    private Logger log = LoggerFactory.getLogger(StorageServiceImpl.class);

    public StorageServiceImpl(AmazonS3 s3client, FileRepository fileRepository) {
        this.s3client = s3client;
        this.fileRepository = fileRepository;
    }

    private String[] getFileAndPath(String key) {
        String[] ret = {"", ""};

        Path p = Paths.get(key);
        Path parent = p.getParent();
        if (parent != null) ret[1] = parent.toString();

        ret[0] = p.getFileName().toString();

        return ret;
    }

    @Transactional
    public void rename(String oldkey, String newkey) {
        File file = fileRepository.findByKey(oldkey);
        if (file == null) throw new GeneralException("Not Found", HttpStatus.NOT_FOUND);

        s3client.copyObject(bucket, oldkey, bucket, newkey);
        s3client.deleteObject(bucket, oldkey);
        String[] filedata = getFileAndPath(newkey);

        file.setName(filedata[0]);
        file.setPath(filedata[1]);
        fileRepository.save(file);
    }

    @Override
    @Transactional
    public boolean upload(String key, MultipartFile multipartFile) {
        File file = fileRepository.findByKey(key);
        boolean isNew = false;

        if (file == null) {
            file = new File();
            isNew = true;
        }

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getInputStream().available());
            if (multipartFile.getContentType() != null && !"".equals(multipartFile.getContentType())) {
                metadata.setContentType(multipartFile.getContentType());
            }
            s3client.putObject(
                    new PutObjectRequest(bucket, key, multipartFile.getInputStream(), metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            String[] filedata = getFileAndPath(key);

            file.setName(filedata[0]);
            file.setPath(filedata[1]);
            fileRepository.save(file);
            return true;
        } catch (IOException e) {
            if (file != null && isNew) {
                fileRepository.delete(file);
            }
            log.error("Error putting S3 Object, " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional
    public void delete(String key) {
        File file = fileRepository.findByKey(key);
        if (file == null) throw new GeneralException("Not Found", HttpStatus.NOT_FOUND);

        s3client.deleteObject(new DeleteObjectRequest(bucket, key));
        fileRepository.delete(file);
    }
}
