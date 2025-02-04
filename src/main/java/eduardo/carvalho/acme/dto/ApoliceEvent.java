package eduardo.carvalho.acme.dto;

import eduardo.carvalho.acme.model.Apolice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApoliceEvent {
    private UUID eventId;
    private Apolice apolice;
}
