package br.com.desafiojava.granjapatos.model.pato;

import br.com.desafiojava.granjapatos.dtos.PatoDTO;
import br.com.desafiojava.granjapatos.enums.StatusPato;
import br.com.desafiojava.granjapatos.enums.TipoPato;
import br.com.desafiojava.granjapatos.model.venda.Venda;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity(name = "pato")
@Table(name = "pato")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Pato{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "mae_id")
    private Pato mae;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "venda_id")
    @JsonBackReference
    private Venda venda;

    @NotNull
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private TipoPato tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private StatusPato status;

    public Pato(PatoDTO dto) {
        this.nome = dto.getNome();
        this.mae = dto.getMae();
        this.tipo = dto.getTipo();
        this.valor = dto.getValor();
        this.status = StatusPato.DISPONIVEL;
    }
}
