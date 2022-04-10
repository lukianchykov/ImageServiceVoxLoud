package com.voxloud.imageservicevoxloud.service;

import com.voxloud.imageservicevoxloud.entity.Account;
import com.voxloud.imageservicevoxloud.entity.Image;
import com.voxloud.imageservicevoxloud.exception.CustomEmptyDataException;
import com.voxloud.imageservicevoxloud.repository.ImageRepository;
import com.voxloud.imageservicevoxloud.service.interfaces.ImageServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ImageService implements ImageServiceInterface {

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

    @Override
    public List<Image> findImageByFilter(String filter) {
        log.info("Fetching all images {} ", filter);
        return imageRepository.searchByFilter(filter);
    }
    @Override
    public Optional<Image> findImageById(Long id) {
        return imageRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Image> getAllImages() {
        log.info("Fetching all images");
        return imageRepository.findAll();
    }
}
