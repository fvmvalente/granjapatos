package br.com.desafiojava.granjapatos.controllers;

import br.com.desafiojava.granjapatos.Services.VendaService;
import br.com.desafiojava.granjapatos.dtos.ResponseVenda;
import br.com.desafiojava.granjapatos.dtos.VendaDTO;
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
@RequestMapping("/venda")
@RequiredArgsConstructor
public class VendaController {

    private final VendaService vendaService;

    @PostMapping
    @Transactional
    public ResponseEntity<ResponseVenda> cadastrarVenda(@RequestBody @Valid VendaDTO dto, UriComponentsBuilder builder) throws Exception {
        ResponseVenda responseVenda = vendaService.cadastrarVenda(dto);
        URI endereco = builder.path("/venda/{id}").buildAndExpand(responseVenda.getId()).toUri();
        return ResponseEntity.created(endereco).body(responseVenda);
    }

    @GetMapping
    public ResponseEntity<List<VendaDTO>> buscarVendas(){
        List<VendaDTO> vendasDTO = vendaService.buscarVendas();
        return ResponseEntity.ok(vendasDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaDTO> buscarVendaPorId(@PathVariable Long id){
        VendaDTO vendaDTO = vendaService.buscarVendaPorId(id);
        return ResponseEntity.ok(vendaDTO);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ResponseVenda> atualizarVenda(@RequestBody @Valid VendaDTO dto, @PathVariable @NotNull Long id) throws Exception {
        return ResponseEntity.ok(vendaService.atualizarVenda(dto, id));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluirVenda(@PathVariable @NotNull Long id){
        vendaService.excluirVenda(id);
        return ResponseEntity.noContent().build();
    }
}
