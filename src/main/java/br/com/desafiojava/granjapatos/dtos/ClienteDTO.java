package br.com.desafiojava.granjapatos.dtos;

import br.com.desafiojava.granjapatos.enums.TipoCliente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {

    private Long id;

    @NotBlank
    private String nome;

    private Boolean ativo;

    private TipoCliente tipo;

}
