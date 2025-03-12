package simulador.financiamento.sistemas.amortizacao;

import lombok.Getter;
import simulador.financiamento.dominio.*;

@Getter
public class SAC extends SistemaAmortizacao {

    private final Double amortizacaoConstante;

    public SAC(String nomeFinanciamento, Double valorImovel, Double percentualEntrada, Double jurosAnual,
               Integer prazo,
               AmortizacaoExtra amortizacaoExtra,
               RendimentoPassivo rendimentoPassivo,
               FGTS fgts,
               OpcoesAvancadas opcoesAvancadas) {
        super(nomeFinanciamento, valorImovel, percentualEntrada, jurosAnual, prazo,
                amortizacaoExtra, rendimentoPassivo, fgts, opcoesAvancadas);

        amortizacaoConstante = valorFinanciado / (double) prazo;
    }

    @Override
    public SistemaAmortizacaoEnum getSistemaAmortizacao() {
        return SistemaAmortizacaoEnum.SAC;
    }

    @Override
    public Double calcularMes() {
        numeroParcelas++;
        fgts.calcularMes();

        //J = SD * i
        jurosMensal = saldoDevedor * taxaJurosMensal;

        //P = A + J
        parcela = amortizacaoConstante + jurosMensal;

        valorPagoMensal = parcela;

        amortizacaoMensal = amortizacaoConstante;

        atualizarValorExtra();

        atualizarRendimentoPassivo();

        atualizarFgts();

        //SD = SD - A
        saldoDevedor = saldoDevedor - amortizacaoMensal;

        checarSaldoDevedorNegativo();

        atualizarCampos();

        return saldoDevedor;
    }

}
