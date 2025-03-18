package simulador.financiamento.dominio;

import lombok.Getter;
import simulador.financiamento.utils.Constants;
import simulador.financiamento.utils.Conversor;

import java.io.Serializable;

@Getter
public class RendimentoPassivo implements Serializable {
    private final Double rendimentoAnual;
    private final Double impostoRenda = Constants.IMPOSTO_DE_RENDA;

    private final Double rendimentoMensal;

    //Valor que é atualizado mensalmente
    private Double valorInvestido;

    //Valor inicial do investimento
    private final Double valorInicial;

    public RendimentoPassivo(Double valorInvestido, Double rendimentoAnual) {
        this.valorInvestido = valorInvestido;
        this.valorInicial = valorInvestido;
        this.rendimentoAnual = rendimentoAnual;
        this.rendimentoMensal = Conversor.converterTaxaAnualParaMensal(rendimentoAnual);
    }

    public Double amortizar() {
        var lucroLiquido = (valorInvestido - valorInicial) * (1 - impostoRenda);
        this.valorInvestido = valorInicial;
        return lucroLiquido;
    }

    public void calcularMes() {
        valorInvestido = valorInvestido + valorInvestido*rendimentoMensal;
    }
}
