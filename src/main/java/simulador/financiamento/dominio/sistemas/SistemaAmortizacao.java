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

        System.out.println("Calculado com sucesso");
    }

    private Double calcularMes() {
        numeroParcelas++;

        amortizacaoExtraList.forEach(AmortizacaoExtra::calcularMes);

        //J = SD * i
        jurosMensal = saldoDevedor * taxaJurosMensal;

        atualizarParcela();
        atualizarAmortizacao();

        valorPagoMensal = parcela;

        amortizacaoExtraList.forEach(amortizacaoExtra -> {
            if (amortizacaoExtra.deveAmortizar(numeroParcelas)) {
                var valorExtra = amortizacaoExtra.amortizar();
                valorPagoMensal += valorExtra;
                amortizacaoMensal += valorExtra;
            }
        });

        saldoDevedor = saldoDevedor - amortizacaoMensal;

        checarSaldoDevedorNegativo();

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
        tabela.add(String.format(Locale.US, "%d,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f", 0, 0.0, 0.0, 0.0, 0.0, 0.0, saldoDevedor));
    }

    private void checarSaldoDevedorNegativo() {
        if (saldoDevedor < 0) {
            //Caso o saldo devedor fique negativo, o valorPagoMensal deve ser apenas o necessário para zerar o saldo devedor.
            valorPagoMensal = valorPagoMensal + saldoDevedor;
            amortizacaoMensal = amortizacaoMensal + saldoDevedor;
            saldoDevedor = 0.0;
            parcela = amortizacaoMensal + jurosMensal;
        }
    }

    private void atualizarCampos() {
        //System.out.println(String.format("Parcela %d | Saldo Devedor: R$ %.2f | Valor Parcela: R$ %.2f | Valor Extra: R$ %.2f | Valor Pago Mensal: R$ %.2f", numeroParcelas, saldoDevedor, parcela, valorExtra, valorPagoMensal));
        this.valorPagoMensalList.add(valorPagoMensal);
        this.tabela.add(String.format(Locale.US, "%d,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f",
                numeroParcelas, amortizacaoMensal, jurosMensal, parcela, valorPagoMensal - parcela, valorPagoMensal, saldoDevedor));
    }

    protected abstract void atualizarParcela();
    protected abstract void atualizarAmortizacao();
    public abstract SistemaAmortizacaoEnum getSistemaAmortizacao();

}
