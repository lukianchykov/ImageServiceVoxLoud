package com.voxloud.imageservicevoxloud.repository;

import com.voxloud.imageservicevoxloud.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);
    Account findAccountById(Long id);
    Account findByEmail(String email);
    List<Account> findAll();
}
