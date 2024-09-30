package br.com.desafiojava.granjapatos.repositories;

import br.com.desafiojava.granjapatos.dtos.PatoDTO;
import br.com.desafiojava.granjapatos.enums.StatusPato;
import br.com.desafiojava.granjapatos.model.pato.Pato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatoRepository extends JpaRepository<Pato, Long> {
    Optional<Pato> findPatoById(Long id);
    Optional<Pato> findPatoByMae(Pato mae);
    Optional<List<Pato>> findPatoFilhosByMae(Pato mae);
    long countByMaeId(Long maeId);
    List<Pato> findByStatus(StatusPato status);

}
