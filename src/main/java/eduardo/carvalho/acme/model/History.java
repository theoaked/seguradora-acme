package eduardo.carvalho.acme.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class History {
    private Status status;
    private LocalDateTime timestamp;

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

