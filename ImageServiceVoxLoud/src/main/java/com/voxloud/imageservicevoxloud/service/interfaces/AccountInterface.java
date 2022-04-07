package com.voxloud.imageservicevoxloud.service.interfaces;

import com.voxloud.imageservicevoxloud.entity.Account;
import com.voxloud.imageservicevoxloud.entity.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AccountInterface extends UserDetailsService {
    void saveAccount(Account account);
    void save(UserRegistrationDto registration);
    Account findByEmail(String email);
    Account findByName(String username);
    Account findAccountById(Long id);
    List<Account> getList();
    Account updateAccount(Account source, Long id);
    String deleteAccount(Long id);
}
