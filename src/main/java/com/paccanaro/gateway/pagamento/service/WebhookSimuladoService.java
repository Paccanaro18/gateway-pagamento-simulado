package com.paccanaro.gateway.pagamento.service;

import com.paccanaro.gateway.pagamento.model.Pagamento;
import com.paccanaro.gateway.pagamento.model.StatusPagamento;
import com.paccanaro.gateway.pagamento.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WebhookSimuladoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Async
    public void simularConfirmacao(Integer pagamentoId) {
        try {
            Thread.sleep(8000);

            Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
                    .orElseThrow(() -> new IllegalStateException("Pagamento não encontrado"));

            pagamento.setStatus(StatusPagamento.PAGO);
            pagamento.setDataPagamento(LocalDateTime.now());
            pagamentoRepository.save(pagamento);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

