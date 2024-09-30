package br.com.desafiojava.granjapatos.controllers;

import br.com.desafiojava.granjapatos.Services.RelatorioExcelPatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/relatorio")
@RequiredArgsConstructor
public class RelatorioExcelPatoController {

    @Autowired
    private RelatorioExcelPatoService excelReportService;

    @GetMapping("/excel")
    public ResponseEntity<byte[]> downloadExcelReport() {
        try {
            ByteArrayOutputStream reportStream = excelReportService.generateReport();
            byte[] content = reportStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=patos_report.xlsx");
            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
