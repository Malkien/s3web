package com.web.app;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class MainController {
    @GetMapping("")
    public String showIndex(){
        return "index";
    }
    @PostMapping("/create")
    public ResponseEntity<String> create(String name, @RequestParam("image") MultipartFile image){
        if(name == null){
            S3Util.uploadFile(image.getName(), image);
        }else{
            S3Util.uploadFile(name, image);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
