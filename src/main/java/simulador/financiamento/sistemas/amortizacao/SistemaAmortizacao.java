package simulador.financiamento.sistemas.amortizacao;

import lombok.Getter;
import simulador.financiamento.ExcelWriter;
import simulador.financiamento.FGTS;
import simulador.financiamento.RendimentoPassivo;
import simulador.financiamento.tabela.TableRow;
import simulador.financiamento.utils.Conversor;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Getter
public abstract class SistemaAmortizacao {

    protected final String nomeFinanciamento;
    protected final Double valorImovel;
    protected final Double percentualEntrada;
    protected final Double jurosAnual;
    protected final Double valorParcela;
    protected final Double valorExtraInicial;
    protected final Double percentProximoValorExtra;
    protected final Double valorExtraMinimo;
    protected final ExcelWriter excelWriter = new ExcelWriter();

    protected Double jurosMensal;
    protected Double entrada;
    protected Double saldoDevedor;
    protected Integer numeroParcelas = 0;
    protected Double valorExtra;

    protected RendimentoPassivo rendimentoPassivo;
    protected FGTS fgts;

    protected List<Double> valorPagoMensal = new ArrayList<>();
    protected List<String> tabela = new ArrayList<>();
    protected List<TableRow> linhasTabela = new ArrayList<>();

    public SistemaAmortizacao(String nomeFinanciamento, Double valorImovel, Double percentualEntrada, Double jurosAnual,
                 Double valorParcela, Double valorExtraInicial, Double percentProximoValorExtra, Double valorExtraMinimo,
                 RendimentoPassivo rendimentoPassivo,
                 FGTS fgts) {
        this.nomeFinanciamento = nomeFinanciamento;
        this.valorImovel = valorImovel;
        this.percentualEntrada = percentualEntrada/100;
        this.jurosAnual = jurosAnual/100;
        this.valorParcela = valorParcela;
        this.valorExtraInicial = valorExtraInicial;
        this.valorExtra = valorExtraInicial;
        this.percentProximoValorExtra = percentProximoValorExtra/100;
        this.valorExtraMinimo = valorExtraMinimo;

        this.jurosMensal = Conversor.converterTaxaAnualParaMensal(jurosAnual);
        this.entrada = valorImovel*this.percentualEntrada;
        this.saldoDevedor = valorImovel - this.entrada;

        this.rendimentoPassivo = rendimentoPassivo;
        this.fgts = fgts;
        this.tabela.add(createHeaderRow());
        System.out.println(this);
    }

    private String createHeaderRow() {
        return "Num Prestacao,Parcela,Valor Extra,Valor Pago Mensal,Saldo Devedor";
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("##############################");
        sb.append(String.format("\nFinancimento: %s", nomeFinanciamento));
        sb.append(String.format("\nValor Imóvel: R$ %.2f, Valor Entrada: R$ %.2f", valorImovel, entrada));
        sb.append(String.format("\nJuros Anual: %.2f%%", jurosAnual*100));
        sb.append(String.format("\nValor Parcela: R$ %.2f, ValorExtraInicial: R$ %.2f, ValorExtraMinimo: R$ %.2f", valorParcela, valorExtraInicial, valorExtraMinimo));
        if (nonNull(rendimentoPassivo)) {
            sb.append(String.format("\nValor Investido: R$ %.2f", rendimentoPassivo.getValorInvestido()));
        }
        return sb.toString();
    }

    public double getValorPagoTotal() {
        return entrada + valorPagoMensal.stream().mapToDouble(x -> x).sum();
    }

    public abstract void calcularFinanciamento();

}
