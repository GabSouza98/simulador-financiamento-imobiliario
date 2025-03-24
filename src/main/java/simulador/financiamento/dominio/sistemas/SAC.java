package simulador.financiamento.dominio.sistemas;

import lombok.Getter;
import simulador.financiamento.dominio.*;
import simulador.financiamento.dominio.amortizacao.FGTS;
import simulador.financiamento.dominio.amortizacao.Investimentos;
import simulador.financiamento.dominio.amortizacao.Recorrencia;
import simulador.financiamento.dominio.enums.SistemaAmortizacaoEnum;

@Getter
public class SAC extends SistemaAmortizacao {

    private final Double amortizacaoConstante;

    public SAC(Financiamento financiamento,
               Recorrencia recorrencia,
               Investimentos investimentos,
               FGTS fgts,
               OpcoesAvancadas opcoesAvancadas) {

        super(financiamento, recorrencia, investimentos, fgts, opcoesAvancadas);

        amortizacaoConstante = valorFinanciado / (double) prazo;
    }

    @Override
    public SistemaAmortizacaoEnum getSistemaAmortizacao() {
        return SistemaAmortizacaoEnum.SAC;
    }

    @Override
    public void atualizarParcela() {
        //P = A + J
        parcela = amortizacaoConstante + jurosMensal;
    }

    @Override
    public void atualizarAmortizacao() {
        amortizacaoMensal = amortizacaoConstante;
    }
}
