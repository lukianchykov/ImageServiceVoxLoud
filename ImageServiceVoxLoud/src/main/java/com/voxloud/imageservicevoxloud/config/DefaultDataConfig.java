package com.voxloud.imageservicevoxloud.config;

import com.voxloud.imageservicevoxloud.entity.Account;
import com.voxloud.imageservicevoxloud.entity.Role;
import com.voxloud.imageservicevoxloud.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Component
@Slf4j
public class DefaultDataConfig {
  private final AccountService accountService;
  private final PasswordEncoder passwordEncoder;

  public DefaultDataConfig(AccountService accountService, PasswordEncoder passwordEncoder) {
    this.accountService = accountService;
    this.passwordEncoder = passwordEncoder;
    createDefaultUsers();
  }

  private void createDefaultUsers() {
    accountService.saveAccount(new Account(null, "john", passwordEncoder.encode("123"), "john321@mail.ru"
            , new Date(), new Date(), new HashSet<>(), new HashSet<>(List.of(Role.USER))));
    accountService.saveAccount(new Account(null, "jane", passwordEncoder.encode("123"), "jane321@mail.ru"
            , new Date(), new Date(), new HashSet<>(), new HashSet<>(List.of(Role.USER))));
    accountService.saveAccount(new Account(null, "admin", passwordEncoder.encode("admin"), "admin333@gmail.com"
            , new Date(), new Date(), new HashSet<>(), new HashSet<>(List.of(Role.ADMIN))));
  }
}