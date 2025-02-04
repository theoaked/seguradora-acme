package eduardo.carvalho.acme.service;

import eduardo.carvalho.acme.dto.ApoliceEvent;
import eduardo.carvalho.acme.dto.FraudResponse;
import eduardo.carvalho.acme.dto.SimplifiedApolice;
import eduardo.carvalho.acme.model.Apolice;
import eduardo.carvalho.acme.model.History;
import eduardo.carvalho.acme.repository.ApoliceRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApoliceService {

    private final ApoliceRepository apoliceRepository;

    private final RestTemplate restTemplate;

    // Criação de uma nova apolice
    public SimplifiedApolice createApolice(Apolice apolice) {

        LocalDateTime timeRegister = LocalDateTime.now();
        // criando e setando na apolice a primeira lista de historico de status
        List<History> historyStatus = new ArrayList<>();
        historyStatus.add(new History(History.Status.RECEIVED, timeRegister));
        apolice.setHistory(historyStatus);
        // setando os registros de data
        apolice.setCreatedAt(timeRegister);
        apolice.setFinishedAt(timeRegister);
        // setando status de recebido
        apolice.setStatus(Apolice.Status.RECEIVED);

        // persistir a apolice
        Apolice createdApolice = apoliceRepository.save(apolice);

        // retornar objeto simplificado, com id da apolice + o timestamp do primeiro history
        return new SimplifiedApolice(createdApolice.getId(), createdApolice.getHistory().get(0).getTimestamp());
    }

    @KafkaListener(topics = "apolices", groupId = "seguradora-acme")
    public void analiseFraud(ConsumerRecord<String, ApoliceEvent> record){

        // capturar apolice do evento recebido
        Apolice apolice = record.value().getApolice();

        // Consultar API de Fraudes para obter a classificação de risco
        String fraudApiUrl = "http://api-de-fraudes.com/avaliar";
        FraudResponse fraudResponse = restTemplate.postForObject(fraudApiUrl, apolice.getCustomerId(), FraudResponse.class);

        // capturar lista de mudanças de status da apolice para inclusão
        List<History> historyStatus = apolice.getHistory();
        historyStatus.add(new History(History.Status.VALIDATED,LocalDateTime.now()));
        apolice.setHistory(historyStatus);

        // aplicar regras de validacao conforme a classificacao de rico da api
        if (fraudResponse.getClassification().equals("HIGH_RISK")) {
            System.out.println("Classificação alta");
            apolice.setStatus(Apolice.Status.REJECTED);
        } else {
            System.out.println("Classificação baixa");
            apolice.setStatus(Apolice.Status.VALIDATED);
        }

        // Salvar novamente a apolice com o status de risco
        apoliceRepository.save(apolice);
    }

    // Encontrar apolice por ID
    public Optional<Apolice> getApoliceById(String id) {
        return apoliceRepository.findById(id);
    }

    // Encontrar apolices por customerId
    public List<Apolice> getApolicesByCustomerId(UUID customerId) {
        return apoliceRepository.findByCustomerId(customerId);
    }

    // Atualizar uma apolice
    public Apolice updateApolice(String id, Apolice updatedApolice) {
        if (apoliceRepository.existsById(id)) {
            updatedApolice.setId(id);
            return apoliceRepository.save(updatedApolice);
        } else {
            return null; // ou lançar exceção
        }
    }

    // Excluir apolice
    public boolean deleteApolice(String id) {
        if (apoliceRepository.existsById(id)) {
            apoliceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Adicionar cobertura à apolice
    public Apolice addCoverage(String apoliceId, String coverageName, BigDecimal coverageAmount) {
        Optional<Apolice> apoliceOpt = apoliceRepository.findById(apoliceId);
        if (apoliceOpt.isPresent()) {
            Apolice apolice = apoliceOpt.get();
            apolice.getCoverages().put(coverageName, coverageAmount);
            return apoliceRepository.save(apolice);
        }
        return null; // ou lançar exceção
    }

    // Adicionar histórico à apolice
    public Apolice addHistory(String apoliceId, History history) {
        Optional<Apolice> apoliceOpt = apoliceRepository.findById(apoliceId);
        if (apoliceOpt.isPresent()) {
            Apolice apolice = apoliceOpt.get();
            apolice.getHistory().add(history);
            return apoliceRepository.save(apolice);
        }
        return null; // ou lançar exceção
    }
}

