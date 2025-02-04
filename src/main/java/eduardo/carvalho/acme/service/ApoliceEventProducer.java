package eduardo.carvalho.acme.service;

import eduardo.carvalho.acme.dto.ApoliceEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ApoliceEventProducer {
    private final KafkaTemplate<String, ApoliceEvent> kafkaTemplate;

    public ApoliceEventProducer(KafkaTemplate<String, ApoliceEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void createEvent(ApoliceEvent apoliceEvent) {
        kafkaTemplate.send("apolices", apoliceEvent);
    }
}
