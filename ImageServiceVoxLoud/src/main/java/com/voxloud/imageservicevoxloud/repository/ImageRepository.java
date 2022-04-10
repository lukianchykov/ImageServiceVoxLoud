package com.voxloud.imageservicevoxloud.repository;

import com.voxloud.imageservicevoxloud.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findById(Long id);
    @Query(value = "SELECT i FROM Image i WHERE CONCAT(i.name, ' ', i.type, ' ', i.tag) LIKE %?1%")
    List<Image> searchByFilter(String filter);
    List<Image> findAll();
}
