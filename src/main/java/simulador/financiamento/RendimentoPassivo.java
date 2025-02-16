package simulador.financiamento;

import lombok.Getter;

@Getter
public class RendimentoPassivo {
    private final Double valorInvestido;
    private final Double rendimentoAnual;
    private final Double impostoRenda = Constants.IMPOSTO_DE_RENDA;
    private final Double rendimentoEfetivo;

    public RendimentoPassivo(Double valorInvestido, Double rendimentoAnual) {
        this.valorInvestido = valorInvestido;
        this.rendimentoAnual = rendimentoAnual;
        this.rendimentoEfetivo = valorInvestido*(rendimentoAnual/100)*(1-impostoRenda);
    }
}
