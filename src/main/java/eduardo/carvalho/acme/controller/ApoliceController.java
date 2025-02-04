package eduardo.carvalho.acme.controller;

import eduardo.carvalho.acme.dto.SimplifiedApolice;
import eduardo.carvalho.acme.model.Apolice;
import eduardo.carvalho.acme.model.History;
import eduardo.carvalho.acme.service.ApoliceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/apolices")
@RequiredArgsConstructor
public class ApoliceController {

    private final ApoliceService apoliceService;

    // Criar uma nova apólice
    @PostMapping
    public ResponseEntity<SimplifiedApolice> createApolice(@RequestBody Apolice apolice) {
        SimplifiedApolice createdApolice = apoliceService.createApolice(apolice);
        return new ResponseEntity<>(createdApolice, HttpStatus.CREATED);
    }

    // Buscar apólice por ID
    @GetMapping("/{id}")
    public ResponseEntity<Apolice> getApoliceById(@PathVariable String id) {
        Optional<Apolice> apolice = apoliceService.getApoliceById(id);
        return apolice.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Buscar apólices por customerId
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Apolice>> getApolicesByCustomerId(@PathVariable UUID customerId) {
        List<Apolice> apolices = apoliceService.getApolicesByCustomerId(customerId);
        return apolices.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.ok(apolices);
    }

    // Atualizar uma apólice
    @PutMapping("/{id}")
    public ResponseEntity<Apolice> updateApolice(@PathVariable String id, @RequestBody Apolice updatedApolice) {
        Apolice apolice = apoliceService.updateApolice(id, updatedApolice);
        return apolice != null ? ResponseEntity.ok(apolice)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Excluir apólice
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApolice(@PathVariable String id) {
        boolean isDeleted = apoliceService.deleteApolice(id);
        return isDeleted ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Adicionar cobertura a uma apólice
    @PostMapping("/{id}/coverage")
    public ResponseEntity<Apolice> addCoverage(@PathVariable String id,
                                               @RequestParam String coverageName,
                                               @RequestParam BigDecimal coverageAmount) {
        Apolice apolice = apoliceService.addCoverage(id, coverageName, coverageAmount);
        return apolice != null ? ResponseEntity.ok(apolice)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Adicionar histórico a uma apólice
    @PostMapping("/{id}/history")
    public ResponseEntity<Apolice> addHistory(@PathVariable String id, @RequestBody History history) {
        Apolice apolice = apoliceService.addHistory(id, history);
        return apolice != null ? ResponseEntity.ok(apolice)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

