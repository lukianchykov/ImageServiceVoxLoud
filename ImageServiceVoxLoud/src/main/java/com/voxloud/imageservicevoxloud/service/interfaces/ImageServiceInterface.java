package com.voxloud.imageservicevoxloud.service.interfaces;

import com.voxloud.imageservicevoxloud.entity.Image;

import java.util.List;
import java.util.Optional;

public interface ImageServiceInterface {
    void saveImage(Image image);
    Optional<Image> findImageById(Long id);
    List<Image> findImageByFilter(String filter);
    List<Image> getAllImages();
}
