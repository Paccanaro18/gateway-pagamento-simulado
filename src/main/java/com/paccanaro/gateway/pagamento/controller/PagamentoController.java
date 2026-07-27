package com.paccanaro.gateway.pagamento.controller;

import com.paccanaro.gateway.pagamento.dto.CriarPagamentoRequest;
import com.paccanaro.gateway.pagamento.model.Pagamento;
import com.paccanaro.gateway.pagamento.model.Usuario;
import com.paccanaro.gateway.pagamento.repository.UsuarioRepository;
import com.paccanaro.gateway.pagamento.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;
    @Autowired
    private UsuarioRepository usuarioRepository;


    @PostMapping
    public ResponseEntity<Pagamento> criarPagamento(@RequestBody CriarPagamentoRequest request,
                                                    @AuthenticationPrincipal OidcUser principal) {

        Usuario usuario = usuarioRepository.findByEmail(principal.getAttribute("email"))
                .orElseThrow(() -> new IllegalStateException("usuario nao encontrado"));

        Pagamento pagamento = pagamentoService.criarPagamento(
                usuario,
                request.getValor(),
                request.getMetodoPagamento()
        );
        return ResponseEntity.ok(pagamento);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> buscarPorId(@PathVariable Integer id) {
        return pagamentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}