package com.sb.studyBoard_Backend.controller;


import com.sb.studyBoard_Backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login/success")
    public Map<String, Object> loginSuccess(@AuthenticationPrincipal OAuth2User user) {
        if (user == null) {
            throw new RuntimeException("User not authenticated");
        }

        Map<String, Object> userAttributes = user.getAttributes();
        userService.saveOrUpdateUser(userAttributes);

        return userAttributes;
    }
}

