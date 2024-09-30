package br.com.desafiojava.granjapatos.Services;

import br.com.desafiojava.granjapatos.dtos.ClienteDTO;
import br.com.desafiojava.granjapatos.dtos.ResponsePato;
import br.com.desafiojava.granjapatos.dtos.ResponseVenda;
import br.com.desafiojava.granjapatos.dtos.VendaDTO;
import br.com.desafiojava.granjapatos.enums.StatusPato;
import br.com.desafiojava.granjapatos.enums.TipoCliente;
import br.com.desafiojava.granjapatos.exceptions.ClienteNotFoundException;
import br.com.desafiojava.granjapatos.exceptions.PatoNotFoundException;
import br.com.desafiojava.granjapatos.exceptions.VendaNotFoundException;
import br.com.desafiojava.granjapatos.model.cliente.Cliente;
import br.com.desafiojava.granjapatos.model.pato.Pato;
import br.com.desafiojava.granjapatos.model.venda.LongPato;
import br.com.desafiojava.granjapatos.model.venda.Venda;
import br.com.desafiojava.granjapatos.repositories.ClienteRepository;
import br.com.desafiojava.granjapatos.repositories.PatoRepository;
import br.com.desafiojava.granjapatos.repositories.VendaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;

    private final ClienteRepository clienteRepository;

    private final PatoRepository patoRepository;

    private final ModelMapper modelMapper;

    public ResponseVenda cadastrarVenda(VendaDTO dto) throws Exception {
        BigDecimal soma = BigDecimal.ZERO;
        List<LongPato> patosIds;
        List<Pato> patos = new ArrayList<>();
        Optional<Cliente> OptionalCliente = clienteRepository.findClienteById(dto.getCliente_id());

        if(OptionalCliente.isPresent()) {

            Cliente cliente = OptionalCliente.get();
            if (!cliente.getAtivo()){
                throw new Exception("O cliente "+cliente.getNome()+" está inativo.");
            }

            dto.setCliente(cliente);
            dto.setTipoCliente(cliente.getTipo());

            patosIds = dto.getPatosIds();
            for (LongPato patosList : patosIds) {
                Optional<Pato> OptionalPato = patoRepository.findPatoById(patosList.id());
                if(OptionalPato.isPresent()) {
                    Pato pato = OptionalPato.get();
                    if (pato.getStatus() == StatusPato.DISPONIVEL){
                        soma = soma.add(pato.getValor());
                        patos.add(pato);
                    } else {
                        throw new Exception("Pato do id "+pato.getId()+" já esta vendido.");
                    }
                } else {
                    throw new PatoNotFoundException("Pato de id "+patosList.id()+" não foi encontrado.");
                }
            }

            dto.setPatos(patos);
            dto.setValor(soma);

            if (dto.getTipoCliente() == TipoCliente.COM_DESCONTO)
                dto.setValor(dto.getValor().subtract(dto.getValor().multiply(BigDecimal.valueOf(0.2))));

            Venda novaVenda = modelMapper.map(dto, Venda.class);
            novaVenda.setData(LocalDateTime.now().toLocalDate().atStartOfDay());
            vendaRepository.save(novaVenda);

            for (LongPato patosList : patosIds) {
                Optional<Pato> OptionalPato = patoRepository.findPatoById(patosList.id());
                if(OptionalPato.isPresent()) {
                    Pato pato = OptionalPato.get();
                    pato.setVenda(novaVenda);
                    pato.setStatus(StatusPato.VENDIDO);
                } else {
                    throw new PatoNotFoundException("Pato de id "+patosList.id()+" não foi encontrado.");
                }
            }

            return createResponse(novaVenda);
        } else {
            throw new ClienteNotFoundException("Cliente de id "+dto.getCliente_id()+" não foi encontrado.");
        }
//        Venda venda = modelMapper.map(dto, Venda.class);
//        vendaRepository.save(venda);
//        return modelMapper.map(venda, VendaDTO.class);
    }

    public ResponseVenda createResponse(Venda venda) {
        ResponseVenda responseVenda = new ResponseVenda();
        ClienteDTO responseCliente = new ClienteDTO();
        List<ResponsePato> responsePatoList = new ArrayList<>();
        List<Pato> patos = venda.getPatos();

        for (Pato pato : patos){

            Optional<Pato> OptionalPato = patoRepository.findPatoById(pato.getId());
            if(OptionalPato.isPresent()) {
                Pato patoList = OptionalPato.get();
                ResponsePato responsePato = new ResponsePato();

                if (patoList.getMae() != null)
                    responsePato.setMae_id(patoList.getMae().getId());

                responsePato.setId(patoList.getId());
                responsePato.setTipo(patoList.getTipo());
                responsePato.setVenda_id(patoList.getVenda().getId());
                responsePato.setValor(patoList.getValor());
                responsePato.setStatus(patoList.getStatus());

                responsePatoList.add(responsePato);
            }
        }

        responseCliente.setId(venda.getCliente().getId());
        responseCliente.setNome(venda.getCliente().getNome());
        responseCliente.setTipo(venda.getTipoCliente());
        responseCliente.setAtivo(venda.getCliente().getAtivo());

        responseVenda.setId(venda.getId());
        responseVenda.setCliente(responseCliente);
        responseVenda.setValor(venda.getValor());
        responseVenda.setPatos(responsePatoList);

        return responseVenda;
    }

    public List<VendaDTO> buscarVendas() {
        return vendaRepository.findAll().stream().map(v -> modelMapper.map(v, VendaDTO.class))
                .collect(Collectors.toList());
    }

    public VendaDTO buscarVendaPorId(Long id) {
        Venda venda = vendaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return modelMapper.map(venda, VendaDTO.class);
    }

    public ResponseVenda atualizarVenda(@Valid VendaDTO dto, Long id) throws Exception {
        BigDecimal soma = BigDecimal.ZERO;
        List<LongPato> patosIds;
        List<Pato> patos = new ArrayList<>();

        Optional<Venda> optionalVenda = this.vendaRepository.findById(id);
        if(optionalVenda.isPresent()){
            Venda venda = optionalVenda.get();

            if (dto.getCliente_id() != null) {
                Optional<Cliente> OptionalCliente = clienteRepository.findClienteById(dto.getCliente_id());
                if(OptionalCliente.isPresent()) {
                    Cliente cliente = OptionalCliente.get();
                    if (!cliente.getAtivo()){
                        throw new Exception("O cliente "+cliente.getNome()+" está inativo.");
                    }

                    venda.setCliente(cliente);
                    venda.setTipoCliente(cliente.getTipo());

                    if (cliente.getTipo() == TipoCliente.COM_DESCONTO)
                        venda.setValor(venda.getValor().subtract(venda.getValor().multiply(BigDecimal.valueOf(0.2))));
                } else {
                    throw new ClienteNotFoundException("Cliente de id "+dto.getCliente_id()+" não foi encontrado.");
                }
            }

            if (dto.getPatosIds() != null) {

                patosIds = dto.getPatosIds();
                for (LongPato idPato : patosIds) {
                    Optional<Pato> OptionalPato = patoRepository.findPatoById(idPato.id());
                    if(OptionalPato.isPresent()) {
                        Pato pato = OptionalPato.get();

                        if (pato.getStatus() == StatusPato.DISPONIVEL) {
                            soma = soma.add(pato.getValor());
                            patos.add(pato);
                        } else {
                            throw new Exception("Pato do id "+pato.getId()+" já está vendido.");
                        }
                    } else {
                        throw new PatoNotFoundException("Pato de id "+idPato.id()+" não foi encontrado.");
                    }
                }
                // desvincula os patos que estavam vinculados a venda e muda status para DISPONIVEL novamente
                List<Pato> patosOld = venda.getPatos();
                for (Pato patoOld: patosOld){
                    patoOld.setVenda(null);
                    patoOld.setStatus(StatusPato.DISPONIVEL);
                }

                venda.setPatos(patos);
                venda.setValor(soma);

                if (venda.getTipoCliente() == TipoCliente.COM_DESCONTO)
                    venda.setValor(venda.getValor().subtract(venda.getValor().multiply(BigDecimal.valueOf(0.2))));

                // vincula e novos patos para a venda e muda status para VENDIDO
                for (LongPato idPato : patosIds) {
                    Optional<Pato> OptionalPato = patoRepository.findPatoById(idPato.id());
                    if(OptionalPato.isPresent()) {
                        Pato pato = OptionalPato.get();
                        pato.setVenda(venda);
                        pato.setStatus(StatusPato.VENDIDO);
                    } else {
                        throw new PatoNotFoundException("Pato de id "+idPato.id()+" não foi encontrado.");
                    }
                }
            }

            return createResponse(venda);
        } else {
            throw new VendaNotFoundException("Venda de id "+id+" não foi encontrada.");
        }
    }

    public void excluirVenda(@NotNull Long id) {
        vendaRepository.deleteById(id);
    }
}
