package simulador.financiamento.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import simulador.financiamento.dominio.sistemas.SistemaAmortizacao;
import simulador.financiamento.utils.Constants;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.ArrayList;
import java.util.Locale;

public class CompararSimulacoes extends JFrame {
    private JPanel compararSimulacoesPanel;
    private JLabel compararSimulacoesLabel;
    private JPanel graficosPanel;
    private JPanel tablePanel;
    private JButton graficoIndividualButton;
    private JButton graficoCombinadoButton;

    public CompararSimulacoes(ArrayList<SistemaAmortizacao> simulationsList) {
        setContentPane(compararSimulacoesPanel);
        setTitle("Comparar Simulações");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        JTable jTable = getComparisonJTable(simulationsList);
        JScrollPane scrollPane = new JScrollPane(jTable);

        tablePanel.removeAll();
        tablePanel.add(scrollPane);
        tablePanel.revalidate();
        tablePanel.repaint();

        graficoIndividualButton.addActionListener(e -> new GraficoIndividual(simulationsList));
        graficoCombinadoButton.addActionListener(e -> new GraficoMultiplo(simulationsList));
    }

    private JTable getComparisonJTable(ArrayList<SistemaAmortizacao> simulationsList) {
        // Column Names
        final String[] columnNames = Constants.COMPARISON_COLUMN_NAMES;

        String[][] data = new String[simulationsList.size()][columnNames.length];

        for (int i = 0; i < simulationsList.size(); i++) {
            String[] row = new String[columnNames.length];

            SistemaAmortizacao sistemaAmortizacao = simulationsList.get(i);

            row[0] = sistemaAmortizacao.getNomeFinanciamento();
            row[1] = sistemaAmortizacao.getNumeroParcelas() + "/" + sistemaAmortizacao.getPrazo();
            row[2] = String.format("%.2f%%", sistemaAmortizacao.getJurosAnual());
            row[3] = String.format("%,.0f", sistemaAmortizacao.getValorImovel());
            row[4] = String.format("%,.0f", sistemaAmortizacao.getEntrada());
            row[5] = String.format("%,.0f", sistemaAmortizacao.getValorPagoTotal());
            row[6] = String.format("%.2f", sistemaAmortizacao.getValorPagoTotal() / sistemaAmortizacao.getValorImovel());
            row[7] = String.format("%,.0f", sistemaAmortizacao.getOpcoesAvancadas().getValorImovelValorizado());
            row[8] = String.format("%,.0f", sistemaAmortizacao.getOpcoesAvancadas().getValorImovelInflacao());

            data[i] = row;
        }

        Class[] types = new Class[columnNames.length];

        for (int i = 0; i < columnNames.length; i++) {
            types[i] = String.class;
        }

        var jTable = new JTable();
        jTable.setShowHorizontalLines(true);
        jTable.setShowVerticalLines(true);
        jTable.setModel(
                new DefaultTableModel(data, columnNames) {

                    final boolean[] canEdit = new boolean[]{false, false, false, false, false, false, false, false, false};

                    @Override
                    public Class getColumnClass(int columnIndex) {
                        return types[columnIndex];
                    }

                    @Override
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit[columnIndex];
                    }
                }
        );

//        jTable.setMinimumSize(new Dimension(700, 200));
        jTable.setPreferredScrollableViewportSize(new Dimension(1000, 290));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        jTable.setDefaultRenderer(String.class, centerRenderer);
        return jTable;
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        compararSimulacoesPanel = new JPanel();
        compararSimulacoesPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        compararSimulacoesPanel.setMinimumSize(new Dimension(1100, 420));
        compararSimulacoesPanel.setPreferredSize(new Dimension(1100, 420));
        compararSimulacoesLabel = new JLabel();
        Font compararSimulacoesLabelFont = this.$$$getFont$$$(null, Font.BOLD, 16, compararSimulacoesLabel.getFont());
        if (compararSimulacoesLabelFont != null) compararSimulacoesLabel.setFont(compararSimulacoesLabelFont);
        compararSimulacoesLabel.setHorizontalAlignment(0);
        compararSimulacoesLabel.setHorizontalTextPosition(0);
        compararSimulacoesLabel.setText("Comparar Simulações");
        compararSimulacoesPanel.add(compararSimulacoesLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        graficosPanel = new JPanel();
        graficosPanel.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        compararSimulacoesPanel.add(graficosPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 30), null, null, 0, false));
        graficoIndividualButton = new JButton();
        graficoIndividualButton.setText("Gráfico Individual");
        graficoIndividualButton.setToolTipText("Permite visualizar um gráfico de cada vez para as simulações realizadas.");
        graficosPanel.add(graficoIndividualButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        graficoCombinadoButton = new JButton();
        graficoCombinadoButton.setText("Gráfico Combinado");
        graficoCombinadoButton.setToolTipText("Permite visualizar diferentes gráficos simultaneamente para as simulações realizadas.");
        graficosPanel.add(graficoCombinadoButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        graficosPanel.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        graficosPanel.add(spacer2, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        tablePanel = new JPanel();
        tablePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        compararSimulacoesPanel.add(tablePanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 300), new Dimension(-1, 300), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return compararSimulacoesPanel;
    }

}
