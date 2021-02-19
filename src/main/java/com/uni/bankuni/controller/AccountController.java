package com.uni.bankuni.controller;

import com.uni.bankuni.domain.ResponseBody;
import com.uni.bankuni.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping()
    public ResponseEntity<?> getAccountInfo(HttpServletRequest request, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseBody.ok(accountService.getAccount(authentication.getName()), request.getContextPath())
        );
    }
}
