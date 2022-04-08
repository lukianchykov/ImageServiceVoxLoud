package com.voxloud.imageservicevoxloud.service.interfaces;

import com.voxloud.imageservicevoxloud.entity.Image;

import java.util.List;

public interface ImageServiceInterface {
    void saveImage(Image image);
    List<Image> findImageByTag(String tag);
    List<Image> findImageByName(String name);
    List<Image> findImageByType(String type);
    List<Image> findImageByAccount(String accountName);
    List<Image> getAllImages();
}
