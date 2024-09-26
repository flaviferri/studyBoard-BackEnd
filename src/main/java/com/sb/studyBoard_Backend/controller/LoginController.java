package com.sb.studyBoard_Backend.controller;


import com.sb.studyBoard_Backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login/success")
    public RedirectView loginSuccess(
            @AuthenticationPrincipal OAuth2User user) {

        if (user == null) {
            // Si el usuario no está autenticado, puedes redirigirlo a una página de error
            return new RedirectView("/error");
        }

        // Guardar los datos del usuario en la base de datos
        Map<String, Object> userAttributes = user.getAttributes();
        userService.saveOrUpdateUser(userAttributes);

        // Redirigir a la página de inicio del frontend después de guardar al usuario
        return new RedirectView("http://localhost:5173/home"); // Cambia la URL a la página de tu frontend
    }
}

