package simulador.financiamento.dominio;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import simulador.financiamento.utils.Conversor;

@Getter
@Setter
@ToString
public class OpcoesAvancadas {
    private final Double inflacao;
    private final Double valorizacao;

    private final Double inflacaoMensal;
    private final Double valorizacaoMensal;

    private Double valorImovelInicial;
    private Double valorImovelValorizado;
    private Double valorImovelInflacao;

    public OpcoesAvancadas(Double inflacao, Double valorizacao) {
        this.inflacao = inflacao;
        this.valorizacao = valorizacao;

        inflacaoMensal = Conversor.converterTaxaAnualParaMensal(inflacao);
        valorizacaoMensal = Conversor.converterTaxaAnualParaMensal(valorizacao);
    }

    public void atualizarValorImovel(Integer numeroParcelas) {
        //M = C * (1+i)^n
        valorImovelInflacao = valorImovelInicial * Math.pow(1 + inflacaoMensal, numeroParcelas);
        valorImovelValorizado = valorImovelInicial * Math.pow(1 + valorizacaoMensal, numeroParcelas);
    }

}
