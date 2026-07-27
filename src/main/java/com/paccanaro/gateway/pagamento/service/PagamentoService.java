package com.paccanaro.gateway.pagamento.service;

import com.paccanaro.gateway.pagamento.model.MetodoPagamento;
import com.paccanaro.gateway.pagamento.model.Pagamento;

import com.paccanaro.gateway.pagamento.model.StatusPagamento;
import com.paccanaro.gateway.pagamento.model.Usuario;
import com.paccanaro.gateway.pagamento.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PagamentoService {
    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private WebhookSimuladoService webhookSimuladoService;

    public Pagamento criarPagamento(Usuario usuario, BigDecimal valor, MetodoPagamento metodo,
                                    String ultimosDigitosCartao, String bandeiraCartao) {
        Pagamento pagamento = new Pagamento();
        pagamento.setUsuario(usuario);
        pagamento.setValor(valor);
        pagamento.setMetodoPagamento(metodo);

        if (metodo == MetodoPagamento.PIX) {
            pagamento.setDadosPix(gerarCodigoPixSimulado());
        }  else if (metodo == MetodoPagamento.CARTAO){
            pagamento.setUltimosDigitosCartao(ultimosDigitosCartao);
            pagamento.setBandeiraCartao(bandeiraCartao);
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

    public List<Pagamento> listarPorUsuario(Usuario usuario) {
        return pagamentoRepository.findByUsuarioOrderByDataCriacaoDesc(usuario);
    }

    public BigDecimal calcularTotalRecebido(Usuario usuario) {
        return pagamentoRepository.findByUsuarioOrderByDataCriacaoDesc(usuario).stream()
                .filter(p -> p.getStatus() == StatusPagamento.PAGO)
                .map(Pagamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long contarTransacoesDoMes(Usuario usuario) {
        LocalDateTime inicioDoMes = LocalDateTime.now().withDayOfMonth(1).toLocalDate().atStartOfDay();
        return pagamentoRepository.findByUsuarioOrderByDataCriacaoDesc(usuario).stream()
                .filter(p -> p.getDataCriacao().isAfter(inicioDoMes))
                .count();
    }

    public double calcularTaxaConversao(Usuario usuario) {
        List<Pagamento> pagamentos = pagamentoRepository.findByUsuarioOrderByDataCriacaoDesc(usuario);
        if (pagamentos.isEmpty()) return 0.0;

        long pagos = pagamentos.stream().filter(p -> p.getStatus() == StatusPagamento.PAGO).count();
        return (pagos * 100.0) / pagamentos.size();
    }

}