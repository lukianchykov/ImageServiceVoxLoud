package com.voxloud.imageservicevoxloud.service;

import com.voxloud.imageservicevoxloud.entity.Account;
import com.voxloud.imageservicevoxloud.entity.Image;
import com.voxloud.imageservicevoxloud.exception.CustomEmptyDataException;
import com.voxloud.imageservicevoxloud.repository.AccountRepository;
import com.voxloud.imageservicevoxloud.repository.ImageRepository;
import com.voxloud.imageservicevoxloud.service.interfaces.ImageServiceInterface;
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
public class ImageService implements ImageServiceInterface {

    private final ImageRepository imageRepository;
    private final AccountService accountService;

    public Image saveImage(Image image) {
        image.setAccount(image.getAccount());
        image.setName(image.getName());
        image.setType(image.getType());
        image.setTag(image.getTag());
        image.setImage(image.getImage());
        image.setCreateDate(image.getCreateDate());
        image.setUpdateDate(image.getUpdateDate());
        return imageRepository.save(image);
    }

    @Override
    public List<Image> findImageByFilter(String filter) {
        log.info("Fetching all images {} ", filter);
        return imageRepository.searchByFilter(filter);
    }

    @Override
    public List<Image> getAllImagesByAccountId(Long accountId) {
        log.info("Fetching all images {} ", accountId);
        return imageRepository.findByAccountId(accountId);
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

    @Override
    public Image updateImage(Image source, Long id) {
        Optional<Image> imageForUpdate = imageRepository.findById(id);
        if (imageForUpdate.isPresent()) {
            imageForUpdate.get().setName(source.getName());
            imageForUpdate.get().setTag(source.getTag());
            imageForUpdate.get().setType(source.getType());
            imageForUpdate.get().setUpdateDate(source.getUpdateDate());
            imageForUpdate.get().setCreateDate(source.getCreateDate());
            imageForUpdate.get().setImage(source.getImage());
            imageForUpdate.get().setAccount(source.getAccount());

            imageRepository.save(imageForUpdate.get());
            return imageForUpdate.get();
        } else {
            throw new CustomEmptyDataException("unable to update image");
        }
    }

    @Override
    public String deleteImage(Long id) {
        Optional<Image> imageForDelete = imageRepository.findById(id);

        if (imageForDelete.isPresent()) {
            imageRepository.delete(imageForDelete.get());
            return "Image with id:" + id + " was successfully removed";
        } else {
            throw new CustomEmptyDataException("unable to delete image");
        }
    }
}
