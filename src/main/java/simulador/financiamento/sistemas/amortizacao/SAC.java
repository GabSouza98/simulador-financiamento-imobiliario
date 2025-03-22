package simulador.financiamento.sistemas.amortizacao;

import lombok.Getter;
import simulador.financiamento.dominio.*;

@Getter
public class SAC extends SistemaAmortizacao {

    private final Double amortizacaoConstante;

    public SAC(String nomeFinanciamento, Double valorImovel, Double percentualEntrada, Double jurosAnual,
               Integer prazo,
               Recorrencia recorrencia,
               Investimentos investimentos,
               FGTS fgts,
               OpcoesAvancadas opcoesAvancadas) {
        super(nomeFinanciamento, valorImovel, percentualEntrada, jurosAnual, prazo,
                recorrencia, investimentos, fgts, opcoesAvancadas);

        amortizacaoConstante = valorFinanciado / (double) prazo;
    }

    @Override
    public SistemaAmortizacaoEnum getSistemaAmortizacao() {
        return SistemaAmortizacaoEnum.SAC;
    }

    @Override
    public void calcularMesEspecifico() {
        //P = A + J
        parcela = amortizacaoConstante + jurosMensal;
        amortizacaoMensal = amortizacaoConstante;
    }
}
