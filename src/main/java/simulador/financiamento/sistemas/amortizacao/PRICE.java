package simulador.financiamento.sistemas.amortizacao;

import lombok.Getter;
import simulador.financiamento.FGTS;
import simulador.financiamento.RendimentoPassivo;
import simulador.financiamento.tabela.TableRow;

import java.util.Locale;

import static java.util.Objects.nonNull;

@Getter
public class PRICE extends SistemaAmortizacao {

    public PRICE(String nomeFinanciamento, Double valorImovel, Double percentualEntrada, Double jurosAnual,
                 Double valorParcela, Double valorExtraInicial, Double percentProximoValorExtra, Double valorExtraMinimo,
                 RendimentoPassivo rendimentoPassivo,
                 FGTS fgts) {
        super(nomeFinanciamento, valorImovel, percentualEntrada, jurosAnual, valorParcela,
                valorExtraInicial, percentProximoValorExtra, valorExtraMinimo, rendimentoPassivo, fgts);
    }

    @Override
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

    private void calcularPrimeiroMes() {
        this.valorPagoMensal.add(0.0);
        this.tabela.add(String.format(Locale.US, "%d,%.2f,%.2f,%.2f,%.2f", 0, 0.0, 0.0, 0.0, saldoDevedor));
    }

    private Double calcularMes() {
        numeroParcelas++;
        fgts.calcularMes();

        //Diminuicao do valorExtraInicial a cada mês
        valorExtra = valorExtra*percentProximoValorExtra;

        //Caso o valor extra diminui abaixo do mínimo, usa o valorExtraMinimo
        valorExtra = Math.max(valorExtra, valorExtraMinimo);

        double valorPagoMensal = valorParcela + valorExtra;

        //Abate o rendimento passivo anualmente
        if (nonNull(rendimentoPassivo) && numeroParcelas % 12 == 0) {
            valorPagoMensal += rendimentoPassivo.getRendimentoEfetivo();
        }

        //Abate o FGTS a cada 2 anos
        if (nonNull(fgts) && numeroParcelas % 24 == 0) {
            valorPagoMensal += fgts.amortizar();
        }

        saldoDevedor = saldoDevedor + saldoDevedor*jurosMensal - valorPagoMensal;

        if (saldoDevedor < 0) {
            //Caso o saldo devedor fique negativo, o valorPagoMensal deve ser apenas o necessário para zerar o saldo devedor.
            valorPagoMensal = valorPagoMensal + saldoDevedor;
            saldoDevedor = 0.0;
        }

//        System.out.println(String.format("Parcela %d | Saldo Devedor: R$ %.2f | Valor Parcela: R$ %.2f | Valor Extra: R$ %.2f | Valor Pago Mensal: R$ %.2f", numeroParcelas, saldoDevedor, valorParcela, valorExtra, valorPagoMensal));

        this.valorPagoMensal.add(valorPagoMensal);
        this.tabela.add(String.format(Locale.US, "%d,%.2f,%.2f,%.2f,%.2f", numeroParcelas, valorParcela, valorExtra, valorPagoMensal, saldoDevedor));
        this.linhasTabela.add(new TableRow(numeroParcelas, valorParcela, valorExtra, valorPagoMensal, saldoDevedor));
        return saldoDevedor;
    }
}
