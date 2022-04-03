package com.voxloud.imageservicevoxloud.service.interfaces;

import com.voxloud.imageservicevoxloud.entity.Account;
import com.voxloud.imageservicevoxloud.entity.dto.UserRegistrationDto;

import java.util.List;

public interface AccountInterface {
    void saveAccount(Account account);
    Account save(UserRegistrationDto registration);
    Account findByEmail(String email);
    Account findAccountById(Long id);
    List<Account> getList();
    Account updateAccount(Account source, Long id);
    String deleteAccount(Long id);
}
