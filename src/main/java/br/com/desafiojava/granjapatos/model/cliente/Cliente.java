package br.com.desafiojava.granjapatos.model.cliente;

import br.com.desafiojava.granjapatos.dtos.ClienteDTO;
import br.com.desafiojava.granjapatos.enums.TipoCliente;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity(name = "cliente")
@Table(name = "cliente")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Cliente{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length=100)
    private String nome;

    @NotNull
    private Boolean ativo;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private TipoCliente tipo;

    public Cliente(ClienteDTO dto) {
        this.nome = dto.getNome();
        this.tipo = dto.getTipo();
        this.ativo = true;
    }

}
