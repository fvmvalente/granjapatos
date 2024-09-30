package br.com.desafiojava.granjapatos.controllers;

import br.com.desafiojava.granjapatos.Services.RelatorioPdfPatoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/relatorio")
@RequiredArgsConstructor
public class RelatorioPatoController {

    @Autowired
    private final RelatorioPdfPatoService relatorioPatoService;

    @GetMapping("/pdf")
    public void exportToPdf(HttpServletResponse response) throws Exception {
        this.relatorioPatoService.exportToPdf(response);
    }
}
