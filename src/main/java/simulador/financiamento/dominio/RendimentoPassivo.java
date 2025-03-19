package simulador.financiamento.dominio;

import lombok.Getter;
import simulador.financiamento.utils.Constants;
import simulador.financiamento.utils.Conversor;

import java.io.Serializable;

@Getter
public class RendimentoPassivo implements Serializable {
    private final Double rendimentoAnual;
    private final Integer prazoResgateAnos;
    private final Integer prazoResgateMeses;
    private final AcoesResgate acao;
    private final Double impostoRenda = Constants.IMPOSTO_DE_RENDA;

    private final Double rendimentoMensal;

    //Valor que é atualizado mensalmente
    private Double valorInvestido;

    //Valor inicial do investimento em cada aporte. Muda caso seja selecionada a opção Reinvestir.
    private Double valorInicialAportado;

    private final Double valorInicialPrimeiroAporte;

    public RendimentoPassivo(Double valorInvestido, Double rendimentoAnual, Integer resgate, String acao) {
        this.valorInvestido = valorInvestido;
        this.valorInicialAportado = valorInvestido;
        this.valorInicialPrimeiroAporte = valorInvestido;
        this.rendimentoAnual = rendimentoAnual;
        this.rendimentoMensal = Conversor.converterTaxaAnualParaMensal(rendimentoAnual);
        this.prazoResgateAnos = resgate;
        this.prazoResgateMeses = resgate*12;
        this.acao = AcoesResgate.getByName(acao);
    }

    public Double resgatar() {

        var lucroLiquido = (valorInvestido - valorInicialAportado) * (1 - impostoRenda);

        if (AcoesResgate.AMORTIZAR.equals(acao)) {
            this.valorInvestido = valorInicialAportado;
            return lucroLiquido;
        }

        if (AcoesResgate.REINVESTIR.equals(acao)) {
            var valorParaReinvestir = valorInicialAportado + lucroLiquido;
            this.valorInicialAportado = valorParaReinvestir;
            this.valorInvestido = valorParaReinvestir;
            return 0.0;
        }

        return null;
    }

    public void calcularMes() {
        valorInvestido = valorInvestido + valorInvestido*rendimentoMensal;
    }
}
