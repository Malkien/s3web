package com.web.app.controllers;

import com.web.app.utils.S3Util;
import com.web.app.hadlers.ResponseMessage;
import org.springframework.boot.logging.LogFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;


@Controller
public class MainController {
    @GetMapping("/")
    public String showIndex(){
        return "redirect:/files";
    }
    @GetMapping("files/new")
    public String newFile(Model model){
        return "upload_form";
    }
    @PostMapping("/files/upload")
    public String uploadFile(Model model, @RequestParam("file") MultipartFile image){
        String publicURL;
        try{
            publicURL = S3Util.uploadFile(image.getName(), image);
            model.addAttribute("message", "Upload sucessful: "+publicURL);
        } catch (Exception e) {

            model.addAttribute("message","Upload unsuccesfull\n"+e.getMessage());
        }
        return "upload_form";
    }

    @GetMapping("/files")
    public String getListFiles(Model model) {
        List<S3Object> s3Objects = S3Util.listFile();
        model.addAttribute("files", s3Objects);
        return "files";
    }
}
