package eduardo.carvalho.acme.service;

import eduardo.carvalho.acme.dto.ApoliceEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

public class ApoliceEventConsumer {
    @KafkaListener(topics = "apolices", groupId = "seguradora-acme")
    public void consumirEvento(ConsumerRecord<String, ApoliceEvent> record) {
        ApoliceEvent evento = record.value();
        System.out.println("Evento recebido: " + evento);
        // Processar a solicitação
    }
}
