package simulador.financiamento.dominio;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AmortizacaoExtra {
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

    public AmortizacaoExtra(Double valorExtraInicial, Double percentProximoValorExtra, Double valorExtraMinimo,
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

    public Boolean deveAmortizar() {
        return semLimiteAmortizacoes || (amortizacoesFeitas < quantidadeAmortizacoesDesejadas);
    }

    public void incrementarAmortizacoes() {
        amortizacoesFeitas++;
    }
}
