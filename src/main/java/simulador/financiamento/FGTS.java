package simulador.financiamento;

import lombok.Getter;
import simulador.financiamento.utils.Conversor;

@Getter
public class FGTS {
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

    public static void main(String[] args) {
        var fgts1 = new FGTS(0.0, 7000.0);
        var fgts2 = new FGTS(0.0, 7000.0);

        for (int i=0; i<24; i++) {
            fgts2.calcularMes();
        }

        System.out.println("Calculo considerando rendimeneto ANUAL em 2 anos:");
        System.out.println(fgts1.getSaldoAtual()); //13641.6
        System.out.println("Calculo considerando rendimeneto MENSAL em 24 meses:");
        System.out.println(fgts2.getSaldoAtual()); //13828.17094179663
    }
}
