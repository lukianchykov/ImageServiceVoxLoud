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

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ImageService implements ImageServiceInterface {

    private final ImageRepository imageRepository;
    private final AccountService accountService;

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
    public List<Image> findImageByTag(String tag) {
        log.info("Fetching image {}", tag);
        List<Image> findImages = imageRepository.findByTag(tag);
        if(findImages != null && findImages.size() != 0) {
            return findImages;
        }else {
            throw new CustomEmptyDataException("unable to find images with such tag");
        }
    }

    @Override
    public List<Image> findImageByName(String name) {
        log.info("Fetching image {}", name);
        List<Image> findImages = imageRepository.findByName(name);
        if(findImages != null && findImages.size() != 0) {
            return findImages;
        }else {
            throw new CustomEmptyDataException("unable to find images with such name");
        }
    }

    @Override
    public List<Image> findImageByType(String type) {
        log.info("Fetching image {}", type);
        List<Image> findImages = imageRepository.findByType(type);
        if(findImages != null && findImages.size() != 0) {
            return findImages;
        }else {
            throw new CustomEmptyDataException("unable to find images with such type");
        }
    }

    @Override
    public List<Image> findImageByAccount(String accountName) {
        log.info("Fetching image {}", accountName);
        Account findAccount = accountService.findByName(accountName);
        if(findAccount != null) {
            List<Image> findImages = imageRepository.findByAccount(findAccount);
            if(findImages != null && findImages.size() != 0) {
                return findImages;
            } else {
                throw new CustomEmptyDataException("unable to find images with such account");
            }
        }else{
            throw new CustomEmptyDataException("unable to find account with such accountName");
        }
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
