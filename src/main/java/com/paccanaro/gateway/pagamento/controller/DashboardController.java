package com.paccanaro.gateway.pagamento.controller;

import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal OidcUser principal, Model model) {
        String nome = principal.getAttribute("name");
        String email = principal.getAttribute("email");

        model.addAttribute("nome", nome);
        model.addAttribute("email", email);
        model.addAttribute("iniciais", gerarIniciais(nome));

        return "dashboard";
    }

    private String gerarIniciais(String nome) {
        String[] partes = nome.trim().split("\\s+");
        String iniciais = "" + partes[0].charAt(0);
        if (partes.length > 1) {
            iniciais += partes[partes.length - 1].charAt(0);
        }
        return iniciais.toUpperCase();
    }

}
