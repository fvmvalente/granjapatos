package br.com.desafiojava.granjapatos.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioPato {
    private Long id;
    private String nome;
    private String status;
    private String cliente;
    private String tipo_cliente;
    private String valor;
    private List<RelatorioPato> filhos;
}