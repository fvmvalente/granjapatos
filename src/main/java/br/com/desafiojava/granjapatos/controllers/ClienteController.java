package br.com.desafiojava.granjapatos.controllers;

import br.com.desafiojava.granjapatos.Services.ClienteService;
import br.com.desafiojava.granjapatos.dtos.ClienteDTO;
import br.com.desafiojava.granjapatos.dtos.ClienteUpdateDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @Transactional
    public ResponseEntity<ClienteDTO> cadastrarCliente(@RequestBody @Valid ClienteDTO dto, UriComponentsBuilder builder) throws Exception{
        ClienteDTO clienteDTO = clienteService.cadastrarCliente(dto);
        URI endereco = builder.path("/cliente/{id}").buildAndExpand(clienteDTO.getId()).toUri();
        return ResponseEntity.created(endereco).body(clienteDTO);
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> buscarClientes(){
        List<ClienteDTO> clientesDTO = clienteService.buscarClientes();
        return ResponseEntity.ok(clientesDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscarClientePorId(@PathVariable @NotNull Long id){
        ClienteDTO clienteDTO = clienteService.buscarClientePorId(id);
        return ResponseEntity.ok(clienteDTO);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ClienteUpdateDTO> atualizarCliente(@RequestBody ClienteUpdateDTO dto, @PathVariable @NotNull Long id) throws Exception {
        ClienteUpdateDTO clienteUpdateDTO = clienteService.atualizarCliente(dto, id);
        return ResponseEntity.ok(clienteUpdateDTO);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluirCliente(@PathVariable @NotNull Long id){
        clienteService.excluirCliente(id);
        return ResponseEntity.noContent().build();
    }
}
