package simulador.financiamento.sistemas.amortizacao;

import lombok.Getter;
import lombok.Setter;
import simulador.financiamento.dominio.*;
import simulador.financiamento.utils.Constants;
import simulador.financiamento.utils.Conversor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static java.util.Objects.nonNull;

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

    protected Double saldoDevedor;
    protected Integer numeroParcelas = 0;

    protected AmortizacaoExtra amortizacaoExtra;
    protected RendimentoPassivo rendimentoPassivo;
    protected FGTS fgts;
    protected OpcoesAvancadas opcoesAvancadas;

    protected Double parcela = 0.0;
    protected Double amortizacaoMensal = 0.0;
    protected Double valorPagoMensal = 0.0;
    protected Double jurosMensal = 0.0;

    protected List<Double> valorPagoMensalList = new ArrayList<>();
    protected List<String> tabela = new ArrayList<>();

    public SistemaAmortizacao(String nomeFinanciamento,
                              Double valorImovel,
                              Double percentualEntrada,
                              Double jurosAnual,
                              Integer prazo,
                              AmortizacaoExtra amortizacaoExtra,
                              RendimentoPassivo rendimentoPassivo,
                              FGTS fgts,
                              OpcoesAvancadas opcoesAvancadas) {

        //Campos obrigatórios
        this.nomeFinanciamento = nomeFinanciamento;
        this.valorImovel = valorImovel;
        this.percentualEntrada = percentualEntrada/100;
        this.jurosAnual = jurosAnual/100;
        this.prazo = prazo;

        //Inicialização de parâmetros
        this.taxaJurosMensal = Conversor.converterTaxaAnualParaMensal(jurosAnual);
        this.entrada = valorImovel * this.percentualEntrada;
        this.valorFinanciado = valorImovel - this.entrada;
        this.saldoDevedor = valorImovel - this.entrada;

        //Campos opcionais
        this.amortizacaoExtra = amortizacaoExtra;
        this.rendimentoPassivo = rendimentoPassivo;
        this.fgts = fgts;
        this.opcoesAvancadas = opcoesAvancadas;

        this.tabela.add(createHeaderRow());
        System.out.println(this);
    }

    public void calcularFinanciamento() {
        calcularPrimeiroMes();

        Double saldoDevedor;
        do {
            saldoDevedor = calcularMes();
        } while (saldoDevedor > 0.001);

        opcoesAvancadas.atualizarValorImovel(numeroParcelas);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Valor pago total: R$ %.2f\n", getValorPagoTotal()));
        sb.append(String.format("Número de parcelas: %d\n", numeroParcelas));
        sb.append(String.format("Valor Imóvel Valorizado: R$ %.2f\n", opcoesAvancadas.getValorImovelValorizado()));
        sb.append(String.format("Valor Imóvel Inflação: R$ %.2f\n", opcoesAvancadas.getValorImovelInflacao()));
        System.out.println(sb);
    }

    private String createHeaderRow() {
        return Constants.HEADER_ROW;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\nFinancimento: %s", nomeFinanciamento));
        sb.append(String.format("\nValor Imóvel: R$ %.2f, Valor Entrada: R$ %.2f", valorImovel, entrada));
        sb.append(String.format("\nJuros Anual: %.2f%%", jurosAnual*100));
        sb.append(String.format("\nValor Parcela: R$ %.2f, ValorExtraInicial: R$ %.2f, ValorExtraMinimo: R$ %.2f",
                parcela, amortizacaoExtra.getValorExtraInicial(), amortizacaoExtra.getValorExtraMinimo()));
        if (nonNull(rendimentoPassivo)) {
            sb.append(String.format("\nValor Investido: R$ %.2f", rendimentoPassivo.getValorInvestido()));
        }
        return sb.toString();
    }

    public double getValorPagoTotal() {
        return entrada + valorPagoMensalList.stream().mapToDouble(x -> x).sum();
    }

    protected void calcularPrimeiroMes() {
        opcoesAvancadas.setValorImovelInicial(valorImovel);

        valorPagoMensalList.add(0.0);
        tabela.add(String.format(Locale.US, "%d,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f", 0, 0.0, 0.0, 0.0, 0.0, 0.0, saldoDevedor));
    }

    protected void atualizarValorExtra() {
        if (numeroParcelas >= amortizacaoExtra.getMesInicial()
            && amortizacaoExtra.deveAmortizar()
            && intervaloCorreto()
        ) {

            if (amortizacaoExtra.getAmortizacoesFeitas() == 0) {
                amortizacaoExtra.setValorExtra(amortizacaoExtra.getValorExtraAnterior());
            } else {
                //Diminuição do valorExtraInicial a cada mês
                amortizacaoExtra.setValorExtra(amortizacaoExtra.getValorExtraAnterior() * amortizacaoExtra.getPercentProximoValorExtra());
            }

            //Caso o valor extra diminua abaixo do mínimo, usa o valorExtraMinimo
            amortizacaoExtra.setValorExtra(Math.max(amortizacaoExtra.getValorExtra(), amortizacaoExtra.getValorExtraMinimo()));

            valorPagoMensal += amortizacaoExtra.getValorExtra();
            amortizacaoMensal += amortizacaoExtra.getValorExtra();

            amortizacaoExtra.setValorExtraAnterior(amortizacaoExtra.getValorExtra());
            amortizacaoExtra.incrementarAmortizacoes();
        } else {
            amortizacaoExtra.setValorExtra(0.0);
        }
    }

    private boolean intervaloCorreto() {
        if (Objects.equals(amortizacaoExtra.getMesInicial(), numeroParcelas)) {
            return true;
        }

        return (numeroParcelas - amortizacaoExtra.getMesInicial()) % amortizacaoExtra.getIntervalo() == 0;
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
            parcela = amortizacaoMensal + jurosMensal;
        }
    }

    protected void atualizarCampos() {
        //System.out.println(String.format("Parcela %d | Saldo Devedor: R$ %.2f | Valor Parcela: R$ %.2f | Valor Extra: R$ %.2f | Valor Pago Mensal: R$ %.2f", numeroParcelas, saldoDevedor, parcela, valorExtra, valorPagoMensal));
        this.valorPagoMensalList.add(valorPagoMensal);
        this.tabela.add(String.format(Locale.US, "%d,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f",
                numeroParcelas, amortizacaoMensal, jurosMensal, parcela, valorPagoMensal - parcela, valorPagoMensal, saldoDevedor));
    }

    protected abstract Double calcularMes();

    public abstract SistemaAmortizacaoEnum getSistemaAmortizacao();

}
