package com.voxloud.imageservicevoxloud;

import com.voxloud.imageservicevoxloud.entity.Account;
import com.voxloud.imageservicevoxloud.entity.Image;
import com.voxloud.imageservicevoxloud.entity.Role;
import com.voxloud.imageservicevoxloud.service.AccountService;
import com.voxloud.imageservicevoxloud.service.ImageService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@WithMockUser(username = "user", roles = "USER")
@TestPropertySource(locations = "classpath:/application.properties")
@SpringBootTest
@RunWith(SpringRunner.class)
public class ImageServiceTest {

    private final String NAME = "burger";
    private final String TAG = "bur";
    private final String TYPE = "type";
    private final byte[] IMAGE = new byte[0];

    @Autowired
    private ImageService imageService;
    @Autowired
    private AccountService accountService;

    @After
    public void cleanDB() {
        imageService.getAllImages().forEach(image -> imageService.deleteImage(image.getId()));
    }

    @Test
    public void createImageTest(){
        Account actualAccount = accountService.saveAccount(new Account(null, "one", "1", "one1@mail.ru"
                , new Date(), new Date(), new HashSet<>(), new HashSet<>(List.of(Role.USER))));

        Image newImage = new Image();

        newImage.setName("first");
        newImage.setTag(TAG);
        newImage.setType(TYPE);
        newImage.setAccount(actualAccount);
        newImage.setCreateDate(new Date());
        newImage.setUpdateDate(new Date());
        newImage.setImage(IMAGE);

        Image actual = imageService.saveImage(newImage);

        assertEquals("first", actual.getName());
        assertEquals(TAG, actual.getTag());
        assertEquals(TYPE, actual.getType());
        assertNotNull(actual.getId());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testUniqueImage() {
        Account actualAccount = accountService.saveAccount(new Account(null, "two", "2", "two2@mail.ru"
                , new Date(), new Date(), new HashSet<>(), new HashSet<>(List.of(Role.USER))));

        Image newImage = new Image();

        newImage.setName(NAME);
        newImage.setTag(TAG);
        newImage.setType(TYPE);
        newImage.setAccount(actualAccount);
        newImage.setCreateDate(new Date());
        newImage.setUpdateDate(new Date());
        newImage.setImage(IMAGE);

        imageService.saveImage(newImage);

        Account actualAccount2 = accountService.saveAccount(new Account(null, "six", "6", "six6@mail.ru"
                , new Date(), new Date(), new HashSet<>(), new HashSet<>(List.of(Role.USER))));

        Image newImage2 = new Image();

        newImage.setName(NAME);
        newImage.setTag(TAG);
        newImage.setType(TYPE);
        newImage.setAccount(actualAccount2);
        newImage.setCreateDate(new Date());
        newImage.setUpdateDate(new Date());
        newImage.setImage(IMAGE);

        imageService.saveImage(newImage2);
    }

    @Test
    public void getImageTest() {
        Account actualAccount = accountService.saveAccount(new Account(null, "three", "3", "three3@mail.ru"
                , new Date(), new Date(), new HashSet<>(), new HashSet<>(List.of(Role.USER))));
        Image newImage = new Image();

        newImage.setName("wow");
        newImage.setTag(TAG);
        newImage.setType(TYPE);
        newImage.setAccount(actualAccount);
        newImage.setCreateDate(new Date());
        newImage.setUpdateDate(new Date());
        newImage.setImage(IMAGE);

        Image actual = imageService.saveImage(newImage);
        Optional<Image> current = imageService.findImageById(actual.getId());

        if(current.isPresent()) {
            assertEquals(actual.getId(), current.get().getId());
            assertEquals("wow", current.get().getName());
            assertEquals(TAG, current.get().getTag());
            assertEquals(TYPE, current.get().getType());
        }

    }

    @Test
    public void updateImageTest() {
        Account actualAccount = accountService.saveAccount(new Account(null, "four", "4", "four4@mail.ru"
                , new Date(), new Date(), new HashSet<>(), new HashSet<>(List.of(Role.USER))));
        Image newImage = new Image();

        newImage.setName("lucky");
        newImage.setTag(TAG);
        newImage.setType(TYPE);
        newImage.setAccount(actualAccount);
        newImage.setCreateDate(new Date());
        newImage.setUpdateDate(new Date());
        newImage.setImage(IMAGE);

        Image actual = imageService.saveImage(newImage);

        Account actualAccount2 = accountService.saveAccount(new Account(null, "seven", "7", "seven7@mail.ru"
                , new Date(), new Date(), new HashSet<>(), new HashSet<>(List.of(Role.USER))));

        Image imageForUpdate = new Image();
        imageForUpdate.setName("image1");
        imageForUpdate.setTag("blurb");
        imageForUpdate.setType("Unknown");
        imageForUpdate.setAccount(actualAccount2);
        imageForUpdate.setCreateDate(new Date());
        imageForUpdate.setUpdateDate(new Date());
        imageForUpdate.setImage(IMAGE);

        Image updatedImage = imageService.updateImage(imageForUpdate, actual.getId());

        assertEquals(actual.getId(), updatedImage.getId());
        assertEquals("image1", updatedImage.getName());
        assertEquals("blurb", updatedImage.getTag());
        assertEquals("Unknown", updatedImage.getType());

    }

    @Test
    public void deleteImageTest() {
        Account actualAccount = accountService.saveAccount(new Account(null, "five", "5", "five5@mail.ru"
                , new Date(), new Date(), new HashSet<>(), new HashSet<>(List.of(Role.USER))));
        Image newImage = new Image();

        newImage.setName("anime");
        newImage.setTag(TAG);
        newImage.setType(TYPE);
        newImage.setAccount(actualAccount);
        newImage.setCreateDate(new Date());
        newImage.setUpdateDate(new Date());
        newImage.setImage(IMAGE);

        Image actual = imageService.saveImage(newImage);

        List<Image> imageListAfterCreate = imageService.getAllImages();
        assertEquals(1, imageListAfterCreate.size());

        imageService.deleteImage(actual.getId());

        List<Image> imageListAfterDelete = imageService.getAllImages();
        assertEquals(0, imageListAfterDelete.size());
    }
}
