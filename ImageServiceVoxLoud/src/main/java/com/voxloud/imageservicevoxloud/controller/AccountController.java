package com.voxloud.imageservicevoxloud.controller;

import com.voxloud.imageservicevoxloud.entity.Account;
import com.voxloud.imageservicevoxloud.entity.dto.UserRegistrationDto;
import com.voxloud.imageservicevoxloud.exception.CustomEmptyDataException;
import com.voxloud.imageservicevoxloud.service.AccountService;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Controller
@RequiredArgsConstructor
@ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 400, message = "This is a bad request, please follow the API documentation for the proper request format"),
        @io.swagger.annotations.ApiResponse(code = 401, message = "Due to security constraints, your access request cannot be authorized"),
        @io.swagger.annotations.ApiResponse(code = 500, message = "The server is down. Please bear with us."),
})
public class AccountController {
    private final AccountService accountService;

    @Secured("USER")
    @GetMapping("/account/{id}")
    public String welcome(@PathVariable Long id, Model model) {
        model.addAttribute("account", accountService.findAccountById(id));
        return "account-profile";
    }

    @ModelAttribute("account")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUserAccount(@ModelAttribute("account") @Valid UserRegistrationDto userDto,
                                      BindingResult result){

        Account existing = accountService.findByEmail(userDto.getEmail());
        if (existing != null){
            result.rejectValue("email", String.valueOf(HttpStatus.CONFLICT), "There is already an account registered with that email");
        }

        if (result.hasErrors()){
            return "registration";
        }

        accountService.save(userDto);
        return "redirect:/login";
    }

    @Secured("ADMIN")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/admin/{id}")
    public String getAdminPage(@PathVariable Long id, Model model) {
        model.addAttribute("admin", accountService.findAccountById(id));
        model.addAttribute("list" , accountService.getList());
        return "admin-page";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    @Secured("ADMIN")
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/accounts")
    public ResponseEntity<List<Account>> getAccounts() {
        return ResponseEntity.ok().body(accountService.getList());
    }

    @Secured("ADMIN")
    @PreAuthorize("isAuthenticated()")
    @PutMapping(path = "/account/{id}")
    public ResponseEntity<Account> updateUser(@RequestBody Account source, @PathVariable Long id) {
        return ResponseEntity.ok().body(accountService.updateAccount(source,id));
    }

    @Secured("ADMIN")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(path = "/account/{id}")
    public ResponseEntity<String> deleteUser (@PathVariable Long id) {
        return ResponseEntity.ok().body(accountService.deleteAccount(id));
    }

    @ExceptionHandler
    public ResponseEntity<String> onConflictAccountEmail(DataIntegrityViolationException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ClassUtils.getShortName(exception.getClass()) + ": User with such email already registered");
    }

    @ExceptionHandler
    public ResponseEntity<String> onConflictAccountName(DataIntegrityViolationException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ClassUtils.getShortName(exception.getClass()) + ": User with such accountName already registered");
    }

    @ExceptionHandler
    public ResponseEntity<String> onMissingAccountId(NoSuchElementException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClassUtils.getShortName(exception.getClass()) + ": No such user was found");
    }

    @ExceptionHandler
    public ResponseEntity<String> onMissingAccount (EmptyResultDataAccessException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClassUtils.getShortName(exception.getClass())
                + exception.getLocalizedMessage()
                + ": no one user was found");
    }

    @ExceptionHandler
    public ResponseEntity<String> SQLProblems (SQLException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ClassUtils.getShortName(exception.getClass())
                + exception.getSQLState()
                + exception.getLocalizedMessage()
                + ": something went wrong with user");
    }

    @ExceptionHandler
    public ResponseEntity<String> customExceptionHandler (CustomEmptyDataException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClassUtils.getShortName(exception.getClass())
                + " "
                + exception.getCause()
                + " "
                + exception.getLocalizedMessage());
    }
}
