package com.paccanaro.gateway.pagamento.controller;

import com.paccanaro.gateway.pagamento.model.Usuario;
import com.paccanaro.gateway.pagamento.repository.PagamentoRepository;
import com.paccanaro.gateway.pagamento.repository.UsuarioRepository;
import com.paccanaro.gateway.pagamento.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PagamentoService pagamentoService;



    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal OidcUser principal, Model model) {
        String nome = principal.getAttribute("name");
        String email = principal.getAttribute("email");

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado"));

        model.addAttribute("nome", nome);
        model.addAttribute("email", email);
        model.addAttribute("iniciais", gerarIniciais(nome));

        model.addAttribute("pagamentos", pagamentoService.listarPorUsuario(usuario));
        model.addAttribute("totalRecebido", pagamentoService.calcularTotalRecebido(usuario));
        model.addAttribute("transacoesDoMes", pagamentoService.contarTransacoesDoMes(usuario));
        model.addAttribute("taxaConversao", pagamentoService.calcularTaxaConversao(usuario));

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
