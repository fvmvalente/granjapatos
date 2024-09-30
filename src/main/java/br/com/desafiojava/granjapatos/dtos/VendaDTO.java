package br.com.desafiojava.granjapatos.dtos;

import br.com.desafiojava.granjapatos.enums.TipoCliente;
import br.com.desafiojava.granjapatos.model.cliente.Cliente;
import br.com.desafiojava.granjapatos.model.pato.Pato;
import br.com.desafiojava.granjapatos.model.venda.LongPato;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendaDTO {

    private Cliente cliente;

    private Long cliente_id;

    private TipoCliente tipoCliente;

    private List<Pato> patos;

    private List<LongPato> patosIds;

    private BigDecimal valor;

    private LocalDateTime data;

    private Boolean ativo;


}
