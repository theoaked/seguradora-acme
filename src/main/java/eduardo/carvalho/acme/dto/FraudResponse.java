package eduardo.carvalho.acme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FraudResponse {
    private UUID orderId;
    private UUID customerId;
    private OffsetDateTime analyzedAt;
    private String classification;
    private List<Occurrence> occurrences;

    // Classe interna 'Occurrence' representando os itens da lista 'occurrences'
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Occurrence {
        private UUID id;
        private long productId;
        private String type;
        private String description;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
    }
}
