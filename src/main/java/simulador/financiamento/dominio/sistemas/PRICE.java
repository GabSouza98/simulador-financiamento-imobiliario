package simulador.financiamento.dominio.sistemas;

import lombok.Getter;
import simulador.financiamento.dominio.*;
import simulador.financiamento.dominio.amortizacao.FGTS;
import simulador.financiamento.dominio.amortizacao.Investimentos;
import simulador.financiamento.dominio.amortizacao.Recorrencia;
import simulador.financiamento.dominio.enums.SistemaAmortizacaoEnum;
import simulador.financiamento.utils.RendaCerta;

@Getter
public class PRICE extends SistemaAmortizacao {

    private final Double parcelaConstante;

    public PRICE(Financiamento financiamento,
                 Recorrencia recorrencia,
                 Investimentos investimentos,
                 FGTS fgts,
                 OpcoesAvancadas opcoesAvancadas) {

        super(financiamento, recorrencia, investimentos, fgts, opcoesAvancadas);

        parcela = valorFinanciado / RendaCerta.calcularTermoExponencial(taxaJurosMensal, prazo);
        parcelaConstante = parcela;
    }

    @Override
    public SistemaAmortizacaoEnum getSistemaAmortizacao() {
        return SistemaAmortizacaoEnum.PRICE;
    }

    @Override
    public void atualizarParcela() {
        //No modelo PRICE a parcela é constante.
        parcela = parcelaConstante;
    }

    @Override
    public void atualizarAmortizacao() {
        //A = P - J
        amortizacaoMensal = parcela - jurosMensal;
    }
}
