package br.com.desafiojava.granjapatos.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseVenda {
    private Long id;
    private ClienteDTO cliente;
    private BigDecimal valor;
    private List<ResponsePato> patos;
}
