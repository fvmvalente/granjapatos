package br.com.desafiojava.granjapatos.repositories;

import br.com.desafiojava.granjapatos.model.cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findClienteById(Long id);
}
