package br.com.desafiojava.granjapatos.dtos;

import br.com.desafiojava.granjapatos.enums.StatusPato;
import br.com.desafiojava.granjapatos.enums.TipoPato;
import br.com.desafiojava.granjapatos.model.pato.Pato;
import br.com.desafiojava.granjapatos.model.venda.Venda;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatoUpdateDTO {
    private Long id;
    private String nome;
    private TipoPato tipo;
    private BigDecimal valor;
    private StatusPato status;
    private Long mae_id;
    private Pato mae;
    private Long venda_id;
    private Venda venda;
}