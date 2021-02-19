package com.uni.bankuni.controller;

import com.uni.bankuni.domain.ResponseBody;
import com.uni.bankuni.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {

    public final TransferService transferService;

    @GetMapping("/history")
    public ResponseEntity<?> getHistoryForUser(HttpServletRequest request, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseBody.ok(
                transferService.getHistory(authentication.getName()), request.getContextPath()));
    }
}
