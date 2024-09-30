package br.com.desafiojava.granjapatos.Services;

import br.com.desafiojava.granjapatos.dtos.RelatorioPato;
import br.com.desafiojava.granjapatos.enums.TipoCliente;
import br.com.desafiojava.granjapatos.model.pato.Pato;
import br.com.desafiojava.granjapatos.model.venda.Venda;
import br.com.desafiojava.granjapatos.repositories.PatoRepository;
import br.com.desafiojava.granjapatos.repositories.VendaRepository;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RelatorioExcelPatoService {

    @Autowired
    private PatoRepository patoRepository;

    @Autowired
    private VendaRepository vendaRepository;

    public ByteArrayOutputStream generateReport() throws IOException {
        List<RelatorioPato> relatorioPatos = createRelatorioPatos();

        //XSSFWorkbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Patos Report");

        //Header
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Nome", "Status", "Valor", "Cliente", "Tipo Cliente", "Filhos"};
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowNum = 1;

        //Dados da tabela
        for (RelatorioPato pato : relatorioPatos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(pato.getId());
            row.createCell(1).setCellValue(pato.getNome());
            row.createCell(2).setCellValue(pato.getStatus());
            row.createCell(3).setCellValue(pato.getValor());
            row.createCell(4).setCellValue(pato.getCliente());
            row.createCell(5).setCellValue(pato.getTipo_cliente());
            row.createCell(6).setCellValue(pato.getFilhos() != null ? pato.getFilhos().size() : 0);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream;
    }

    private List<RelatorioPato> createRelatorioPatos() {
        List<RelatorioPato> relatorioPatos = new ArrayList<>();
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<Pato> patos = this.patoRepository.findAll(sort);

        for (Pato pato : patos) {
            //verifica se pato tem filhos
            List<RelatorioPato> patosFilhos = new ArrayList<>();
            Optional<List<Pato>> optFilhos = this.patoRepository.findPatoFilhosByMae(pato);
            if (optFilhos.isPresent()) {
                List<Pato> filhos = optFilhos.get();
                for (Pato filho : filhos) {
                    RelatorioPato patoReportFilho = new RelatorioPato();
                    patoReportFilho.setId(filho.getId());
                    patoReportFilho.setNome(filho.getNome());
                    patoReportFilho.setStatus(filho.getStatus().toString());
                    //valor sem desconto
                    patoReportFilho.setValor(filho.getValor().toString());

                    Optional<List<Pato>> optFilhos2 = this.patoRepository.findPatoFilhosByMae(filho);
                    if (optFilhos2.isPresent()) {
                        List<RelatorioPato> patosFilhos2 = new ArrayList<>();
                        List<Pato> filhos2 = optFilhos2.get();
                        for (Pato filho2 : filhos2) {
                            RelatorioPato patoReportFilho2 = new RelatorioPato();
                            patoReportFilho2.setId(filho2.getId());
                            patoReportFilho2.setNome(filho2.getNome());
                            patoReportFilho2.setStatus(filho2.getStatus().toString());
                            //valor sem desconto
                            patoReportFilho.setValor(filho.getValor().toString());

                            if (filho2.getVenda() != null) {
                                Optional<Venda> OptVenda = vendaRepository.findVendaById(filho2.getVenda().getId());
                                if (OptVenda.isPresent()) {
                                    Venda venda = OptVenda.get();
                                    patoReportFilho2.setCliente(venda.getCliente().getNome());
                                    patoReportFilho2.setTipo_cliente(venda.getTipoCliente().toString());
                                    //patoReportFilho2.setValor("R$ " + venda.getValor().toString());
                                }
                            }
                            patosFilhos2.add(patoReportFilho2);
                        }
                        patoReportFilho.setFilhos(patosFilhos2);
                    }

                    if (filho.getVenda() != null) {
                        Optional<Venda> OptVenda = vendaRepository.findVendaById(filho.getVenda().getId());
                        if (OptVenda.isPresent()) {
                            Venda venda = OptVenda.get();
                            patoReportFilho.setCliente(venda.getCliente().getNome());
                            patoReportFilho.setTipo_cliente(venda.getTipoCliente().toString());
                            //patoReportFilho.setValor("R$ " + venda.getValor().toString());
                        }
                    }

                    patosFilhos.add(patoReportFilho);
                }
            }
            RelatorioPato patoReport = new RelatorioPato();
            patoReport.setFilhos(patosFilhos);
            patoReport.setId(pato.getId());
            patoReport.setNome(pato.getNome());
            patoReport.setStatus(pato.getStatus().toString());
            //valor sem desconto
            patoReport.setValor("R$ "+pato.getValor().toString());

            if (pato.getVenda() != null) {
                Optional<Venda> OptVenda = vendaRepository.findVendaById(pato.getVenda().getId());
                if (OptVenda.isPresent()) {
                    Venda venda = OptVenda.get();
                    patoReport.setCliente(venda.getCliente().getNome());
                    patoReport.setTipo_cliente(venda.getTipoCliente().toString());
                    //Calculo desconto para valor de patos individualmente
                    if (venda.getTipoCliente().equals(TipoCliente.COM_DESCONTO)) {
                        String valorComDesconto = pato.getValor().subtract(venda.getValor().multiply(BigDecimal.valueOf(0.2))).toString();
                        patoReport.setValor("R$ " + valorComDesconto.substring(0, valorComDesconto.length() - 1));
                    } else if (venda.getTipoCliente().equals(TipoCliente.SEM_DESCONTO)) {
                        String valorSemDesconto = pato.getValor().toString();
                        patoReport.setValor("R$ " + valorSemDesconto);
                    } else if (pato.getValor() == null) {
                        patoReport.setValor("-");
                    }
                }
            }else{
                patoReport.setCliente("-");
                patoReport.setTipo_cliente("-");
            }
            relatorioPatos.add(patoReport);
        }
        return relatorioPatos;
    }
}

