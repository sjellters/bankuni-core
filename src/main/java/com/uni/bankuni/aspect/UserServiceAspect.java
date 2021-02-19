package com.uni.bankuni.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Aspect
@Configuration
public class UserServiceAspect {

    @Before("execution(* com.uni.bankuni.service.UserService.registerUser(..))")
    public void beforeRegister(JoinPoint joinPoint) {
        log.info("Attempting to register a new user");
        log.info("Allowed execution for {}", joinPoint);
    }

    @After("execution(* com.uni.bankuni.service.UserService.registerUser(..))")
    public void afterRegister(JoinPoint joinPoint) {
        log.info("User registration end");
        log.info("Finished execution for {}", joinPoint);
    }
}
