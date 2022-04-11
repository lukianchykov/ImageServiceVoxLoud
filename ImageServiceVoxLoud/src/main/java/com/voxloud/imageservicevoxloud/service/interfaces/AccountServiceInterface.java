package com.voxloud.imageservicevoxloud.service.interfaces;

import com.voxloud.imageservicevoxloud.entity.Account;
import com.voxloud.imageservicevoxloud.entity.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AccountServiceInterface extends UserDetailsService {
    Account saveAccount(Account account);
    void save(UserRegistrationDto registration);
    Account findAccountByEmail(String email);
    Account findAccountByName(String username);
    Account findAccountById(Long id);
    List<Account> getList();
    Account updateAccount(Account source, Long id);
    String deleteAccount(Long id);
}
