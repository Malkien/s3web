package com.web.app.classes;

import org.springframework.web.multipart.MultipartFile;

public class Params {
    private String key;
    private MultipartFile image;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
