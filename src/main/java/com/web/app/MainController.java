package com.web.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;

@Controller
public class MainController {
    @GetMapping("")
    public String showIndex(){
        return "index";
    }
    @PostMapping("/create")
    public String create(String name, @RequestParam("image") File image){

        if(name == null){
            S3Util.uploadFile(image.getName(), image);
        }else{
            S3Util.uploadFile(name, image);
        }
        return "index";
    }
}
