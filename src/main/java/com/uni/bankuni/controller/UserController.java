package com.uni.bankuni.controller;

import com.uni.bankuni.domain.ResponseBody;
import com.uni.bankuni.domain.User;
import com.uni.bankuni.exception.UserAlreadyExistsException;
import com.uni.bankuni.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(HttpServletRequest request, @RequestBody User user) {
        try {
            userService.registerUser(user);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ResponseBody.created(
                            "El usuario fue registrado con éxito",
                            request.getContextPath()));
        } catch (UserAlreadyExistsException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseBody.internalServerError(
                            "Ya existe un usuario registrado con este email",
                            request.getContextPath()));
        }
    }
}
