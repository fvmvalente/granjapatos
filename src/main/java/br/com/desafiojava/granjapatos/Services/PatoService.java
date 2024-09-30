package br.com.desafiojava.granjapatos.Services;

import br.com.desafiojava.granjapatos.dtos.PatoDTO;
import br.com.desafiojava.granjapatos.dtos.PatoUpdateDTO;
import br.com.desafiojava.granjapatos.dtos.RelatorioPato;
import br.com.desafiojava.granjapatos.enums.StatusPato;
import br.com.desafiojava.granjapatos.enums.TipoPato;
import br.com.desafiojava.granjapatos.exceptions.PatoNotFoundException;
import br.com.desafiojava.granjapatos.model.pato.Pato;
import br.com.desafiojava.granjapatos.model.venda.Venda;
import br.com.desafiojava.granjapatos.repositories.PatoRepository;
import br.com.desafiojava.granjapatos.repositories.VendaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PatoService {

    @Autowired
    private final PatoRepository patoRepository;

    @Autowired
    private final VendaRepository vendaRepository;

    @Autowired
    private final ModelMapper modelMapper;

    public PatoDTO cadastrarPato(PatoDTO dto) throws Exception {
        if (dto.getTipo() == TipoPato.FILHO) {
            validateMaeId(dto.getMae_id());
            Pato mae = patoRepository.findPatoById(dto.getMae_id())
                    .orElseThrow(() -> new Exception("ID da pata_mae inválido"));
            Long filhosCont = patoRepository.countByMaeId(mae.getId());
            if (filhosCont == 0) {
                mae.setValor(BigDecimal.valueOf(50.00));
            } else if (filhosCont == 1) {
                mae.setValor(BigDecimal.valueOf(25.00));
            }
            mae.setTipo(TipoPato.MAE);
            dto.setMae(mae);
            dto.setTipo(TipoPato.FILHO);
        }

        Pato pato = modelMapper.map(dto, Pato.class);
        pato.setValor(BigDecimal.valueOf(70.0));
        patoRepository.save(pato);
        PatoDTO patoDTO = modelMapper.map(pato, PatoDTO.class);
        if (pato.getMae() != null)
            patoDTO.setMae_id(dto.getMae_id());
        if (pato.getVenda() != null)
            patoDTO.setVenda_id(pato.getVenda().getId());

        return patoDTO;
    }

    private void validateMaeId(Long maeId) {
        if (maeId == null) {
            throw new PatoNotFoundException("Informe o id da Pata Mae");
        }
    }

    public List<PatoDTO> buscarPatos() {
        return patoRepository.findAll().stream()
                .map(p -> modelMapper.map(p, PatoDTO.class))
                .collect(Collectors.toList());
    }

    public PatoDTO buscarPatoPorId(Long id) {
        Pato pato = patoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return modelMapper.map(pato, PatoDTO.class);
    }

    public List<PatoDTO> buscarPatosPorStatus(StatusPato status) {
        return patoRepository.findByStatus(status).stream()
                .map(p -> modelMapper.map(p, PatoDTO.class))
                .collect(Collectors.toList());
    }

    public PatoUpdateDTO atualizarPato(PatoUpdateDTO dto, Long id) throws Exception {
        Optional<Pato> optPato = this.patoRepository.findById(id);
        if (optPato.isPresent()) {
            Pato pato = optPato.get();
            //Atualiza nome se possivel
            if (dto.getNome() != null) {
                pato.setNome(dto.getNome());
            }

            //Atualiza tipo se possivel
            if (dto.getTipo() != null) {
                if ((dto.getTipo() != TipoPato.MAE) && (dto.getTipo() != TipoPato.FILHO)) {
                    throw new Exception("Tipo inválido, tipo aceito: MAE, FILHO");
                }
                pato.setTipo(dto.getTipo());

                if (dto.getTipo() == TipoPato.MAE) {
                    Long filhosCont = patoRepository.countByMaeId(pato.getId());
                    if (filhosCont == 0) {
                        pato.setValor(BigDecimal.valueOf(70.00));
                    } else if (filhosCont == 1) {
                        pato.setValor(BigDecimal.valueOf(50.00));
                    } else if (filhosCont == 2) {
                        pato.setValor(BigDecimal.valueOf(25.00));
                    }
                } else if (dto.getTipo() == TipoPato.FILHO) {
                    pato.setValor(BigDecimal.valueOf(70.00));
                }
            }

            if (dto.getMae_id() != null) {
                Pato mae = patoRepository.findPatoById(dto.getMae_id())
                        .orElseThrow(() -> new Exception("ID da pata_mae inválido"));
                pato.setMae(mae);
            }

            if (dto.getValor() != null)
                pato.setValor(dto.getValor());

            if (dto.getStatus() != null) {
                if ((dto.getStatus() != StatusPato.DISPONIVEL) && (dto.getStatus() != StatusPato.VENDIDO)) {
                    throw new Exception("Status informado inválido, status aceito DISPONIVEL, VENDIDO");
                }
                pato.setStatus(dto.getStatus());
            }

            PatoUpdateDTO patoDTO = modelMapper.map(pato, PatoUpdateDTO.class);
            if (pato.getMae() != null)
                patoDTO.setMae_id(pato.getMae().getId());
            if (pato.getVenda() != null)
                patoDTO.setVenda_id(pato.getVenda().getId());

            return patoDTO;
        } else {
            throw new PatoNotFoundException("Pato de id " + id + " não foi encontrado.");
        }
    }

    public void excluirPato(@NotNull Long id) {
        patoRepository.deleteById(id);
    }

}
