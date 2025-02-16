package simulador.financiamento.sistemas.amortizacao;

import lombok.Getter;
import simulador.financiamento.FGTS;
import simulador.financiamento.RendimentoPassivo;

@Getter
public class SAC extends SistemaAmortizacao {

    public SAC(String nomeFinanciamento, Double valorImovel, Double percentualEntrada, Double jurosAnual,
                 Double valorParcela, Double valorExtraInicial, Double percentProximoValorExtra, Double valorExtraMinimo,
                 RendimentoPassivo rendimentoPassivo,
                 FGTS fgts) {
        super(nomeFinanciamento, valorImovel, percentualEntrada, jurosAnual, valorParcela,
                valorExtraInicial, percentProximoValorExtra, valorExtraMinimo, rendimentoPassivo, fgts);
    }

    @Override
    public void calcularFinanciamento() {

    }

}
