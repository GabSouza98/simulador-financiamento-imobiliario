package simulador.financiamento.dominio;

import lombok.Getter;
import simulador.financiamento.utils.Constants;
import simulador.financiamento.utils.Conversor;

import java.io.Serializable;

@Getter
public class FGTS implements Serializable {
    private Double saldoAtual;
    private final Double salario;
    private final Double rendimentoAnual = Constants.RENDIMENTO_ANUAL_FGTS; //3%
    private final Double rendimentoMensal = Conversor.converterTaxaAnualParaMensal(rendimentoAnual);
    private final Double aliquotaMensal = Constants.ALIQUOTA_MENSAL_FGTS; //8%

    public FGTS(Double saldoAtual, Double salario) {
        this.saldoAtual = saldoAtual;
        this.salario = salario;
    }

    public Double amortizar() {
        var saldo = this.saldoAtual;
        this.saldoAtual = 0.0;
        return saldo;
    }

    public void calcularMes() {
        saldoAtual = saldoAtual + saldoAtual*rendimentoMensal + salario*aliquotaMensal/100;
    }
}
