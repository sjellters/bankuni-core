package com.uni.bankuni.controller;

import com.uni.bankuni.domain.ResponseBody;
import com.uni.bankuni.domain.TransferRequest;
import com.uni.bankuni.exception.InsufficientAmountException;
import com.uni.bankuni.exception.TransferNotFoundException;
import com.uni.bankuni.exception.UserNotFoundException;
import com.uni.bankuni.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/verifyTransfer")
    public ResponseEntity<?> verifyTransfer(HttpServletRequest request, TransferRequest transferRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseBody.ok(
                    transferService.verifyTransfer(transferRequest), request.getContextPath()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseBody.notFound(
                    "No se encontró un usuario asociado a este id", request.getContextPath()));
        } catch (InsufficientAmountException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseBody.badRequest(
                    "El monto ingresado excede la cantidad disponible", request.getContextPath()));
        }
    }

    @GetMapping("/execute/{id}")
    public ResponseEntity<?> executeTransfer(HttpServletRequest request, @PathVariable("id") String id) {
        try {
            transferService.executeTransfer(id);

            return ResponseEntity.status(HttpStatus.OK).body(ResponseBody.ok(
                    "La transferencia ha sido realizada con éxito", request.getContextPath()));
        } catch (InsufficientAmountException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseBody.badRequest(
                    "El monto ingresado excede la cantidad disponible", request.getContextPath()));
        } catch (TransferNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseBody.notFound(
                    "La transferencia solicitada no se encuentra en el registro",
                    request.getContextPath()));
        }
    }
}
