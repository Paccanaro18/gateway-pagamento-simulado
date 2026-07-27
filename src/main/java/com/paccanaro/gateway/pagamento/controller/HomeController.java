package com.paccanaro.gateway.pagamento.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "Página inicial pública. Acesse /home para logar com Google.";
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        String nome = principal.getAttribute("name");

        return "Bem-Vindo , " + nome + "! Seu email é : " + email;
    }

}
