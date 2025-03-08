package simulador.financiamento;

import com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme;
import simulador.financiamento.forms.PriceCalculatorGUI;

import javax.swing.*;

public class MainSwing {

    public static void main(String[] args) {

        FlatArcDarkOrangeIJTheme.setup();

        SwingUtilities.invokeLater(() -> {
            PriceCalculatorGUI priceCalculatorGUI = new PriceCalculatorGUI();
            priceCalculatorGUI.setVisible(true);
        });
    }
}
