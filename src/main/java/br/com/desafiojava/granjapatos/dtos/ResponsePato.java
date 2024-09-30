package br.com.desafiojava.granjapatos.dtos;

import br.com.desafiojava.granjapatos.enums.StatusPato;
import br.com.desafiojava.granjapatos.enums.TipoPato;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePato {
    private Long id;
    private String nome;
    private TipoPato tipo;
    private BigDecimal valor;
    private Long mae_id;
    private Long venda_id;
    private StatusPato status;
}
