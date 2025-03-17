package simulador.financiamento.forms;

import com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import simulador.financiamento.dominio.*;
import simulador.financiamento.tabela.ExcelWriter;
import simulador.financiamento.sistemas.amortizacao.PRICE;
import simulador.financiamento.sistemas.amortizacao.SAC;
import simulador.financiamento.sistemas.amortizacao.SistemaAmortizacao;
import simulador.financiamento.utils.SwingUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PriceCalculatorGUI extends JFrame {

    private JPanel contentPane;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JTextField nomeFinanciamento;
    private JTextField valorImovel;
    private JSlider percentualEntrada;
    private JTextField jurosAnual;
    private JTextField valorExtraInicial;
    private JTextField percentProximoValorExtra;
    private JTextField valorExtraMinimo;
    private JTextField valorInvestido;
    private JTextField rendimentoAnual;
    private JPanel bottomLeftPanel;
    private JPanel upperLeftPanel;
    private JPanel nomeFinanciamentoPanel;
    private JPanel valorImovelPanel;
    private JPanel percentualEntradaPanel;
    private JPanel jurosAnualPanel;
    private JPanel valorExtraInicialPanel;
    private JPanel percentualProximoValorExtraPanel;
    private JPanel valorExtraMinimoPanel;
    private JPanel valorInvestidoPanel;
    private JPanel rendimentoAnualPanel;
    private JButton calcularButton;
    private JTabbedPane tabs;
    private JLabel valorPagoTotalField;
    private JLabel numeroParcelasField;
    private JLabel ratioValorPagoTotal;
    private JSplitPane verticalSplitPlane;
    private JSplitPane horizontalSplitPane;
    private JLabel nomeFinanciamentoLabel;
    private JLabel valorImovelLabel;
    private JLabel percentualEntradaLabel;
    private JLabel jurosAnualLabel;
    private JLabel valorExtraInicialLabel;
    private JLabel percentProximoValorExtraLabel;
    private JLabel valorExtraMinimoLabel;
    private JLabel valorInvestidoLabel;
    private JLabel rendimentoAnualLabel;
    private JButton changeThemeButton;
    private JButton compararSimulacoesButton;
    private JButton downloadExcelButton;
    private JPanel salarioBrutoPanel;
    private JTextField salarioBruto;
    private JPanel saldoAtualPanel;
    private JTextField saldoFGTS;
    private JPanel resetarCamposPanel;
    private JCheckBox resetarCamposBox;
    private JPanel escolherSistemaPanel;
    private JComboBox<String> sistemaAmortizacaoComboBox;
    private JPanel prazoPanel;
    private JLabel prazoLabel;
    private JTextField prazo;
    private JButton advancedOptionsButton;
    private JPanel mesInicialPanel;
    private JTextField mesInicial;
    private JLabel mesInicialLabel;
    private JPanel intervaloPanel;
    private JTextField intervalo;
    private JLabel intervaloLabel;
    private JPanel quantidadePanel;
    private JTextField quantidadePagamentosExtra;
    private JLabel amortizacaoExtra;
    private JLabel investimentosLabel;
    private JLabel fgtsLabel;
    private JCheckBox amortizacoesExtraCheckBox;
    private JCheckBox investimentosCheckBox;
    private JCheckBox fgtsCheckBox;
    private JPanel amortizacaoExtraPanel;
    private JPanel investimentosPanel;
    private JPanel fgtsPanel;
    private JCheckBox qtdOcorrenciasCheckBox;
    private JLabel anosConclusaoField;
    private JLabel valorImovelValorizado;
    private JLabel valorImovelInflacao;

    private final ExcelWriter excelWriter = new ExcelWriter();

    private boolean darkTheme = true;

    private final Map<Integer, SistemaAmortizacao> simulationsMap = new HashMap<>();

    private OpcoesAvancadas opcoesAvancadas = new OpcoesAvancadas(0.0, 0.0);

    public PriceCalculatorGUI() {

        setTitle("Simulador de Financiamentos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        getContentPane().setBackground(Color.black);

        JScrollPane jScrollPane = new JScrollPane(upperLeftPanel);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        horizontalSplitPane.setLeftComponent(jScrollPane);
        horizontalSplitPane.setDividerLocation((int) (contentPane.getPreferredSize().getHeight() * 0.61));

        pack();
        // Set the frame location to the center of the screen
        setLocationRelativeTo(null);

        defineOpcoesSistemasAmortizacao();
        defineFontes();
        desabilitaCamposAmortizacao();
        desabilitaCamposInvestimentos();
        desabilitaCamposFgts();
        preencheCamposOpcionaisDefault();

        calcularButton.addActionListener(e -> calcularFinanciamento());
        changeThemeButton.addActionListener(e -> toggleTheme());
        compararSimulacoesButton.addActionListener(e -> compararSimulacoes(simulationsMap));
        downloadExcelButton.addActionListener(e -> fazerDownload());

        advancedOptionsButton.addActionListener(e -> {
            AdvancedOptionsDialog dialog = new AdvancedOptionsDialog(opcoesAvancadas);
            dialog.setMinimumSize(new Dimension(400, 200));
            dialog.setTitle("Opções Avançadas");
            dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dialog.setLocationRelativeTo(null);
            dialog.pack();

            opcoesAvancadas = dialog.showDialog();
            System.out.println(opcoesAvancadas);
        });

        tabs.addChangeListener(e -> {
            if (!simulationsMap.isEmpty() && simulationsMap.size() == tabs.getTabCount()) {
                int selectedTab = tabs.getSelectedIndex();
                SistemaAmortizacao sistemaAmortizacao = simulationsMap.get(selectedTab);
                updateFields(sistemaAmortizacao);
            }
        });

        amortizacoesExtraCheckBox.addActionListener(e -> {
            if (amortizacoesExtraCheckBox.isSelected()) {
                habilitaCamposAmortizacao();
            } else {
                desabilitaCamposAmortizacao();
            }
        });

        investimentosCheckBox.addActionListener(e -> {
            if (investimentosCheckBox.isSelected()) {
                habilitaCamposInvestimentos();
            } else {
                desabilitaCamposInvestimentos();
            }
        });

        fgtsCheckBox.addActionListener(e -> {
            if (fgtsCheckBox.isSelected()) {
                habilitaCamposFgts();
            } else {
                desabilitaCamposFgts();
            }
        });

        qtdOcorrenciasCheckBox.addActionListener(e -> {
            quantidadePagamentosExtra.setEnabled(!qtdOcorrenciasCheckBox.isSelected());
        });

        // Set the frame visible
        setVisible(true);
    }

    private void fazerDownload() {
        if (tabs.getTabCount() > 0) {
            int selectedTab = tabs.getSelectedIndex();
            SistemaAmortizacao sistemaAmortizacao = simulationsMap.get(selectedTab);
            excelWriter.writeTable(sistemaAmortizacao);
        }
    }

    private void defineFontes() {
        calcularButton.setFont(new Font(calcularButton.getFont().getFontName(), Font.BOLD, 16));
        changeThemeButton.setFont(new Font(changeThemeButton.getFont().getFontName(), Font.PLAIN, 16));
        compararSimulacoesButton.setFont(new Font(compararSimulacoesButton.getFont().getFontName(), Font.PLAIN, 16));
        advancedOptionsButton.setFont(new Font(advancedOptionsButton.getFont().getFontName(), Font.PLAIN, 16));
        downloadExcelButton.setFont(new Font(downloadExcelButton.getFont().getFontName(), Font.PLAIN, 16));
    }

    private void defineOpcoesSistemasAmortizacao() {
        sistemaAmortizacaoComboBox.addItem(SistemaAmortizacaoEnum.PRICE.getNome());
        sistemaAmortizacaoComboBox.addItem(SistemaAmortizacaoEnum.SAC.getNome());
    }

    private void desabilitaCamposAmortizacao() {
        valorExtraInicialPanel.setVisible(false);
        percentualProximoValorExtraPanel.setVisible(false);
        valorExtraMinimoPanel.setVisible(false);
        mesInicialPanel.setVisible(false);
        intervaloPanel.setVisible(false);
        quantidadePanel.setVisible(false);
        quantidadePagamentosExtra.setEnabled(false);
    }

    private void habilitaCamposAmortizacao() {
        valorExtraInicialPanel.setVisible(true);
        percentualProximoValorExtraPanel.setVisible(true);
        valorExtraMinimoPanel.setVisible(true);
        mesInicialPanel.setVisible(true);
        intervaloPanel.setVisible(true);
        quantidadePanel.setVisible(true);
    }

    private void desabilitaCamposInvestimentos() {
        valorInvestidoPanel.setVisible(false);
        rendimentoAnualPanel.setVisible(false);
    }

    private void habilitaCamposInvestimentos() {
        valorInvestidoPanel.setVisible(true);
        rendimentoAnualPanel.setVisible(true);
    }

    private void desabilitaCamposFgts() {
        salarioBrutoPanel.setVisible(false);
        saldoAtualPanel.setVisible(false);
    }

    private void habilitaCamposFgts() {
        salarioBrutoPanel.setVisible(true);
        saldoAtualPanel.setVisible(true);
    }

    private void preencheCamposOpcionaisDefault() {
        valorExtraInicial.setText("0");
        percentProximoValorExtra.setText("100");
        valorExtraMinimo.setText("0");
        mesInicial.setText("1");
        intervalo.setText("1");
        quantidadePagamentosExtra.setText("0");

        valorInvestido.setText("0");
        rendimentoAnual.setText("0");

        salarioBruto.setText("0");
        saldoFGTS.setText("0");
    }

    private void compararSimulacoes(Map<Integer, SistemaAmortizacao> simulationsMap) {
        new CompararSimulacoes(simulationsMap);
    }

    private void toggleTheme() {
        darkTheme = !darkTheme;

        try {
            if (darkTheme) {
                UIManager.setLookAndFeel(new FlatArcDarkOrangeIJTheme());
            } else {
                UIManager.setLookAndFeel(new FlatArcOrangeIJTheme());
            }
        } catch (UnsupportedLookAndFeelException ex) {
            throw new RuntimeException(ex);
        }

        SwingUtilities.updateComponentTreeUI(this);
        pack();
    }

    private void calcularFinanciamento() {

        SistemaAmortizacao sistemaAmortizacao = getSistemaAmortizacao((String) sistemaAmortizacaoComboBox.getSelectedItem());

        sistemaAmortizacao.calcularFinanciamento();

        valorPagoTotalField.setText(String.format("Valor Pago Total: R$ %,.2f", sistemaAmortizacao.getValorPagoTotal()));
        numeroParcelasField.setText("Número de Parcelas / Prazo: " + sistemaAmortizacao.getNumeroParcelas() + "/" + sistemaAmortizacao.getPrazo());
        ratioValorPagoTotal.setText(String.format("Valor Pago Total / Valor Imovel: %.2f", sistemaAmortizacao.getValorPagoTotal() / sistemaAmortizacao.getValorImovel()));
        anosConclusaoField.setText(String.format("Quitado em: %.1f anos", (double) sistemaAmortizacao.getNumeroParcelas() / 12));
        valorImovelValorizado.setText(String.format("Valor Imóvel Valorizado: R$ %,.2f", sistemaAmortizacao.getOpcoesAvancadas().getValorImovelValorizado()));
        valorImovelInflacao.setText(String.format("Valor Imóvel Inflação: R$ %,.2f", sistemaAmortizacao.getOpcoesAvancadas().getValorImovelInflacao()));

        JTable jTable = SwingUtils.getSimulationJTable(sistemaAmortizacao);
        JScrollPane scrollPane = new JScrollPane(jTable);

        tabs.add(nomeFinanciamento.getText(), scrollPane);
        tabs.setSelectedIndex(tabs.getTabCount() - 1);

        simulationsMap.put(tabs.getSelectedIndex(), sistemaAmortizacao);

        if (resetarCamposBox.isSelected()) {
            resetFields();
        }
    }

    private SistemaAmortizacao getSistemaAmortizacao(String sistemaSelecionado) {
        SistemaAmortizacao sistemaAmortizacao;
        if (SistemaAmortizacaoEnum.PRICE.getNome().equals(sistemaSelecionado)) {
            sistemaAmortizacao = new PRICE(
                    nomeFinanciamento.getText(),
                    Double.valueOf(valorImovel.getText()),
                    (double) percentualEntrada.getValue(),
                    Double.valueOf(jurosAnual.getText()),
                    Integer.valueOf(prazo.getText()),
                    new AmortizacaoExtra(
                            Double.valueOf(valorExtraInicial.getText()),
                            Double.valueOf(percentProximoValorExtra.getText()),
                            Double.valueOf(valorExtraMinimo.getText()),
                            Integer.valueOf(mesInicial.getText()),
                            Integer.valueOf(intervalo.getText()),
                            Integer.valueOf(quantidadePagamentosExtra.getText()),
                            qtdOcorrenciasCheckBox.isSelected()),
                    new RendimentoPassivo(
                            Double.valueOf(valorInvestido.getText()),
                            Double.valueOf(rendimentoAnual.getText())),
                    new FGTS(
                            Double.valueOf(saldoFGTS.getText()),
                            Double.valueOf(salarioBruto.getText())
                    ),
                    opcoesAvancadas
            );
        } else {
            sistemaAmortizacao = new SAC(
                    nomeFinanciamento.getText(),
                    Double.valueOf(valorImovel.getText()),
                    (double) percentualEntrada.getValue(),
                    Double.valueOf(jurosAnual.getText()),
                    Integer.valueOf(prazo.getText()),
                    new AmortizacaoExtra(
                            Double.valueOf(valorExtraInicial.getText()),
                            Double.valueOf(percentProximoValorExtra.getText()),
                            Double.valueOf(valorExtraMinimo.getText()),
                            Integer.valueOf(mesInicial.getText()),
                            Integer.valueOf(intervalo.getText()),
                            Integer.valueOf(quantidadePagamentosExtra.getText()),
                            qtdOcorrenciasCheckBox.isSelected()),
                    new RendimentoPassivo(
                            Double.valueOf(valorInvestido.getText()),
                            Double.valueOf(rendimentoAnual.getText())),
                    new FGTS(
                            Double.valueOf(saldoFGTS.getText()),
                            Double.valueOf(salarioBruto.getText())
                    ),
                    opcoesAvancadas
            );
        }
        return sistemaAmortizacao;
    }

    private void resetFields() {
        nomeFinanciamento.setText("");
        valorImovel.setText("");
        percentualEntrada.setValue(30);
        jurosAnual.setText("");
        prazo.setText("");
        valorExtraInicial.setText("");
        percentProximoValorExtra.setText("");
        valorExtraMinimo.setText("");
        valorInvestido.setText("");
        rendimentoAnual.setText("");
    }

    private void updateFields(SistemaAmortizacao sistemaAmortizacao) {
        nomeFinanciamento.setText(sistemaAmortizacao.getNomeFinanciamento());
        valorImovel.setText(sistemaAmortizacao.getValorImovel().toString());
        percentualEntrada.setValue((int) (sistemaAmortizacao.getPercentualEntrada() * 100));
        sistemaAmortizacaoComboBox.setSelectedIndex(sistemaAmortizacao.getSistemaAmortizacao().getIndex());
        jurosAnual.setText(String.valueOf(sistemaAmortizacao.getJurosAnual() * 100));
        prazo.setText(String.valueOf(sistemaAmortizacao.getPrazo()));

        valorExtraInicial.setText(sistemaAmortizacao.getAmortizacaoExtra().getValorExtraInicial().toString());
        percentProximoValorExtra.setText(String.valueOf(sistemaAmortizacao.getAmortizacaoExtra().getPercentProximoValorExtra() * 100));
        valorExtraMinimo.setText(sistemaAmortizacao.getAmortizacaoExtra().getValorExtraMinimo().toString());
        mesInicial.setText(sistemaAmortizacao.getAmortizacaoExtra().getMesInicial().toString());
        intervalo.setText(sistemaAmortizacao.getAmortizacaoExtra().getIntervalo().toString());
        quantidadePagamentosExtra.setText(sistemaAmortizacao.getAmortizacaoExtra().getQuantidadeAmortizacoesDesejadas().toString());

        valorInvestido.setText(sistemaAmortizacao.getRendimentoPassivo().getValorInvestido().toString());
        rendimentoAnual.setText(sistemaAmortizacao.getRendimentoPassivo().getRendimentoAnual().toString());

        salarioBruto.setText(sistemaAmortizacao.getFgts().getSalario().toString());
        saldoFGTS.setText(sistemaAmortizacao.getFgts().getSaldoAtual().toString());

        //Bottom Fields
        valorPagoTotalField.setText(String.format("Valor Pago Total: R$ %.2f", sistemaAmortizacao.getValorPagoTotal()));
        numeroParcelasField.setText("Número de Parcelas: " + sistemaAmortizacao.getNumeroParcelas());
        ratioValorPagoTotal.setText(String.format("Valor Pago Total / Valor Imovel: %.2f", sistemaAmortizacao.getValorPagoTotal() / sistemaAmortizacao.getValorImovel()));
        anosConclusaoField.setText(String.format("Quitado em: %.1f anos", (double) sistemaAmortizacao.getNumeroParcelas() / 12));
        valorImovelValorizado.setText(String.format("Valor Imóvel Valorizado: R$ %,.2f", sistemaAmortizacao.getOpcoesAvancadas().getValorImovelValorizado()));
        valorImovelInflacao.setText(String.format("Valor Imóvel Inflação: R$ %,.2f", sistemaAmortizacao.getOpcoesAvancadas().getValorImovelInflacao()));

        opcoesAvancadas = sistemaAmortizacao.getOpcoesAvancadas();
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
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(10, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.setEnabled(true);
        contentPane.setPreferredSize(new Dimension(1400, 800));
        verticalSplitPlane = new JSplitPane();
        verticalSplitPlane.setOrientation(1);
        contentPane.add(verticalSplitPlane, new GridConstraints(0, 0, 10, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        leftPanel.setForeground(new Color(-13947600));
        leftPanel.setMinimumSize(new Dimension(450, 673));
        verticalSplitPlane.setLeftComponent(leftPanel);
        horizontalSplitPane = new JSplitPane();
        horizontalSplitPane.setOrientation(0);
        leftPanel.add(horizontalSplitPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(400, -1), new Dimension(400, -1), null, 0, false));
        bottomLeftPanel = new JPanel();
        bottomLeftPanel.setLayout(new GridLayoutManager(11, 2, new Insets(20, 20, 20, 20), -1, -1));
        bottomLeftPanel.setMaximumSize(new Dimension(2147483647, 300));
        bottomLeftPanel.setMinimumSize(new Dimension(239, 300));
        bottomLeftPanel.setPreferredSize(new Dimension(264, 300));
        horizontalSplitPane.setRightComponent(bottomLeftPanel);
        bottomLeftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        valorPagoTotalField = new JLabel();
        valorPagoTotalField.setText("Valor Pago Total:");
        bottomLeftPanel.add(valorPagoTotalField, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numeroParcelasField = new JLabel();
        numeroParcelasField.setText("Número de Parcelas / Prazo:");
        bottomLeftPanel.add(numeroParcelasField, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), new Dimension(-1, 20), 0, false));
        ratioValorPagoTotal = new JLabel();
        ratioValorPagoTotal.setText("Valor Pago Total / Valor Imovel:");
        ratioValorPagoTotal.setToolTipText(" ");
        bottomLeftPanel.add(ratioValorPagoTotal, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        bottomLeftPanel.add(spacer1, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        changeThemeButton = new JButton();
        Font changeThemeButtonFont = this.$$$getFont$$$(null, -1, -1, changeThemeButton.getFont());
        if (changeThemeButtonFont != null) changeThemeButton.setFont(changeThemeButtonFont);
        changeThemeButton.setText("Alterar Tema");
        bottomLeftPanel.add(changeThemeButton, new GridConstraints(7, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        compararSimulacoesButton = new JButton();
        Font compararSimulacoesButtonFont = this.$$$getFont$$$(null, -1, -1, compararSimulacoesButton.getFont());
        if (compararSimulacoesButtonFont != null) compararSimulacoesButton.setFont(compararSimulacoesButtonFont);
        compararSimulacoesButton.setText("Comparar Simulações");
        bottomLeftPanel.add(compararSimulacoesButton, new GridConstraints(8, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        downloadExcelButton = new JButton();
        downloadExcelButton.setText("Download Excel");
        downloadExcelButton.setVerticalTextPosition(3);
        bottomLeftPanel.add(downloadExcelButton, new GridConstraints(9, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        anosConclusaoField = new JLabel();
        anosConclusaoField.setText("Quitado em:");
        bottomLeftPanel.add(anosConclusaoField, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        valorImovelValorizado = new JLabel();
        valorImovelValorizado.setText("Valor Imóvel Valorização:");
        bottomLeftPanel.add(valorImovelValorizado, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        valorImovelInflacao = new JLabel();
        valorImovelInflacao.setText("Valor Imóvel Inflação:");
        bottomLeftPanel.add(valorImovelInflacao, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        upperLeftPanel = new JPanel();
        upperLeftPanel.setLayout(new GridLayoutManager(23, 6, new Insets(20, 20, 20, 20), -1, -1));
        upperLeftPanel.setFocusable(false);
        upperLeftPanel.setForeground(new Color(-13947600));
        horizontalSplitPane.setLeftComponent(upperLeftPanel);
        upperLeftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        nomeFinanciamentoPanel = new JPanel();
        nomeFinanciamentoPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(nomeFinanciamentoPanel, new GridConstraints(0, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        nomeFinanciamentoLabel = new JLabel();
        nomeFinanciamentoLabel.setText("Nome Financiamento");
        nomeFinanciamentoPanel.add(nomeFinanciamentoLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        nomeFinanciamento = new JTextField();
        nomeFinanciamentoPanel.add(nomeFinanciamento, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        valorImovelPanel = new JPanel();
        valorImovelPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(valorImovelPanel, new GridConstraints(1, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        valorImovelLabel = new JLabel();
        valorImovelLabel.setText("Valor Imóvel (R$)");
        valorImovelPanel.add(valorImovelLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        valorImovel = new JTextField();
        valorImovelPanel.add(valorImovel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        percentualEntradaPanel = new JPanel();
        percentualEntradaPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(percentualEntradaPanel, new GridConstraints(2, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        percentualEntradaLabel = new JLabel();
        percentualEntradaLabel.setText("Percentual Entrada (%)");
        percentualEntradaPanel.add(percentualEntradaLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        percentualEntrada = new JSlider();
        percentualEntrada.setMajorTickSpacing(10);
        percentualEntrada.setMinorTickSpacing(10);
        percentualEntrada.setPaintLabels(true);
        percentualEntrada.setPaintTicks(false);
        percentualEntrada.setPaintTrack(true);
        percentualEntrada.setSnapToTicks(true);
        percentualEntrada.setValueIsAdjusting(false);
        percentualEntradaPanel.add(percentualEntrada, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        jurosAnualPanel = new JPanel();
        jurosAnualPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(jurosAnualPanel, new GridConstraints(4, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        jurosAnualLabel = new JLabel();
        jurosAnualLabel.setText("Juros Anual (%)");
        jurosAnualPanel.add(jurosAnualLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        jurosAnual = new JTextField();
        jurosAnualPanel.add(jurosAnual, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        valorExtraInicialPanel = new JPanel();
        valorExtraInicialPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        valorExtraInicialPanel.setEnabled(true);
        upperLeftPanel.add(valorExtraInicialPanel, new GridConstraints(7, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        valorExtraInicialLabel = new JLabel();
        valorExtraInicialLabel.setText("Valor Inicial (R$)");
        valorExtraInicialPanel.add(valorExtraInicialLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        valorExtraInicial = new JTextField();
        valorExtraInicialPanel.add(valorExtraInicial, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        percentualProximoValorExtraPanel = new JPanel();
        percentualProximoValorExtraPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(percentualProximoValorExtraPanel, new GridConstraints(8, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        percentProximoValorExtraLabel = new JLabel();
        percentProximoValorExtraLabel.setText("Prox. Valor (%)");
        percentProximoValorExtraLabel.putClientProperty("html.disable", Boolean.FALSE);
        percentualProximoValorExtraPanel.add(percentProximoValorExtraLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        percentProximoValorExtra = new JTextField();
        percentualProximoValorExtraPanel.add(percentProximoValorExtra, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        valorExtraMinimoPanel = new JPanel();
        valorExtraMinimoPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(valorExtraMinimoPanel, new GridConstraints(9, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(100, -1), null, 0, false));
        valorExtraMinimoLabel = new JLabel();
        valorExtraMinimoLabel.setText("Valor Mínimo (R$)");
        valorExtraMinimoPanel.add(valorExtraMinimoLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        valorExtraMinimo = new JTextField();
        valorExtraMinimoPanel.add(valorExtraMinimo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        valorInvestidoPanel = new JPanel();
        valorInvestidoPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(valorInvestidoPanel, new GridConstraints(14, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        valorInvestidoLabel = new JLabel();
        valorInvestidoLabel.setText("Valor Investido (R$)");
        valorInvestidoPanel.add(valorInvestidoLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        valorInvestido = new JTextField();
        valorInvestidoPanel.add(valorInvestido, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        rendimentoAnualPanel = new JPanel();
        rendimentoAnualPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(rendimentoAnualPanel, new GridConstraints(15, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        rendimentoAnualLabel = new JLabel();
        rendimentoAnualLabel.setText("Rendimento Anual (%)");
        rendimentoAnualPanel.add(rendimentoAnualLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        rendimentoAnual = new JTextField();
        rendimentoAnualPanel.add(rendimentoAnual, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        salarioBrutoPanel = new JPanel();
        salarioBrutoPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(salarioBrutoPanel, new GridConstraints(17, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Salário Bruto (R$)");
        salarioBrutoPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        salarioBruto = new JTextField();
        salarioBrutoPanel.add(salarioBruto, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        saldoAtualPanel = new JPanel();
        saldoAtualPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(saldoAtualPanel, new GridConstraints(18, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Saldo FGTS (R$)");
        saldoAtualPanel.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        saldoFGTS = new JTextField();
        saldoAtualPanel.add(saldoFGTS, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        resetarCamposPanel = new JPanel();
        resetarCamposPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(resetarCamposPanel, new GridConstraints(19, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Resetar campos?");
        resetarCamposPanel.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        resetarCamposBox = new JCheckBox();
        resetarCamposBox.setText("");
        resetarCamposPanel.add(resetarCamposBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        escolherSistemaPanel = new JPanel();
        escolherSistemaPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(escolherSistemaPanel, new GridConstraints(3, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Sistema Amortização");
        escolherSistemaPanel.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        sistemaAmortizacaoComboBox = new JComboBox();
        escolherSistemaPanel.add(sistemaAmortizacaoComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        prazoPanel = new JPanel();
        prazoPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(prazoPanel, new GridConstraints(5, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        prazoLabel = new JLabel();
        prazoLabel.setText("Prazo (meses)");
        prazoPanel.add(prazoLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        prazo = new JTextField();
        prazoPanel.add(prazo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        mesInicialPanel = new JPanel();
        mesInicialPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(mesInicialPanel, new GridConstraints(10, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        mesInicialLabel = new JLabel();
        mesInicialLabel.setText("Mês Inicio");
        mesInicialPanel.add(mesInicialLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(150, -1), null, 0, false));
        mesInicial = new JTextField();
        mesInicialPanel.add(mesInicial, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        intervaloPanel = new JPanel();
        intervaloPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(intervaloPanel, new GridConstraints(11, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        intervaloLabel = new JLabel();
        intervaloLabel.setText("Intervalo");
        intervaloPanel.add(intervaloLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(150, -1), null, 0, false));
        intervalo = new JTextField();
        intervaloPanel.add(intervalo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        quantidadePanel = new JPanel();
        quantidadePanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(quantidadePanel, new GridConstraints(12, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Repetições");
        quantidadePanel.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        quantidadePagamentosExtra = new JTextField();
        quantidadePanel.add(quantidadePagamentosExtra, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        qtdOcorrenciasCheckBox = new JCheckBox();
        qtdOcorrenciasCheckBox.setSelected(true);
        qtdOcorrenciasCheckBox.setText("Sempre");
        quantidadePanel.add(qtdOcorrenciasCheckBox, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        amortizacaoExtraPanel = new JPanel();
        amortizacaoExtraPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(amortizacaoExtraPanel, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        amortizacaoExtra = new JLabel();
        Font amortizacaoExtraFont = this.$$$getFont$$$(null, Font.BOLD, 14, amortizacaoExtra.getFont());
        if (amortizacaoExtraFont != null) amortizacaoExtra.setFont(amortizacaoExtraFont);
        amortizacaoExtra.setText("Amortizações Extra");
        amortizacaoExtraPanel.add(amortizacaoExtra, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        amortizacoesExtraCheckBox = new JCheckBox();
        Font amortizacoesExtraCheckBoxFont = this.$$$getFont$$$(null, Font.BOLD, 14, amortizacoesExtraCheckBox.getFont());
        if (amortizacoesExtraCheckBoxFont != null) amortizacoesExtraCheckBox.setFont(amortizacoesExtraCheckBoxFont);
        amortizacoesExtraCheckBox.setText("");
        amortizacaoExtraPanel.add(amortizacoesExtraCheckBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        investimentosPanel = new JPanel();
        investimentosPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(investimentosPanel, new GridConstraints(13, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        investimentosLabel = new JLabel();
        Font investimentosLabelFont = this.$$$getFont$$$(null, Font.BOLD, 14, investimentosLabel.getFont());
        if (investimentosLabelFont != null) investimentosLabel.setFont(investimentosLabelFont);
        investimentosLabel.setText("Investimentos");
        investimentosPanel.add(investimentosLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        investimentosCheckBox = new JCheckBox();
        investimentosCheckBox.setText("");
        investimentosPanel.add(investimentosCheckBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fgtsPanel = new JPanel();
        fgtsPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(fgtsPanel, new GridConstraints(16, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fgtsLabel = new JLabel();
        fgtsLabel.setEnabled(true);
        Font fgtsLabelFont = this.$$$getFont$$$(null, Font.BOLD, 14, fgtsLabel.getFont());
        if (fgtsLabelFont != null) fgtsLabel.setFont(fgtsLabelFont);
        fgtsLabel.setText("FGTS");
        fgtsPanel.add(fgtsLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        fgtsCheckBox = new JCheckBox();
        fgtsCheckBox.setText("");
        fgtsPanel.add(fgtsCheckBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        advancedOptionsButton = new JButton();
        advancedOptionsButton.setText("Opções Avançadas");
        upperLeftPanel.add(advancedOptionsButton, new GridConstraints(21, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        final Spacer spacer2 = new Spacer();
        upperLeftPanel.add(spacer2, new GridConstraints(20, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 50), null, 0, false));
        calcularButton = new JButton();
        calcularButton.setEnabled(true);
        Font calcularButtonFont = this.$$$getFont$$$(null, -1, -1, calcularButton.getFont());
        if (calcularButtonFont != null) calcularButton.setFont(calcularButtonFont);
        calcularButton.setHideActionText(false);
        calcularButton.setHorizontalTextPosition(0);
        calcularButton.setText("Calcular");
        upperLeftPanel.add(calcularButton, new GridConstraints(22, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        verticalSplitPlane.setRightComponent(rightPanel);
        tabs = new JTabbedPane();
        rightPanel.add(tabs, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
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
        return contentPane;
    }

}
