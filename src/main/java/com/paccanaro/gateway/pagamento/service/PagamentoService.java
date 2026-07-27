package com.paccanaro.gateway.pagamento.service;

import com.paccanaro.gateway.pagamento.model.MetodoPagamento;
import com.paccanaro.gateway.pagamento.model.Pagamento;

import com.paccanaro.gateway.pagamento.model.Usuario;
import com.paccanaro.gateway.pagamento.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PagamentoService {
    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private WebhookSimuladoService webhookSimuladoService;

    public Pagamento criarPagamento(Usuario usuario, BigDecimal valor, MetodoPagamento metodo) {
        Pagamento pagamento = new Pagamento();
        pagamento.setUsuario(usuario);
        pagamento.setValor(valor);
        pagamento.setMetodoPagamento(metodo);

        if (metodo == MetodoPagamento.PIX) {
            pagamento.setDadosPix(gerarCodigoPixSimulado());
        }

        Pagamento salvo = pagamentoRepository.save(pagamento);

        webhookSimuladoService.simularConfirmacao(salvo.getId());

        return salvo;
    }

    public Optional<Pagamento> buscarPorId(Integer id) {
        return pagamentoRepository.findById(id);
    }

    private String gerarCodigoPixSimulado() {
        return "00020126580014BR.GOV.BCB.PIX0136" + java.util.UUID.randomUUID() + "5204000053039865802BR5913FinCore Pay6009SAO PAULO62070503***6304";
    }
}