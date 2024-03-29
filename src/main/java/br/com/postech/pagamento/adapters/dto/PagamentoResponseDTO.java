package br.com.postech.pagamento.adapters.dto;

import br.com.postech.pagamento.core.enums.StatusPagamento;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@SuppressWarnings("unused")
public class PagamentoResponseDTO {
    private UUID id;
    private BigDecimal valor;
    private StatusPagamento status;
    private PedidoDTO pedido;
}
