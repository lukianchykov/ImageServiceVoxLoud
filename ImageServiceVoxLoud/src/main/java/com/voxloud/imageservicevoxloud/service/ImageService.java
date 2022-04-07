package com.voxloud.imageservicevoxloud.service;

import com.voxloud.imageservicevoxloud.entity.Image;
import com.voxloud.imageservicevoxloud.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public void saveImage(Image image) {
        image.setAccount(image.getAccount());
        image.setName(image.getName());
        image.setType(image.getType());
        image.setTag(image.getTag());
        image.setImage(image.getImage());
        image.setCreateDate(image.getCreateDate());
        image.setUpdateDate(image.getUpdateDate());
        imageRepository.save(image);
    }

    public List<Image> getAllImages() {
        log.info("Fetching all images");
        return imageRepository.findAll();
    }

    public Optional<Image> getImageById(Long id) {
        return imageRepository.findById(id);
    }

    public List<Image> findImageByTag(String tag) {
        return imageRepository.findByTag(tag);
    }
}
