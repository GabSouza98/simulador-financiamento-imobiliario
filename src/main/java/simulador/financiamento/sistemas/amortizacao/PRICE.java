package simulador.financiamento.sistemas.amortizacao;

import lombok.Getter;
import simulador.financiamento.dominio.*;
import simulador.financiamento.utils.RendaCerta;

@Getter
public class PRICE extends SistemaAmortizacao {

    private final Double parcelaConstante;

    public PRICE(String nomeFinanciamento, Double valorImovel, Double percentualEntrada, Double jurosAnual,
                 Integer prazo, Recorrencia recorrencia,
                 Investimentos investimentos,
                 FGTS fgts,
                 OpcoesAvancadas opcoesAvancadas) {
        super(nomeFinanciamento, valorImovel, percentualEntrada, jurosAnual, prazo,
                recorrencia, investimentos, fgts, opcoesAvancadas);

        parcela = valorFinanciado / RendaCerta.calcularTermoExponencial(taxaJurosMensal, prazo);
        parcelaConstante = parcela;
    }

    @Override
    public SistemaAmortizacaoEnum getSistemaAmortizacao() {
        return SistemaAmortizacaoEnum.PRICE;
    }

    @Override
    public void calcularMesEspecifico() {
        //A = P - J
        amortizacaoMensal = parcela - jurosMensal;
    }
}
