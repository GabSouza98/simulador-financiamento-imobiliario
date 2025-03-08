package simulador.financiamento.sistemas.amortizacao;

import lombok.Getter;
import simulador.financiamento.ExcelWriter;
import simulador.financiamento.FGTS;
import simulador.financiamento.RendimentoPassivo;
import simulador.financiamento.tabela.TableRow;
import simulador.financiamento.utils.Constants;
import simulador.financiamento.utils.Conversor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.Objects.nonNull;

@Getter
public abstract class SistemaAmortizacao {

    protected final String nomeFinanciamento;
    protected final Double valorImovel;
    protected final Double percentualEntrada;
    protected final Double jurosAnual;
    protected final Integer prazo;
    protected final Double valorExtraInicial;
    protected final Double percentProximoValorExtra;
    protected final Double valorExtraMinimo;
    protected final ExcelWriter excelWriter = new ExcelWriter();

    protected Double taxaJurosMensal;
    protected Double entrada;
    protected Double saldoDevedor;
    protected final Double valorFinanciado;
    protected Integer numeroParcelas = 0;
    protected Double valorExtra;

    protected RendimentoPassivo rendimentoPassivo;
    protected FGTS fgts;

    protected Double parcela;
    protected Double amortizacaoMensal = 0.0;
    protected Double valorPagoMensal = 0.0;
    protected Double jurosMensal = 0.0;

    protected List<Double> valorPagoMensalList = new ArrayList<>();
    protected List<String> tabela = new ArrayList<>();
    protected List<TableRow> linhasTabela = new ArrayList<>();

    public SistemaAmortizacao(String nomeFinanciamento, Double valorImovel, Double percentualEntrada, Double jurosAnual,
                              Integer prazo,
                              Double parcela, Double valorExtraInicial, Double percentProximoValorExtra, Double valorExtraMinimo,
                              RendimentoPassivo rendimentoPassivo,
                              FGTS fgts) {
        this.nomeFinanciamento = nomeFinanciamento;
        this.valorImovel = valorImovel;
        this.percentualEntrada = percentualEntrada/100;
        this.jurosAnual = jurosAnual/100;
        this.prazo = prazo;
        this.parcela = 0.0;
        this.valorExtraInicial = valorExtraInicial;
        this.valorExtra = valorExtraInicial;
        this.percentProximoValorExtra = percentProximoValorExtra/100;
        this.valorExtraMinimo = valorExtraMinimo;

        this.taxaJurosMensal = Conversor.converterTaxaAnualParaMensal(jurosAnual);
        this.entrada = valorImovel*this.percentualEntrada;
        this.saldoDevedor = valorImovel - this.entrada;
        this.valorFinanciado = valorImovel - this.entrada;

        this.rendimentoPassivo = rendimentoPassivo;
        this.fgts = fgts;
        this.tabela.add(createHeaderRow());
        System.out.println(this);
    }

    public void calcularFinanciamento() {
        calcularPrimeiroMes();

        Double saldoDevedor;
        do {
            saldoDevedor = calcularMes();
        } while (saldoDevedor > 0);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Valor pago total: R$ %.2f\n", getValorPagoTotal()));
        sb.append(String.format("Número de parcelas: %d", numeroParcelas));
        System.out.println(sb);
    }

    private String createHeaderRow() {
        return Constants.HEADER_ROW;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("##############################");
        sb.append(String.format("\nFinancimento: %s", nomeFinanciamento));
        sb.append(String.format("\nValor Imóvel: R$ %.2f, Valor Entrada: R$ %.2f", valorImovel, entrada));
        sb.append(String.format("\nJuros Anual: %.2f%%", jurosAnual*100));
        sb.append(String.format("\nValor Parcela: R$ %.2f, ValorExtraInicial: R$ %.2f, ValorExtraMinimo: R$ %.2f", parcela, valorExtraInicial, valorExtraMinimo));
        if (nonNull(rendimentoPassivo)) {
            sb.append(String.format("\nValor Investido: R$ %.2f", rendimentoPassivo.getValorInvestido()));
        }
        return sb.toString();
    }

    public double getValorPagoTotal() {
        return entrada + valorPagoMensalList.stream().mapToDouble(x -> x).sum();
    }

    protected void calcularPrimeiroMes() {
        this.valorPagoMensalList.add(0.0);
        this.tabela.add(String.format(Locale.US, "%d,%.2f,%.2f,%.2f,%.2f,%.2f", 0, 0.0, 0.0, 0.0, 0.0, saldoDevedor));
        this.linhasTabela.add(new TableRow(0, 0.0, 0.0, 0.0, saldoDevedor));
    }

    protected void atualizarValorExtra() {
        //Diminuicao do valorExtraInicial a cada mês
        valorExtra = valorExtra *  percentProximoValorExtra;

        //Caso o valor extra diminua abaixo do mínimo, usa o valorExtraMinimo
        valorExtra = Math.max(valorExtra, valorExtraMinimo);

        valorPagoMensal += valorExtra;
        amortizacaoMensal += valorExtra;
    }

    protected void atualizarRendimentoPassivo() {
        //Abate o rendimento passivo anualmente
        if (nonNull(rendimentoPassivo) && numeroParcelas % 12 == 0) {
            double rendimento = rendimentoPassivo.getRendimentoEfetivo();
            valorPagoMensal += rendimento;
            amortizacaoMensal += rendimento;
        }
    }

    protected void atualizarFgts() {
        //Abate o FGTS a cada 2 anos
        if (nonNull(fgts) && numeroParcelas % 24 == 0) {
            double rendimentoFgts = fgts.amortizar();
            valorPagoMensal += rendimentoFgts;
            amortizacaoMensal += rendimentoFgts;
        }
    }

    protected void checarSaldoDevedorNegativo() {
        if (saldoDevedor < 0) {
            //Caso o saldo devedor fique negativo, o valorPagoMensal deve ser apenas o necessário para zerar o saldo devedor.
            valorPagoMensal = valorPagoMensal + saldoDevedor;
            amortizacaoMensal = amortizacaoMensal + saldoDevedor;
            saldoDevedor = 0.0;
        }
    }

    protected void atualizarCampos() {
        //System.out.println(String.format("Parcela %d | Saldo Devedor: R$ %.2f | Valor Parcela: R$ %.2f | Valor Extra: R$ %.2f | Valor Pago Mensal: R$ %.2f", numeroParcelas, saldoDevedor, parcela, valorExtra, valorPagoMensal));
        this.valorPagoMensalList.add(valorPagoMensal);
        this.tabela.add(String.format(Locale.US, "%d,%.2f,%.2f,%.2f,%.2f,%.2f", numeroParcelas, amortizacaoMensal, parcela, valorExtra, valorPagoMensal, saldoDevedor));
        this.linhasTabela.add(new TableRow(numeroParcelas, parcela, valorExtra, valorPagoMensal, saldoDevedor));
    }

    protected abstract Double calcularMes();

}
