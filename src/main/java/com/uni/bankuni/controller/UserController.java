package com.uni.bankuni.controller;

import com.uni.bankuni.domain.ResponseBody;
import com.uni.bankuni.domain.User;
import com.uni.bankuni.exception.UserAlreadyExistsException;
import com.uni.bankuni.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(HttpServletRequest request, @PathVariable("id") String userId) {
        User user = userService.getUser(userId);

        if (user != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ResponseBody.ok(user, request.getContextPath()));
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseBody.notFound(
                            "No se encontró ningún usuario asociado a este id",
                            request.getContextPath()
                    ));
        }
    }
}
