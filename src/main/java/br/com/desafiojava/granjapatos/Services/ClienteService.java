package br.com.desafiojava.granjapatos.Services;

import br.com.desafiojava.granjapatos.dtos.ClienteDTO;
import br.com.desafiojava.granjapatos.dtos.ClienteUpdateDTO;
import br.com.desafiojava.granjapatos.enums.TipoCliente;
import br.com.desafiojava.granjapatos.exceptions.ClienteNotFoundException;
import br.com.desafiojava.granjapatos.model.cliente.Cliente;
import br.com.desafiojava.granjapatos.repositories.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    private final ModelMapper modelMapper;

    public ClienteDTO cadastrarCliente(ClienteDTO dto) throws Exception {
        if ((dto.getTipo() != TipoCliente.COM_DESCONTO) && (dto.getTipo() != TipoCliente.SEM_DESCONTO)){
            throw new Exception("Tipo de cliente informado inválido, tipos aceitos: COM_DESCONTO SEM_DESCONTO");
        }

        Cliente cliente = modelMapper.map(dto, Cliente.class);
        clienteRepository.save(cliente);
        return modelMapper.map(cliente, ClienteDTO.class);
    }

    public List<ClienteDTO> buscarClientes() {
        return clienteRepository.findAll().stream().map(c -> modelMapper.map(c , ClienteDTO.class))
                .collect(Collectors.toList());
    }

    public ClienteDTO buscarClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return modelMapper.map(cliente, ClienteDTO.class);
    }

    public ClienteUpdateDTO atualizarCliente(ClienteUpdateDTO dto, Long id) throws Exception {
        Optional<Cliente> optCliente = this.clienteRepository.findClienteById(id);
        if(optCliente.isPresent()){
            Cliente cliente = optCliente.get();
            if (dto.getNome() != null)
                cliente.setNome(dto.getNome());
            if (dto.getTipo() != null) {
                if ((dto.getTipo() != TipoCliente.COM_DESCONTO) && (dto.getTipo() != TipoCliente.SEM_DESCONTO)){
                    throw new Exception("Tipo informado inválido, tipo aceito COM_DESCONTO SEM_DESCONTO");
                }
                cliente.setTipo(dto.getTipo());
            }
            if (dto.getAtivo() != null) {
                cliente.setAtivo(dto.getAtivo());
            }
            return modelMapper.map(cliente, ClienteUpdateDTO.class);
        } else {
            throw new ClienteNotFoundException("Cliente de id "+id+" não foi encontrado.");
        }
    }

    public void excluirCliente(@NotNull Long id) {
        clienteRepository.deleteById(id);
    }
}
