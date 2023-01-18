package com.web.app.controllers;

import com.web.app.utils.S3Util;
import com.web.app.hadlers.ResponseMessage;
import org.springframework.boot.logging.LogFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
            publicURL = S3Util.uploadFile(image.getOriginalFilename(), image);
            model.addAttribute("message", "Upload sucessful: "+publicURL);
        } catch (RuntimeException e) {
            model.addAttribute("message","Upload unsuccesfull\n"+e.getMessage());
        } catch (Exception ex){

            model.addAttribute("message","Upload unsuccesfull\n"+ex.getMessage());
        }
        return "upload_form";
    }

    @GetMapping("/files")
    public String getListFiles(Model model) {
        List<S3Object> s3Objects = S3Util.listFile();
        model.addAttribute("files", s3Objects);
        return "files";
    }
    @GetMapping("/files/delete")
    public String deleteFile(@PathVariable S3Object file, Model model, RedirectAttributes redirectAttributes) {
        try {
            boolean existed = S3Util.deleteFile(file);

            if (existed) {
                redirectAttributes.addFlashAttribute("message", "Delete the file successfully");
            } else {
                redirectAttributes.addFlashAttribute("message", "The file does not exist!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message",
                    "Could not delete the file: " + file.key() + ". Error: " + e.getMessage());
        }

        return "redirect:/files";
    }
    /*
    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileKey) {
        Resource file = storageService.load(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }*/
}
