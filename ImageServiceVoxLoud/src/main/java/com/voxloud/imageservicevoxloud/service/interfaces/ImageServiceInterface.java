package com.voxloud.imageservicevoxloud.service.interfaces;

import com.voxloud.imageservicevoxloud.entity.Account;
import com.voxloud.imageservicevoxloud.entity.Image;

import java.util.List;
import java.util.Optional;

public interface ImageServiceInterface {
    Image saveImage(Image image);
    Optional<Image> findImageById(Long id);
    List<Image> findImageByFilter(String filter);
    List<Image> getAllImages();
    Image updateImage(Image source, Long id);
    String deleteImage(Long id);
}
