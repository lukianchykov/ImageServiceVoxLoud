package com.voxloud.imageservicevoxloud.controller;

import com.voxloud.imageservicevoxloud.entity.Account;
import com.voxloud.imageservicevoxloud.entity.Image;
import com.voxloud.imageservicevoxloud.exception.CustomEmptyDataException;
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
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
@ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 400, message = "This is a bad request, please follow the API documentation for the proper request format"),
        @io.swagger.annotations.ApiResponse(code = 401, message = "Due to security constraints, your access request cannot be authorized"),
        @io.swagger.annotations.ApiResponse(code = 500, message = "The server is down. Please bear with us."),
})
public class ImageController {
    private final ImageService imageService;
    private final AccountService accountService;

    @Secured("USER")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/upload")
    public String upload() {
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
        Account account = accountService.findAccountByName(principal.getName());

        Image image = new Image();

        image.setAccount(account);
        image.setName(name);
        image.setImage(ImageUtility.compressImage(file.getBytes()));
        image.setType(file.getContentType());
        image.setTag(tag);
        image.setCreateDate(new Date());
        image.setUpdateDate(new Date());
        imageService.saveImage(image);

        return "redirect:/account/images";
    }

    @GetMapping("/image/display/{id}")
    @ResponseBody
    void showUniqueImageUtils(@PathVariable("id") Long id, HttpServletResponse response)
            throws IOException {
        log.info("Id: " + id);
        Optional<Image> image = imageService.findImageById(id);
        if (image.isPresent()) {
            response.setContentType(String.valueOf(MediaType.valueOf(image.get().getType())));
            response.getOutputStream().write(ImageUtility.decompressImage(image.get().getImage()));
            response.getOutputStream().close();
        } else {
            throw new CustomEmptyDataException("Unable to find image");
        }
    }

    @Secured("USER")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/account/images")
    public String search(@Param("filter")String filter, Model model, Principal principal) {
        List<Image> images;

        if (filter != null && !filter.isEmpty()) {
            images = imageService.findImageByFilter(filter);
        }else {
            Account account = accountService.findAccountByName(principal.getName());
            images = imageService.getAllImagesByAccountId(account.getId());
        }

        model.addAttribute("list", images);
        model.addAttribute("filter", filter);

        return "account-images";
    }

    @RequestMapping("uploadError")
    public ModelAndView onUploadError() {
        ModelAndView errorView = new ModelAndView("upload");
        errorView.addObject("error", "File is too large! File must be less than 2 MB");
        return errorView;
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
        return (factory) -> factory.addErrorPages(new ErrorPage(MaxUploadSizeExceededException.class, "/uploadError"));
    }


}
