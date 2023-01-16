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
import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {
    @GetMapping("")
    public String showIndex(){
        return "index";
    }
    @PostMapping("/create")
    public ResponseEntity<String> create(String name, @RequestParam("file") MultipartFile image){
        String publicURL;
        if(name == null){
            publicURL = S3Util.uploadFile(image.getName(), image);
        }else{
            publicURL = S3Util.uploadFile(name, image);
        }
        Map<String, String> response = new HashMap<>();
        response.put("publicURL", publicURL);
        return new ResponseEntity<String>(HttpStatus.CREATED);
    }
}
