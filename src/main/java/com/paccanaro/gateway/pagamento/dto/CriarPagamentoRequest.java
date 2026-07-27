package com.paccanaro.gateway.pagamento.dto;

import com.paccanaro.gateway.pagamento.model.MetodoPagamento;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter @Setter
public class CriarPagamentoRequest {

    private BigDecimal valor;
    private MetodoPagamento metodoPagamento;
    private String ultimosDigitosCartao;
    private String bandeiraCartao;
}
