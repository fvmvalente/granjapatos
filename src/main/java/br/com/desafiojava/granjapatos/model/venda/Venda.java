package br.com.desafiojava.granjapatos.model.venda;

import br.com.desafiojava.granjapatos.dtos.VendaDTO;
import br.com.desafiojava.granjapatos.enums.TipoCliente;
import br.com.desafiojava.granjapatos.model.cliente.Cliente;
import br.com.desafiojava.granjapatos.model.pato.Pato;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "venda")
@Table(name = "venda")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = "id")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Venda{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "venda")
    @JsonManagedReference
    private List<Pato> patos = new ArrayList<>();

    @NotNull
    private BigDecimal valor;

    private LocalDateTime data;

    @NotNull
    private Boolean ativo;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private TipoCliente tipoCliente;

    public Venda(VendaDTO dto) {
        this.valor = dto.getValor();
        this.tipoCliente = dto.getTipoCliente();
        this.ativo = true;

        if (dto.getPatos() != null) {
            this.patos = new ArrayList<>();
            for (Pato pato : dto.getPatos()) {
                pato.setVenda(this);
                this.patos.add(pato);
            }
        }
        this.data = LocalDateTime.now();
    }
}
