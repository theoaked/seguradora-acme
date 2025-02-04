package eduardo.carvalho.acme.repository;

import eduardo.carvalho.acme.model.Apolice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApoliceRepository extends JpaRepository<Apolice, String> {

    List<Apolice> findByCustomerId(UUID customerId);

}
