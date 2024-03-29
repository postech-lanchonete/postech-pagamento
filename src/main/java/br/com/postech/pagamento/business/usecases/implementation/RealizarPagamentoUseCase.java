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
@Component("realizarPagamentoUseCase")
public class RealizarPagamentoUseCase implements UseCase<Pagamento, Pagamento> {

    private final PagamentoGateway pagamentoGateway;

    @Override
    public Pagamento realizar(Pagamento pagamento) {
        log.info("Realizando pagamento do cliente {}...", pagamento.getPedido().getIdCliente());
        pagamento.setStatus(StatusPagamento.APROVADO);
        log.info("Pagamento realizado com sucesso");
        return this.pagamentoGateway.salvar(pagamento);
    }
}
