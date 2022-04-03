package com.voxloud.imageservicevoxloud.service;


import com.voxloud.imageservicevoxloud.entity.Account;
import com.voxloud.imageservicevoxloud.entity.Role;
import com.voxloud.imageservicevoxloud.entity.dto.UserRegistrationDto;
import com.voxloud.imageservicevoxloud.exception.CustomEmptyDataException;
import com.voxloud.imageservicevoxloud.repository.AccountRepository;
import com.voxloud.imageservicevoxloud.service.interfaces.AccountInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService implements AccountInterface, UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            log.error("User not found in db");
            throw new UsernameNotFoundException("User not found in db");
        } else {
            log.info("User found in db: {}", username);
            return (UserDetails) account;
        }
    }

    @Override
    public void saveAccount(Account account) {
        log.info("Saving new user {} to db", account.getUsername());
        Account checkIfExists = accountRepository.findByUsername(account.getUsername());
        if(checkIfExists != null){
            throw new CustomEmptyDataException("User is already in db");
        }else {
            account.setUsername(account.getUsername());
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            account.setEmail(account.getEmail());
            account.setCreateDate(account.getCreateDate());
            account.setUpdateDate(account.getUpdateDate());
            account.setImages(account.getImages());
            if (account.getRoles() == null) {
                account.setRoles(new HashSet<>(List.of(Role.USER)));
            }
            accountRepository.save(account);
        }
    }

    public Account save(UserRegistrationDto registration){
        log.info("Saving new registered user {} to db", registration.getUsername());
        Account account = new Account();
        account.setUsername(registration.getUsername());
        account.setEmail(registration.getEmail());
        account.setPassword(passwordEncoder.encode(registration.getPassword()));
        account.setCreateDate(new Date());
        account.setUpdateDate(new Date());
        account.setRoles(new HashSet<>(List.of(Role.USER)));
        return accountRepository.save(account);
    }

    @Override
    public Account findByEmail(String email) {
        return null;
    }

    @Override
    @Transactional
    @PostAuthorize("returnObject.username == authentication.name")
    public Account findAccountById(Long id) {
        log.info("Fetching user {}", id);
        Account findAccount = accountRepository.findAccountById(id);
        if(findAccount != null) {
            return findAccount;
        }else {
            throw new CustomEmptyDataException("unable to find account with such id");
        }
    }

    @Override
    @Transactional
    @PostFilter("filterObject.roles.size() >= 1")
    public List<Account> getList() {
        log.info("Fetching all users");
        return accountRepository.findAll();
    }

    @Override
    public Account updateAccount(Account source, Long id) {
        Account accountForUpdate = accountRepository.findAccountById(id);
        if (accountForUpdate != null) {
            accountForUpdate.setUsername(source.getUsername());
            accountForUpdate.setEmail(source.getEmail());
            accountForUpdate.setPassword(passwordEncoder.encode(source.getPassword()));
            accountForUpdate.setUpdateDate(new Date());

            accountRepository.save(accountForUpdate);
            return accountForUpdate;
        } else {
            throw new CustomEmptyDataException("unable to update user");
        }
    }

    @Override
    public String deleteAccount(Long id) {
        Account accountForDelete = accountRepository.findAccountById(id);

        if (accountForDelete != null) {
            accountRepository.delete(accountForDelete);
            return "User with id:" + id + " was successfully remover";
        } else {
            throw new CustomEmptyDataException("unable to delete user");
        }
    }

}
