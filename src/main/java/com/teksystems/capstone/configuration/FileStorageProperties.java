package com.teksystems.capstone.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
// probably dont need this file, could just tell spring to send all multipart uploads to resources/static/uploads
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    private String uploadDir;
    public String getUploadDir(){
        return uploadDir;
    }
    public void setUploadDir(String uploadDir){
        this.uploadDir = uploadDir;
    }
}
