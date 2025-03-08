package simulador.financiamento;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;
import simulador.financiamento.forms.PriceCalculatorGUI;

import javax.swing.*;

public class MainSwing {

    public static void main(String[] args) {

//        FlatLightLaf.setup();
//        FlatCarbonIJTheme.setup();
//        FlatCyanLightIJTheme.setup();
//        FlatNightOwlIJTheme.setup();
//        FlatMaterialOceanicIJTheme.setup();
//        FlatAtomOneDarkIJTheme.setup();
//        FlatArcDarkIJTheme.setup();
        FlatArcDarkOrangeIJTheme.setup();
//        FlatDarculaLaf.setup();

        // Launch the book editor form
        SwingUtilities.invokeLater(() -> {
            PriceCalculatorGUI priceCalculatorGUI = new PriceCalculatorGUI();
            priceCalculatorGUI.setVisible(true);
        });
    }
}
