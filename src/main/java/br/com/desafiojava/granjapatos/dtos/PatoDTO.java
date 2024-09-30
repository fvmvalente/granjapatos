package br.com.desafiojava.granjapatos.dtos;

import br.com.desafiojava.granjapatos.enums.StatusPato;
import br.com.desafiojava.granjapatos.enums.TipoPato;
import br.com.desafiojava.granjapatos.model.pato.Pato;
import br.com.desafiojava.granjapatos.model.venda.Venda;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatoDTO {

    private Long id;

    private String nome;

    private Pato mae;

    private Long mae_id;

    private Venda venda;

    private Long venda_id;

    private BigDecimal valor;

    private TipoPato tipo;

    private StatusPato status;
}
