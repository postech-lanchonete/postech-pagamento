package br.com.postech.pagamento.business.usecases.implementation;

import br.com.postech.pagamento.drivers.external.PagamentoGateway;
import br.com.postech.pagamento.business.usecases.UseCase;
import br.com.postech.pagamento.core.entities.Pagamento;
import br.com.postech.pagamento.core.entities.Produto;
import br.com.postech.pagamento.core.enums.StatusPagamento;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@AllArgsConstructor
@Component("desfazerPagamentoUseCase")
public class DesfazerPagamentoUseCase implements UseCase<Pagamento, Pagamento> {

    private final PagamentoGateway pagamentoGateway;

    @Override
    public Pagamento realizar(Pagamento pagamento) {
        var valorTotal = pagamento.getPedido().getProdutos().stream()
                .map(Produto::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        pagamento.setValor(valorTotal);
        pagamento.setStatus(StatusPagamento.REPROVADO_PENDENTE);
        this.pagamentoGateway.salvar(pagamento);
        log.info("Realizando rollback do pagamento do cliente {} no valor de {}",
                pagamento.getPedido().getIdCliente(),
                valorTotal);
        pagamento.setStatus(StatusPagamento.REPROVADO);
        log.info("Pagamento desfeito com sucesso");
        return this.pagamentoGateway.salvar(pagamento);
    }
}
