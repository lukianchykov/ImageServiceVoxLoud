package com.voxloud.imageservicevoxloud;


import com.voxloud.imageservicevoxloud.entity.Account;
import com.voxloud.imageservicevoxloud.entity.Role;
import com.voxloud.imageservicevoxloud.exception.CustomEmptyDataException;
import com.voxloud.imageservicevoxloud.service.AccountService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@WithMockUser(username = "user", roles = "USER")
@TestPropertySource(locations = "classpath:/application.properties")
@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountServiceTest {

    private final String USERNAME = "user";
    private final String EMAIL = "email";
    private final String PASSWORD = "123";

    @Autowired
    private AccountService accountService;

    @After
    public void cleanDB() {
        accountService.getList().forEach(account -> accountService.deleteAccount(account.getId()));
    }

    @Test
    public void createAccountTest() {
        Account newAccount = new Account();
        newAccount.setUsername(USERNAME);
        newAccount.setEmail(EMAIL);
        newAccount.setPassword(PASSWORD);
        newAccount.setCreateDate(new Date());
        newAccount.setUpdateDate(new Date());
        newAccount.setImages(new HashSet<>());
        newAccount.setRoles(new HashSet<>(List.of(Role.USER)));

        Account actual = accountService.saveAccount(newAccount);

        assertEquals(USERNAME, actual.getUsername());
        assertEquals(EMAIL, actual.getEmail());
        assertEquals(PASSWORD, actual.getPassword());
        assertNotNull(actual.getId());
    }

    @Test(expected = CustomEmptyDataException.class)
    public void testUniqueAccountEmail() {
        Account newAccount = new Account();
        
        newAccount.setUsername(USERNAME);
        newAccount.setEmail(EMAIL);
        newAccount.setPassword(PASSWORD);
        newAccount.setCreateDate(new Date());
        newAccount.setUpdateDate(new Date());
        newAccount.setImages(new HashSet<>());
        newAccount.setRoles(new HashSet<>(List.of(Role.USER)));
        
        accountService.saveAccount(newAccount);
        
        Account newAccount2 = new Account();

        newAccount2.setUsername(USERNAME);
        newAccount2.setEmail(EMAIL);
        newAccount2.setPassword(PASSWORD);
        newAccount2.setCreateDate(new Date());
        newAccount2.setUpdateDate(new Date());
        newAccount2.setImages(new HashSet<>());
        newAccount2.setRoles(new HashSet<>(List.of(Role.USER)));
        
        accountService.saveAccount(newAccount2);
    }

    @Test
    public void getAccountTest() {
        Account newAccount = new Account();

        newAccount.setUsername(USERNAME);
        newAccount.setEmail(EMAIL);
        newAccount.setPassword(PASSWORD);
        newAccount.setCreateDate(new Date());
        newAccount.setUpdateDate(new Date());
        newAccount.setImages(new HashSet<>());
        newAccount.setRoles(new HashSet<>(List.of(Role.USER)));

        Account actual = accountService.saveAccount(newAccount);
        Account current = accountService.findAccountById(actual.getId());

        assertEquals(actual.getId(), current.getId());
        assertEquals(EMAIL, current.getEmail());
        assertEquals(PASSWORD, current.getPassword());
        assertEquals(USERNAME, current.getUsername());

    }

    @Test
    public void findAccountByEmailTest() {
        Account newAccount = new Account();

        newAccount.setUsername(USERNAME);
        newAccount.setEmail(EMAIL);
        newAccount.setPassword(PASSWORD);
        newAccount.setCreateDate(new Date());
        newAccount.setUpdateDate(new Date());
        newAccount.setImages(new HashSet<>());
        newAccount.setRoles(new HashSet<>(List.of(Role.USER)));

        Account actual = accountService.saveAccount(newAccount);
        Account current = accountService.findAccountByEmail(EMAIL);

        assertEquals(actual.getId(), current.getId());
        assertEquals(actual.getEmail(), current.getEmail());
        assertEquals(actual.getPassword(), current.getPassword());
        assertEquals(actual.getUsername(), current.getUsername());

    }

    @Test
    public void findAccountByNameTest() {
        Account newAccount = new Account();

        newAccount.setUsername(USERNAME);
        newAccount.setEmail(EMAIL);
        newAccount.setPassword(PASSWORD);
        newAccount.setCreateDate(new Date());
        newAccount.setUpdateDate(new Date());
        newAccount.setImages(new HashSet<>());
        newAccount.setRoles(new HashSet<>(List.of(Role.USER)));

        Account actual = accountService.saveAccount(newAccount);
        Account current = accountService.findAccountByName(USERNAME);

        assertEquals(actual.getId(), current.getId());
        assertEquals(actual.getEmail(), current.getEmail());
        assertEquals(actual.getPassword(), current.getPassword());
        assertEquals(actual.getUsername(), current.getUsername());

    }

    @Test
    public void updateAccountTest() {
        Account newAccount = new Account();

        newAccount.setUsername(USERNAME);
        newAccount.setEmail(EMAIL);
        newAccount.setPassword(PASSWORD);
        newAccount.setCreateDate(new Date());
        newAccount.setUpdateDate(new Date());
        newAccount.setImages(new HashSet<>());
        newAccount.setRoles(new HashSet<>(List.of(Role.USER)));

        Account actual = accountService.saveAccount(newAccount);

        Account accountForUpdate = new Account();
        accountForUpdate.setEmail("user123@mail.ru");
        accountForUpdate.setPassword("111");
        accountForUpdate.setUsername("User");

        Account updatedUser = accountService.updateAccount(accountForUpdate, actual.getId());

        assertEquals(actual.getId(), updatedUser.getId());
        assertEquals("user123@mail.ru", updatedUser.getEmail());
        assertEquals("111", updatedUser.getPassword());
        assertEquals("User", updatedUser.getUsername());

    }

    @Test
    public void deleteAccountTest() {
        Account newAccount = new Account();

        newAccount.setUsername(USERNAME);
        newAccount.setEmail(EMAIL);
        newAccount.setPassword(PASSWORD);
        newAccount.setCreateDate(new Date());
        newAccount.setUpdateDate(new Date());
        newAccount.setImages(new HashSet<>());
        newAccount.setRoles(new HashSet<>(List.of(Role.USER)));

        Account actual = accountService.saveAccount(newAccount);

        List<Account> userListAfterCreate = accountService.getList();
        assertEquals(1, userListAfterCreate.size());

        accountService.deleteAccount(actual.getId());

        List<Account> userListAfterDelete = accountService.getList();
        assertEquals(0, userListAfterDelete.size());
    }
}
