package com.voxloud.imageservicevoxloud.controller;

import com.voxloud.imageservicevoxloud.entity.Account;
import com.voxloud.imageservicevoxloud.entity.Image;
import com.voxloud.imageservicevoxloud.service.AccountService;
import com.voxloud.imageservicevoxloud.service.ImageService;
import com.voxloud.imageservicevoxloud.util.ImageUtility;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Controller
@ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 400, message = "This is a bad request, please follow the API documentation for the proper request format"),
        @io.swagger.annotations.ApiResponse(code = 401, message = "Due to security constraints, your access request cannot be authorized"),
        @io.swagger.annotations.ApiResponse(code = 500, message = "The server is down. Please bear with us."),
})
public class ImageController{
    private final ImageService imageService;
    private final AccountService accountService;

    @Secured("USER")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/account/images")
    public String searchByTag(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Image> images;

        if (filter != null && !filter.isEmpty()) {
            images = imageService.findImageByTag(filter);
        } else {
            images = imageService.getAllImages();
        }

        model.addAttribute("images", images);
        model.addAttribute("filter", filter);

        return "account-images";
    }

    @Secured("USER")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/upload")
    public String saveImage(){
        return "upload";
    }

    @Secured("USER")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/upload")
    public String saveImage(
            Principal principal,
            @RequestParam("name") String name,
            @RequestParam("tag") String tag,
            final @RequestParam("image") MultipartFile file
    ) throws IOException {
        Account account = accountService.findByName(principal.getName());

        Image image = new Image();

        image.setAccount(account);
        image.setName(name);
        image.setImage(ImageUtility.compressImage(file.getBytes()));
        image.setType(file.getContentType());
        image.setTag(tag);
        image.setCreateDate(new Date());
        image.setUpdateDate(new Date());
        imageService.saveImage(image);

        return "upload";
    }

    @RequestMapping("uploadError")
    public ModelAndView onUploadError() {
        ModelAndView errorView = new ModelAndView("upload");
        errorView.addObject("error","Uploaded File is too large!");
        return errorView;
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
        return (factory) -> factory.addErrorPages(new ErrorPage(MaxUploadSizeExceededException.class, "/uploadError"));
    }


}
