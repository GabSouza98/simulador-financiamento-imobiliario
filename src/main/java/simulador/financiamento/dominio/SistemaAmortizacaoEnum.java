package simulador.financiamento.dominio;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SistemaAmortizacaoEnum {

    PRICE("PRICE", 0),
    SAC("SAC", 1);

    private final String nome;
    private final int index;

}
