package eduardo.carvalho.acme.model;

import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "apolices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Apolice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private UUID customerId;
    private UUID productId;
    private String category;
    private String salesChannel;
    private String paymentMethod;
    private Status status;
    private BigDecimal totalMonthlyPremiumAmount;
    private BigDecimal insuredAmount;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private Map<String, BigDecimal> coverages;
    private List<String> assistances;
    private List<History> history;

    //Recebido, Validado, Pendente, Rejeitado, Aprovado, Cancelada
    public enum Status {
        RECEIVED,
        VALIDATED,
        PENDING,
        REJECTED,
        APPROVED,
        CANCELED
    }

}

