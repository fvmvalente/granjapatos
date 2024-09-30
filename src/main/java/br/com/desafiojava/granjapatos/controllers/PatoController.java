package br.com.desafiojava.granjapatos.controllers;

import br.com.desafiojava.granjapatos.Services.PatoService;
import br.com.desafiojava.granjapatos.dtos.PatoDTO;
import br.com.desafiojava.granjapatos.dtos.PatoUpdateDTO;
import br.com.desafiojava.granjapatos.enums.StatusPato;
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
@RequestMapping("/pato")
@RequiredArgsConstructor
public class PatoController {

    private final PatoService patoService;

    @PostMapping
    @Transactional
    public ResponseEntity<PatoDTO> cadastrarPato(@RequestBody @Valid PatoDTO dto, UriComponentsBuilder builder) throws Exception{
        PatoDTO patoDTO = patoService.cadastrarPato(dto);
        URI endereco = builder.path("/pato/{id}").buildAndExpand(patoDTO.getId()).toUri();
        return ResponseEntity.created(endereco).body(patoDTO);
    }

    @GetMapping
    public ResponseEntity<List<PatoDTO>> buscarPatos(){
        List<PatoDTO> patosDTO = patoService.buscarPatos();
        return ResponseEntity.ok(patosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatoDTO> buscarPatoPorId(@PathVariable Long id){
        PatoDTO patoDTO = patoService.buscarPatoPorId(id);
        return ResponseEntity.ok(patoDTO);
    }

    @GetMapping("/vendidos")
    public ResponseEntity<List<PatoDTO>> buscarPatosVendidos(){
        List<PatoDTO> listPatosDTO = patoService.buscarPatosPorStatus(StatusPato.VENDIDO);
        return ResponseEntity.ok(listPatosDTO);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<PatoUpdateDTO> atualizarPato(@RequestBody PatoUpdateDTO dto, @PathVariable @NotNull Long id) throws Exception {
        return ResponseEntity.ok(patoService.atualizarPato(dto, id));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluirPato(@PathVariable @NotNull Long id){
        patoService.excluirPato(id);
        return ResponseEntity.noContent().build();
    }
}
