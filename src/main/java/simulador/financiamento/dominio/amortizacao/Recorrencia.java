package simulador.financiamento.dominio.amortizacao;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class Recorrencia implements AmortizacaoExtra, Serializable {
    private final Double valorExtraInicial;
    private final Double percentProximoValorExtra;
    private final Double valorExtraMinimo;
    private final Integer mesInicial;
    private final Integer intervalo;
    private final Integer quantidadeAmortizacoesDesejadas;
    private final Boolean semLimiteAmortizacoes;

    private Double valorExtra;
    private Double valorExtraAnterior;
    private Integer amortizacoesFeitas = 0;

    public Recorrencia(Double valorExtraInicial, Double percentProximoValorExtra, Double valorExtraMinimo,
                       Integer mesInicial, Integer intervalo, Integer quantidadeAmortizacoesDesejadas,
                       Boolean semLimiteAmortizacoes) {
        this.valorExtraInicial = valorExtraInicial;
        this.valorExtra = valorExtraInicial;
        this.valorExtraAnterior = valorExtraInicial;
        this.percentProximoValorExtra = percentProximoValorExtra/100;
        this.valorExtraMinimo = valorExtraMinimo;
        this.mesInicial = mesInicial;
        this.intervalo = intervalo;
        this.quantidadeAmortizacoesDesejadas = quantidadeAmortizacoesDesejadas;
        this.semLimiteAmortizacoes = semLimiteAmortizacoes;
    }

    @Override
    public void calcularMes() {

    }

    @Override
    public Boolean deveAmortizar(Integer numeroParcelas) {
        return numeroParcelas >= mesInicial
                && amortizacoesPendentes()
                && intervaloCorreto(numeroParcelas);
    }

    @Override
    public Double amortizar() {
        if (amortizacoesFeitas == 0) {
            valorExtra = valorExtraAnterior;
        } else {
            //Diminuição do valorExtraInicial a cada mês
            valorExtra = valorExtraAnterior * percentProximoValorExtra;
        }

        //Caso o valor extra diminua abaixo do mínimo, usa o valorExtraMinimo
        valorExtra = Math.max(valorExtra, valorExtraMinimo);

        valorExtraAnterior = valorExtra;
        amortizacoesFeitas++;

        return valorExtra;
    }

    public boolean intervaloCorreto(Integer numeroParcelas) {
        if (Objects.equals(mesInicial, numeroParcelas)) {
            return true;
        }

        return (numeroParcelas - mesInicial) % intervalo == 0;
    }

    public Boolean amortizacoesPendentes() {
        return semLimiteAmortizacoes || (amortizacoesFeitas < quantidadeAmortizacoesDesejadas);
    }
}
