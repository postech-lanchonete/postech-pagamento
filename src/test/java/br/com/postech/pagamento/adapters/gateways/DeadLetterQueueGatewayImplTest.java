package br.com.postech.pagamento.adapters.gateways;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DeadLetterQueueGatewayImplTest {

    @Test
    void enviar_DeveEnviarMensagemParaOTopicoCorreto() {
        KafkaTemplate<String, String> kafkaTemplateMock = mock(KafkaTemplate.class);
        DeadLetterQueueGatewayImpl gateway = new DeadLetterQueueGatewayImpl(kafkaTemplateMock);

        String topico = "meu_topico";
        String mensagem = "Minha mensagem";

        gateway.enviar(topico, mensagem);

        ArgumentCaptor<String> topicoCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> mensagemCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafkaTemplateMock).send(topicoCaptor.capture(), mensagemCaptor.capture());

        assertEquals(topico, topicoCaptor.getValue());
        assertEquals(mensagem, mensagemCaptor.getValue());
    }
}
