package simulador.financiamento.dominio.sistemas;

import lombok.Getter;
import lombok.Setter;
import simulador.financiamento.dominio.*;
import simulador.financiamento.dominio.amortizacao.AmortizacaoExtra;
import simulador.financiamento.dominio.amortizacao.FGTS;
import simulador.financiamento.dominio.amortizacao.Investimentos;
import simulador.financiamento.dominio.amortizacao.Recorrencia;
import simulador.financiamento.dominio.enums.SistemaAmortizacaoEnum;
import simulador.financiamento.utils.Constants;
import simulador.financiamento.utils.Conversor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static simulador.financiamento.utils.Constants.DELIMITER;

@Getter
@Setter
public abstract class SistemaAmortizacao implements Serializable {

    protected final String nomeFinanciamento;
    protected final Double valorImovel;
    protected final Double percentualEntrada;
    protected final Double jurosAnual;
    protected final Integer prazo;
    protected final Double taxaJurosMensal;
    protected final Double entrada;
    protected final Double valorFinanciado;
    protected final List<AmortizacaoExtra> amortizacaoExtraList;

    protected Double saldoDevedor;
    protected Integer numeroParcelas = 0;

    protected OpcoesAvancadas opcoesAvancadas;

    protected Double parcela = 0.0;
    protected Double amortizacaoMensal = 0.0;
    protected Double valorPagoMensal = 0.0;
    protected Double jurosMensal = 0.0;

    protected List<Double> valorPagoMensalList = new ArrayList<>();
    protected List<String> tabela = new ArrayList<>();

    public SistemaAmortizacao(Financiamento financiamento,
                              Recorrencia recorrencia,
                              Investimentos investimentos,
                              FGTS fgts,
                              OpcoesAvancadas opcoesAvancadas) {

        //Campos obrigatórios
        this.nomeFinanciamento = financiamento.getNomeFinanciamento();
        this.valorImovel = financiamento.getValorImovel();
        this.percentualEntrada = financiamento.getPercentualEntrada()/100;
        this.jurosAnual = financiamento.getJurosAnual();
        this.prazo = financiamento.getPrazo();

        //Inicialização de parâmetros
        this.taxaJurosMensal = Conversor.converterTaxaAnualParaMensal(jurosAnual);
        this.entrada = valorImovel * this.percentualEntrada;
        this.valorFinanciado = valorImovel - this.entrada;
        this.saldoDevedor = valorImovel - this.entrada;

        this.opcoesAvancadas = opcoesAvancadas;
        this.amortizacaoExtraList = List.of(recorrencia, investimentos, fgts);

        this.tabela.add(createHeaderRow());
    }

    public void calcularFinanciamento() {
        calcularPrimeiroMes();

        Double saldoDevedor;
        do {
            saldoDevedor = calcularMes();
        } while (saldoDevedor > 0.001);

        opcoesAvancadas.atualizarValorImovel(numeroParcelas);
    }

    private Double calcularMes() {
        numeroParcelas++;

        amortizacaoExtraList.forEach(AmortizacaoExtra::calcularMes);

        //J = SD * i
        jurosMensal = saldoDevedor * taxaJurosMensal;

        atualizarParcela();
        atualizarAmortizacao();

        valorPagoMensal = parcela;

        //Até aqui a amortizacaoMensal é proveniente apenas da parcela
        saldoDevedor -= amortizacaoMensal;
        checarSaldoDevedorNegativo();

        //Só contabiliza as outras formas de amortização se o saldoDevedor ainda for positivo
        if (saldoDevedor > 0) {
            amortizacaoExtraList.forEach(amortizacaoExtra -> {
                if (amortizacaoExtra.deveAmortizar(numeroParcelas) && saldoDevedor > 0) {
                    var valorExtra = amortizacaoExtra.amortizar();
                    if (valorExtra > 0) {
                        valorPagoMensal += valorExtra;
                        amortizacaoMensal += valorExtra;
                        saldoDevedor -= valorExtra;
                        checarSaldoDevedorNegativoPorAmortizacaoExtra();
                    }
                }
            });
        }

        atualizarCampos();

        return saldoDevedor;
    }

    private String createHeaderRow() {
        return Constants.HEADER_ROW;
    }

    public double getValorPagoTotal() {
        return entrada + valorPagoMensalList.stream().mapToDouble(x -> x).sum();
    }

    private void calcularPrimeiroMes() {
        opcoesAvancadas.setValorImovelInicial(valorImovel);
        valorPagoMensalList.add(0.0);
//        tabela.add(String.format(Locale.US, "%d,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f", 0, 0.0, 0.0, 0.0, 0.0, 0.0, saldoDevedor));
        tabela.add(formatRow(0, 0.0, 0.0, 0.0, 0.0, saldoDevedor));
    }

    private void checarSaldoDevedorNegativo() {
        if (saldoDevedor < 0) {
            //Caso o saldo devedor fique negativo, o valorPagoMensal deve ser apenas o necessário para zerar o saldo devedor.
            valorPagoMensal = valorPagoMensal + saldoDevedor;
            amortizacaoMensal = amortizacaoMensal + saldoDevedor;
            saldoDevedor = 0.0;
            //Corrige a ultima parcela PRICE
            parcela = amortizacaoMensal + jurosMensal;
        }
    }

    private void checarSaldoDevedorNegativoPorAmortizacaoExtra() {
        if (saldoDevedor < 0) {
            //Caso o saldo devedor fique negativo, o valorPagoMensal deve ser apenas o necessário para zerar o saldo devedor.
            valorPagoMensal = valorPagoMensal + saldoDevedor;
            amortizacaoMensal = amortizacaoMensal + saldoDevedor;
            saldoDevedor = 0.0;
        }
    }

    private void atualizarCampos() {
        this.valorPagoMensalList.add(valorPagoMensal);
//        this.tabela.add(String.format(Locale.US, "%d,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f",
//                numeroParcelas, amortizacaoMensal, jurosMensal, parcela, valorPagoMensal - parcela, valorPagoMensal, saldoDevedor));

        this.tabela.add(formatRow(numeroParcelas, amortizacaoMensal, jurosMensal, parcela, valorPagoMensal, saldoDevedor));
    }

    private String formatRow(Integer numeroParcelas, Double amortizacaoMensal, Double jurosMensal, Double parcela, Double valorPagoMensal, Double saldoDevedor) {
        return String.format("%d", numeroParcelas).concat(DELIMITER)
                .concat(String.format("%,.2f", amortizacaoMensal)).concat(DELIMITER)
                .concat(String.format("%,.2f", jurosMensal)).concat(DELIMITER)
                .concat(String.format("%,.2f", parcela)).concat(DELIMITER)
                .concat(String.format("%,.2f", valorPagoMensal - parcela)).concat(DELIMITER)
                .concat(String.format("%,.2f", valorPagoMensal)).concat(DELIMITER)
                .concat(String.format("%,.2f", saldoDevedor));
    }

    protected abstract void atualizarParcela();
    protected abstract void atualizarAmortizacao();
    public abstract SistemaAmortizacaoEnum getSistemaAmortizacao();

}
