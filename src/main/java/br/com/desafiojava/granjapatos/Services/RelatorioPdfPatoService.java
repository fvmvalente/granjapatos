package br.com.desafiojava.granjapatos.Services;

import br.com.desafiojava.granjapatos.dtos.RelatorioPato;
import br.com.desafiojava.granjapatos.enums.TipoCliente;
import br.com.desafiojava.granjapatos.model.pato.Pato;
import br.com.desafiojava.granjapatos.model.venda.Venda;
import br.com.desafiojava.granjapatos.repositories.PatoRepository;
import br.com.desafiojava.granjapatos.repositories.VendaRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

@Service
@AllArgsConstructor
public class RelatorioPdfPatoService {

    @Autowired
    private final PatoRepository patoRepository;

    @Autowired
    private final VendaRepository vendaRepository;

    public void exportToPdf(HttpServletResponse response) throws Exception {
        List<RelatorioPato> relatorioPatos = new ArrayList<>();
        relatorioPatos = createDadosRelatorio();

        File file = ResourceUtils.getFile("classpath:relatorio.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(relatorioPatos);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Felipe Valente");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }

    public List<RelatorioPato> createDadosRelatorio() {
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
