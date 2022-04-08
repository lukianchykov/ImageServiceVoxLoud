package com.voxloud.imageservicevoxloud.repository;

import com.voxloud.imageservicevoxloud.entity.Account;
import com.voxloud.imageservicevoxloud.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByTag(String tag);
    List<Image> findByName(String name);
    List<Image> findByType(String type);
    List<Image> findByAccount(Account account);
    List<Image> findAll();
}
