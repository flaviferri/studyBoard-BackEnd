package com.sb.studyBoard_Backend.controller;

import com.sb.studyBoard_Backend.config.jwt.JwtService;
import com.sb.studyBoard_Backend.model.UserEntity;
import com.sb.studyBoard_Backend.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    private final UserService userService;

    private final JwtService jwtService;

    private final RestTemplate restTemplate;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    public LoginController(UserService userService, JwtService jwtService, RestTemplate restTemplate) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.restTemplate = restTemplate;
    }

    @CrossOrigin(origins = "http://localhost:4001")
    @RequestMapping(value = "/auth/github/callback", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseEntity<Map<String, Object>> githubCallback(@RequestParam("code") String code) {
        System.out.println("Received code: " + code);

        String accessToken = exchangeCodeForAccessToken(code);
        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unable to retrieve access token"));
        }

        Map<String, Object> userAttributes = fetchUserAttributes(accessToken);
        if (userAttributes == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unable to fetch user details"));
        }

        UserEntity savedUser = userService.saveOrUpdateUser(userAttributes);
        String token = jwtService.generateToken(savedUser);

        return ResponseEntity.ok(Map.of("token", token));
    }

    private String exchangeCodeForAccessToken(String code) {
        String url = "https://github.com/login/oauth/access_token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, String> body = new HashMap<>();
        body.put("client_id", clientId);
        body.put("client_secret", clientSecret);
        body.put("code", code);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(url, HttpMethod.POST, entity,
                new ParameterizedTypeReference<>() {
                });

        System.out.println("Response Body: " + response.getBody());

        if (response.getBody() == null || !response.getBody().containsKey("access_token")) {
            System.out.println("Error: la respuesta no contiene un access token válido");
            return null;
        }

        String accessToken = response.getBody().get("access_token");
        if (accessToken != null && !accessToken.matches("[A-Za-z0-9_\\-\\.]+")) {
            System.out.println("Token contiene caracteres inválidos");
        }

        return accessToken;
    }

    private Map<String, Object> fetchUserAttributes(String accessToken) {
        String url = "https://api.github.com/user";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @CrossOrigin(origins = "http://localhost:4001")
    @GetMapping("/home")
    public ResponseEntity<Map<String, String>> greeting(
            @RequestParam(required = false, defaultValue = "World") String name) {
        System.out.println("==== get greeting ====");
        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}
