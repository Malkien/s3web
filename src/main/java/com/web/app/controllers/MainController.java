package com.web.app.controllers;

import com.web.app.utils.S3Util;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;

/**
 * Main controller
 */
@Controller
public class MainController {
    /**
     * Redirect to files
     * @return
     */
    @GetMapping("/")
    public String showIndex(){
        return "redirect:/files";
    }

    /**
     * Load uploadform
     * @param model the model
     * @return the html
     */
    @GetMapping("files/new")
    public String newFile(Model model){
        return "upload_form";
    }

    /**
     * Load the form action and redirect to the same html
     * @param model the model
     * @param image the image to upload
     * @return the html
     */
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

    /**
     *  Load files
     * @param model the model
     * @return the html
     */
    @GetMapping("/files")
    public String getListFiles(Model model) {
        List<S3Object> s3Objects = S3Util.listFile();
        model.addAttribute("files", s3Objects);
        return "files";
    }

    /**
     * Load the action delete
     * @param fileKey the key to delete
     * @return redirect to files
     */
    @GetMapping("/files/delete/{fileKey:.+}")
    public String deleteFile(@PathVariable String fileKey) {
        S3Util.deleteFile(fileKey);

        return "redirect:/files";
    }

    /**
     * Load the action form to modify a image
     * @param file the file
     * @param key the key
     * @return redirect to files
     */
    @PostMapping("/files")
    public String newFile( @RequestParam("file") MultipartFile file, @RequestParam("key") String key){
        S3Util.modifyFile(key, file);
        return "redirect:/files";
    }


}
