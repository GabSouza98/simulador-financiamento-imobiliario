package simulador.financiamento.dominio.sistemas;

import simulador.financiamento.dominio.Financiamento;
import simulador.financiamento.dominio.OpcoesAvancadas;
import simulador.financiamento.dominio.amortizacao.FGTS;
import simulador.financiamento.dominio.amortizacao.Investimentos;
import simulador.financiamento.dominio.amortizacao.Recorrencia;
import simulador.financiamento.dominio.enums.SistemaAmortizacaoEnum;

public class SistemaAmortizacaoFactory {

    public static SistemaAmortizacao getSistemaAmortizacao(SistemaAmortizacaoEnum sistema,
                                                           Financiamento financiamento,
                                                           Recorrencia recorrencia,
                                                           Investimentos investimentos,
                                                           FGTS fgts,
                                                           OpcoesAvancadas opcoesAvancadas) {

        switch (sistema) {
            case PRICE:
                return new PRICE(financiamento, recorrencia, investimentos, fgts, opcoesAvancadas);
            case SAC:
                return new SAC(financiamento, recorrencia, investimentos, fgts, opcoesAvancadas);
            default:
                throw new RuntimeException("Sistema de Amortizacao inválido");
        }
    }

}
