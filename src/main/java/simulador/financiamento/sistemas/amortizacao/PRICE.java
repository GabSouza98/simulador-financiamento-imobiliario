package simulador.financiamento.sistemas.amortizacao;

import lombok.Getter;
import simulador.financiamento.FGTS;
import simulador.financiamento.RendimentoPassivo;
import simulador.financiamento.tabela.TableRow;
import simulador.financiamento.utils.RendaCerta;

import java.util.Locale;

@Getter
public class PRICE extends SistemaAmortizacao {

    private final Double parcelaConstante;

    public PRICE(String nomeFinanciamento, Double valorImovel, Double percentualEntrada, Double jurosAnual,
                 Integer prazo, Double valorParcela, Double valorExtraInicial, Double percentProximoValorExtra, Double valorExtraMinimo,
                 RendimentoPassivo rendimentoPassivo,
                 FGTS fgts) {
        super(nomeFinanciamento, valorImovel, percentualEntrada, jurosAnual, prazo, valorParcela,
                valorExtraInicial, percentProximoValorExtra, valorExtraMinimo, rendimentoPassivo, fgts);

        parcela = valorFinanciado / RendaCerta.calcularTermoExponencial(taxaJurosMensal, prazo);
        parcelaConstante = parcela;
    }

    @Override
    public Double calcularMes() {
        numeroParcelas++;
        fgts.calcularMes();

        //J = SD * i
        jurosMensal = saldoDevedor * taxaJurosMensal;

        //A = P - J
        amortizacaoMensal = parcela - jurosMensal;

        valorPagoMensal = parcela;

        atualizarValorExtra();

        atualizarRendimentoPassivo();

        atualizarFgts();

        saldoDevedor = saldoDevedor + jurosMensal - valorPagoMensal;
        //saldoDevedor = saldoDevedor - amortizacaoMensal; //validar igualdade

        checarSaldoDevedorNegativo();

        atualizarCampos();

        return saldoDevedor;
    }
}
