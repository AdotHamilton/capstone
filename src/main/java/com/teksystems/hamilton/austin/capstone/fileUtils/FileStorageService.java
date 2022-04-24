package com.teksystems.hamilton.austin.capstone.fileUtils;

import com.teksystems.hamilton.austin.capstone.configuration.FileStorageProperties;
import com.teksystems.hamilton.austin.capstone.exception.FileStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.util.StringUtils;
import java.nio.file.StandardCopyOption;
@Slf4j
@Service
public class FileStorageService {
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath();

        try {
            Files.createDirectories(this.fileStorageLocation); // create directory

        }catch(Exception ex) {
            throw new FileStorageException("Could not create the directory to upload");
        }
    }

    public String saveFile(MultipartFile multipartFile, String subDir) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        try {
            Path filePath = this.fileStorageLocation.resolve(subDir); // destination = basePath + subdir
            if(Files.notExists(filePath)){
                Files.createDirectories(filePath); // if directory doesn't exist, create it
            }
            Path targetPath = filePath.resolve(fileName); // directory + fileName
            Files.copy(multipartFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            // copy file byte[] to specified path, if this file exists, replace it
            return fileName;
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    public Resource loadFileAsResource(String filename, String subDir){
        try {
            Path filepath =this.fileStorageLocation.resolve(subDir).resolve(filename).normalize(); // subdir could be posts/{user_id} or users/{user_id}
            Resource resource = new UrlResource(filepath.toUri()); // get uri for image host
            if(resource.exists()){
                return resource;
            } else {
                throw new FileStorageException("File not found " + filename);
            }
        } catch(MalformedURLException e){
            throw new FileStorageException("File not found" + filename);
        }
    }

}
