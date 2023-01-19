package com.web.app.controllers;

import com.web.app.classes.Params;
import com.web.app.utils.S3Util;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    @GetMapping("/files/delete/{fileKey:.+}")
    public String deleteFile(@PathVariable String fileKey, Model model, RedirectAttributes redirectAttributes) {
        S3Util.deleteFile(fileKey);

        return "redirect:/files";
    }

    @ModelAttribute("params")
    public Params params(){
        return new Params();
    }
    @PostMapping("/files/modify")
    public String renameFile(@ModelAttribute("modify") Params params){

        S3Util.modifyFile(params.getKey(), params().getImage());
        return "files";
    }
    @GetMapping("/error")
    public String error(){
        return "/error";
    }
}
