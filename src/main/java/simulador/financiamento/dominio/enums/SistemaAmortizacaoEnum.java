package simulador.financiamento.dominio.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum SistemaAmortizacaoEnum {

    PRICE("PRICE", 0),
    SAC("SAC", 1);

    private final String nome;
    private final int index;

    public static SistemaAmortizacaoEnum getByName(String name) {
        return Arrays.stream(SistemaAmortizacaoEnum.values())
                .filter(sa -> sa.getNome().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sistema Amortização não encontrado"));
    }

}
