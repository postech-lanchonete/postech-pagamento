package br.com.postech.pagamento.integrationTest;

import br.com.postech.pagamento.adapters.repositories.PagamentoRepository;
import br.com.postech.pagamento.stub.PagamentoStub;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PagamentoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @BeforeEach
    void setUp() {
        // Limpar dados de teste antes de cada execução de teste
        pagamentoRepository.deleteAll();
    }


    @Test
    void buscarPorStatus_deveRetornarUmPagamento_quandoExistirUmPagamentoAprovado() throws Exception {
        var document = PagamentoStub.createPagamentoDocument();
        pagamentoRepository.save(document);

        mockMvc.perform(get("/v1/pagamentos/status/APROVADO")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].valor").value(document.getValor()))
                .andExpect(jsonPath("$[0].status").value(document.getStatus().name()))
                .andExpect(jsonPath("$[0].pedido.produtos").isArray())
                .andExpect(jsonPath("$[0].pedido.produtos[0].nome").value(document.getPedido().getProdutos().get(0).getNome()))
                .andExpect(jsonPath("$[0].pedido.produtos[0].preco").value(document.getPedido().getProdutos().get(0).getPreco()))
                .andExpect(jsonPath("$[0].pedido.idCliente").value(document.getPedido().getIdCliente()));
    }

    @Test
    void buscarPorStatus_deveRetornarListaVazia_quandoNaoExistirPagamentosNoStatusSelecionado() throws Exception {
        mockMvc.perform(get("/v1/pagamentos/status/APROVADO")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void buscarPorStatus_deveRetornar400_quandoStatusInformadoNaoExistir() throws Exception {
        mockMvc.perform(get("/v1/pagamentos/status/XXXX")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarPorId_deveRetornar404_quandoIdInformadoNaoRetornarNenhumProduto() throws Exception {
        mockMvc.perform(get("/v1/pagamentos/3fa85f64-5717-4562-b3fc-2c963f66afa6")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorId_deveRetornar400_quandoIdInformadoNaoForUmUUID() throws Exception {
        mockMvc.perform(get("/v1/pagamentos/3fa85f64")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarPorId_deveRetornarPagamento_quandoIdInformadoExistente() throws Exception {
        var document = PagamentoStub.createPagamentoDocument();
        pagamentoRepository.save(document);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/pagamentos/" + document.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(document.getId().toString()))
                .andExpect(jsonPath("$.valor").value(document.getValor()))
                .andExpect(jsonPath("$.status").value(document.getStatus().name()))
                .andExpect(jsonPath("$.pedido.produtos").isArray())
                .andExpect(jsonPath("$.pedido.produtos[0].nome").value(document.getPedido().getProdutos().get(0).getNome()))
                .andExpect(jsonPath("$.pedido.produtos[0].preco").value(document.getPedido().getProdutos().get(0).getPreco()))
                .andExpect(jsonPath("$.pedido.idCliente").value(document.getPedido().getIdCliente()));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
