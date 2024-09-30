package br.com.desafiojava.granjapatos.repositories;

import br.com.desafiojava.granjapatos.model.venda.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendaRepository extends JpaRepository<Venda, Long> {
    Optional<Venda> findVendaById(Long id);
}
