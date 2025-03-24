package simulador.financiamento.dominio.amortizacao;

import lombok.Getter;
import simulador.financiamento.dominio.enums.AcoesResgate;
import simulador.financiamento.utils.Constants;
import simulador.financiamento.utils.Conversor;

import java.io.Serializable;

@Getter
public class Investimentos implements AmortizacaoExtra, Serializable {
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

    public Investimentos(Double valorInvestido, Double rendimentoAnual, Integer resgate, String acao) {
        this.valorInvestido = valorInvestido;
        this.valorInicialAportado = valorInvestido;
        this.valorInicialPrimeiroAporte = valorInvestido;
        this.rendimentoAnual = rendimentoAnual;
        this.rendimentoMensal = Conversor.converterTaxaAnualParaMensal(rendimentoAnual);
        this.prazoResgateAnos = resgate;
        this.prazoResgateMeses = resgate*12;
        this.acao = AcoesResgate.getByName(acao);
    }

    @Override
    public Boolean deveAmortizar(Integer numeroParcelas) {
        return numeroParcelas % prazoResgateMeses == 0;
    }

    @Override
    public Double amortizar() {
        return resgatar();
    }

    private Double resgatar() {

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

    @Override
    public void calcularMes() {
        valorInvestido = valorInvestido + valorInvestido*rendimentoMensal;
    }
}
