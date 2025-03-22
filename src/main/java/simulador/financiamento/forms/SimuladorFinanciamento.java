package simulador.financiamento.forms;

import com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import simulador.financiamento.dominio.*;
import simulador.financiamento.dominio.amortizacao.AmortizacaoExtra;
import simulador.financiamento.tabela.ExcelWriter;
import simulador.financiamento.sistemas.amortizacao.PRICE;
import simulador.financiamento.sistemas.amortizacao.SAC;
import simulador.financiamento.sistemas.amortizacao.SistemaAmortizacao;
import simulador.financiamento.utils.JTabbedPaneCloseButton;
import simulador.financiamento.utils.SwingUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Locale;

public class SimuladorFinanciamento extends JFrame {

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
    private JLabel recorrencia;
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
    private JButton saveButton;
    private JButton loadButton;
    private JPanel resgatePanel;
    private JLabel resgateLabel;
    private JTextField resgate;
    private JLabel acaoNoResgate;
    private JComboBox<String> resgateComboBox;
    private JPanel acaoResgatePanel;
    private JLabel saldoInvestido;
    private JLabel valorizacaoReal;

    private final ExcelWriter excelWriter = new ExcelWriter();

    private boolean darkTheme = true;

    private ArrayList<SistemaAmortizacao> simulationsList = new ArrayList<>();

    private OpcoesAvancadas opcoesAvancadas = new OpcoesAvancadas(0.0, 0.0);

    public SimuladorFinanciamento() {
        $$$setupUI$$$();
        setTitle("Simulador de Financiamentos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        getContentPane().setBackground(Color.black);

        JScrollPane jScrollPane = new JScrollPane(upperLeftPanel);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        horizontalSplitPane.setLeftComponent(jScrollPane);
        horizontalSplitPane.setDividerLocation((int) (contentPane.getPreferredSize().getHeight() * 0.6));

        pack();
        // Set the frame location to the center of the screen
        setLocationRelativeTo(null);

        defineOpcoesSistemasAmortizacao();
        defineOpcoesResgate();
        defineFontes();
        desabilitaCamposAmortizacao();
        desabilitaCamposInvestimentos();
        desabilitaCamposFgts();
        preencheCamposOpcionaisDefault();

        calcularButton.addActionListener(e -> calcularFinanciamento());
        changeThemeButton.addActionListener(e -> toggleTheme());
        compararSimulacoesButton.addActionListener(e -> compararSimulacoes(simulationsList));
        downloadExcelButton.addActionListener(e -> fazerDownload());
        saveButton.addActionListener(e -> salvar());
        loadButton.addActionListener(e -> carregar());

        advancedOptionsButton.addActionListener(e -> {
            OpcoesAvancadasDialog dialog = new OpcoesAvancadasDialog(opcoesAvancadas);
            dialog.setMinimumSize(new Dimension(400, 200));
            dialog.setTitle("Opções Avançadas");
            dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dialog.setLocationRelativeTo(null);
            dialog.pack();

            opcoesAvancadas = dialog.showDialog();
            System.out.println(opcoesAvancadas);
        });

        tabs.addChangeListener(e -> {
            if (!simulationsList.isEmpty() && simulationsList.size() == tabs.getTabCount()) {
                int selectedTab = tabs.getSelectedIndex();
                SistemaAmortizacao sistemaAmortizacao = simulationsList.get(selectedTab);
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

    private void salvar() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("simulationsList.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(simulationsList);
            objectOutputStream.close();
            fileOutputStream.close();

            new InformacaoDialog("Dados Salvos", "Dados salvos com sucesso!");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void carregar() {
        try {
            FileInputStream fileInput = new FileInputStream("simulationsList.txt");
            ObjectInputStream objectInput = new ObjectInputStream(fileInput);

            simulationsList = (ArrayList<SistemaAmortizacao>) objectInput.readObject();

            objectInput.close();
            fileInput.close();

            tabs.removeAll();

            simulationsList.forEach((sistemaAmortizacao) -> {
                JTable jTable = SwingUtils.getSimulationJTable(sistemaAmortizacao);
                JScrollPane scrollPane = new JScrollPane(jTable);

                tabs.add(sistemaAmortizacao.getNomeFinanciamento(), scrollPane);
            });

            tabs.setSelectedIndex(tabs.getTabCount() - 1);

            ((JTabbedPaneCloseButton) tabs).setSimulationsList(simulationsList);

        } catch (FileNotFoundException e) {
            new InformacaoDialog("Erro ao carregar", "O sistema não pode encontrar o arquivo especificado.");
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fazerDownload() {
        if (tabs.getTabCount() > 0) {
            int selectedTab = tabs.getSelectedIndex();
            SistemaAmortizacao sistemaAmortizacao = simulationsList.get(selectedTab);
            excelWriter.writeTable(sistemaAmortizacao);
        }
    }

    private void defineFontes() {
        calcularButton.setFont(new Font(calcularButton.getFont().getFontName(), Font.BOLD, 16));
        changeThemeButton.setFont(new Font(changeThemeButton.getFont().getFontName(), Font.PLAIN, 16));
        compararSimulacoesButton.setFont(new Font(compararSimulacoesButton.getFont().getFontName(), Font.PLAIN, 16));
        advancedOptionsButton.setFont(new Font(advancedOptionsButton.getFont().getFontName(), Font.PLAIN, 16));
        downloadExcelButton.setFont(new Font(downloadExcelButton.getFont().getFontName(), Font.PLAIN, 16));
        saveButton.setFont(new Font(saveButton.getFont().getFontName(), Font.PLAIN, 16));
        loadButton.setFont(new Font(loadButton.getFont().getFontName(), Font.PLAIN, 16));
    }

    private void defineOpcoesSistemasAmortizacao() {
        sistemaAmortizacaoComboBox.addItem(SistemaAmortizacaoEnum.PRICE.getNome());
        sistemaAmortizacaoComboBox.addItem(SistemaAmortizacaoEnum.SAC.getNome());
    }

    private void defineOpcoesResgate() {
        resgateComboBox.addItem(AcoesResgate.AMORTIZAR.getNome());
        resgateComboBox.addItem(AcoesResgate.REINVESTIR.getNome());
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
        resgatePanel.setVisible(false);
        acaoResgatePanel.setVisible(false);
    }

    private void habilitaCamposInvestimentos() {
        valorInvestidoPanel.setVisible(true);
        rendimentoAnualPanel.setVisible(true);
        resgatePanel.setVisible(true);
        acaoResgatePanel.setVisible(true);
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
        resgate.setText("2");
        resgateComboBox.setSelectedIndex(0);

        salarioBruto.setText("0");
        saldoFGTS.setText("0");
    }

    private void compararSimulacoes(ArrayList<SistemaAmortizacao> simulationsList) {
        new CompararSimulacoes(simulationsList);
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

        if (validateDuplicatedSimulationName(sistemaAmortizacao)) {
            new InformacaoDialog("Nome duplicado", "O nome da simulação já foi utilizado, favor informar um nome único.");
            return;
        }

        sistemaAmortizacao.calcularFinanciamento();

        valorPagoTotalField.setText(String.format("Valor Pago Total: R$ %,.2f", sistemaAmortizacao.getValorPagoTotal()));
        numeroParcelasField.setText("Número de Parcelas / Prazo: " + sistemaAmortizacao.getNumeroParcelas() + "/" + sistemaAmortizacao.getPrazo());
        ratioValorPagoTotal.setText(String.format("Valor Pago Total / Valor Imovel: %.2f", sistemaAmortizacao.getValorPagoTotal() / sistemaAmortizacao.getValorImovel()));
        anosConclusaoField.setText(String.format("Quitado em: %.1f anos", (double) sistemaAmortizacao.getNumeroParcelas() / 12));
        valorImovelValorizado.setText(String.format("Valor Imóvel Valorizado: R$ %,.2f", sistemaAmortizacao.getOpcoesAvancadas().getValorImovelValorizado()));
        valorImovelInflacao.setText(String.format("Valor Imóvel Inflação: R$ %,.2f", sistemaAmortizacao.getOpcoesAvancadas().getValorImovelInflacao()));
        valorizacaoReal.setText(String.format("Valorização Real: R$ %,.2f", sistemaAmortizacao.getOpcoesAvancadas().getValorImovelValorizado() - sistemaAmortizacao.getOpcoesAvancadas().getValorImovelInflacao()));
        saldoInvestido.setText(String.format("Saldo Valor Investido: R$ %,.2f", AmortizacaoExtra.buscarAmortizacaoExtra(Investimentos.class, sistemaAmortizacao.getAmortizacaoExtraList()).getValorInvestido()));

        JTable jTable = SwingUtils.getSimulationJTable(sistemaAmortizacao);
        JScrollPane scrollPane = new JScrollPane(jTable);

        tabs.add(nomeFinanciamento.getText(), scrollPane);
        tabs.setSelectedIndex(tabs.getTabCount() - 1);

        simulationsList.add(sistemaAmortizacao);

        if (resetarCamposBox.isSelected()) {
            resetFields();
        }
    }

    private boolean validateDuplicatedSimulationName(SistemaAmortizacao sistemaAmortizacao) {
        return simulationsList.stream()
                .map(SistemaAmortizacao::getNomeFinanciamento)
                .anyMatch(name -> name.equals(sistemaAmortizacao.getNomeFinanciamento()));
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
                    new Recorrencia(
                            Double.valueOf(valorExtraInicial.getText()),
                            Double.valueOf(percentProximoValorExtra.getText()),
                            Double.valueOf(valorExtraMinimo.getText()),
                            Integer.valueOf(mesInicial.getText()),
                            Integer.valueOf(intervalo.getText()),
                            Integer.valueOf(quantidadePagamentosExtra.getText()),
                            qtdOcorrenciasCheckBox.isSelected()),
                    new Investimentos(
                            Double.valueOf(valorInvestido.getText()),
                            Double.valueOf(rendimentoAnual.getText()),
                            Integer.valueOf(resgate.getText()),
                            (String) resgateComboBox.getSelectedItem()
                    ),
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
                    new Recorrencia(
                            Double.valueOf(valorExtraInicial.getText()),
                            Double.valueOf(percentProximoValorExtra.getText()),
                            Double.valueOf(valorExtraMinimo.getText()),
                            Integer.valueOf(mesInicial.getText()),
                            Integer.valueOf(intervalo.getText()),
                            Integer.valueOf(quantidadePagamentosExtra.getText()),
                            qtdOcorrenciasCheckBox.isSelected()),
                    new Investimentos(
                            Double.valueOf(valorInvestido.getText()),
                            Double.valueOf(rendimentoAnual.getText()),
                            Integer.valueOf(resgate.getText()),
                            (String) resgateComboBox.getSelectedItem()
                    ),
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
        percentualEntrada.setValue(50);
        sistemaAmortizacaoComboBox.setSelectedIndex(0);
        jurosAnual.setText("");
        prazo.setText("");

        valorExtraInicial.setText("");
        percentProximoValorExtra.setText("");
        valorExtraMinimo.setText("");
        mesInicial.setText("1");
        intervalo.setText("1");
        quantidadePagamentosExtra.setText("0");

        valorInvestido.setText("0");
        rendimentoAnual.setText("0");
        resgate.setText("2");
        resgateComboBox.setSelectedIndex(0);

        salarioBruto.setText("0");
        saldoFGTS.setText("0");
    }

    private void updateFields(SistemaAmortizacao sistemaAmortizacao) {
        nomeFinanciamento.setText(sistemaAmortizacao.getNomeFinanciamento());
        valorImovel.setText(sistemaAmortizacao.getValorImovel().toString());
        percentualEntrada.setValue((int) (sistemaAmortizacao.getPercentualEntrada() * 100));
        sistemaAmortizacaoComboBox.setSelectedIndex(sistemaAmortizacao.getSistemaAmortizacao().getIndex());
        jurosAnual.setText(String.valueOf(sistemaAmortizacao.getJurosAnual() * 100));
        prazo.setText(String.valueOf(sistemaAmortizacao.getPrazo()));

        Recorrencia recorrencia = AmortizacaoExtra.buscarAmortizacaoExtra(Recorrencia.class, sistemaAmortizacao.getAmortizacaoExtraList());
        valorExtraInicial.setText(recorrencia.getValorExtraInicial().toString());
        percentProximoValorExtra.setText(String.valueOf(recorrencia.getPercentProximoValorExtra() * 100));
        valorExtraMinimo.setText(recorrencia.getValorExtraMinimo().toString());
        mesInicial.setText(recorrencia.getMesInicial().toString());
        intervalo.setText(recorrencia.getIntervalo().toString());
        quantidadePagamentosExtra.setText(recorrencia.getQuantidadeAmortizacoesDesejadas().toString());

        Investimentos investimentos = AmortizacaoExtra.buscarAmortizacaoExtra(Investimentos.class, sistemaAmortizacao.getAmortizacaoExtraList());
        valorInvestido.setText(investimentos.getValorInicialPrimeiroAporte().toString());
        rendimentoAnual.setText(investimentos.getRendimentoAnual().toString());
        resgate.setText(investimentos.getPrazoResgateAnos().toString());
        resgateComboBox.setSelectedIndex(investimentos.getAcao().getIndex());

        FGTS fgts = AmortizacaoExtra.buscarAmortizacaoExtra(FGTS.class, sistemaAmortizacao.getAmortizacaoExtraList());
        salarioBruto.setText(fgts.getSalario().toString());
        saldoFGTS.setText(fgts.getSaldoAtual().toString());

        //Bottom Fields
        valorPagoTotalField.setText(String.format("Valor Pago Total: R$ %,.2f", sistemaAmortizacao.getValorPagoTotal()));
        numeroParcelasField.setText("Número de Parcelas / Prazo: " + sistemaAmortizacao.getNumeroParcelas() + "/" + sistemaAmortizacao.getPrazo());
        ratioValorPagoTotal.setText(String.format("Valor Pago Total / Valor Imovel: %.2f", sistemaAmortizacao.getValorPagoTotal() / sistemaAmortizacao.getValorImovel()));
        anosConclusaoField.setText(String.format("Quitado em: %.1f anos", (double) sistemaAmortizacao.getNumeroParcelas() / 12));
        valorImovelValorizado.setText(String.format("Valor Imóvel Valorizado: R$ %,.2f", sistemaAmortizacao.getOpcoesAvancadas().getValorImovelValorizado()));
        valorImovelInflacao.setText(String.format("Valor Imóvel Inflação: R$ %,.2f", sistemaAmortizacao.getOpcoesAvancadas().getValorImovelInflacao()));
        valorizacaoReal.setText(String.format("Valorização Real: R$ %,.2f", sistemaAmortizacao.getOpcoesAvancadas().getValorImovelValorizado() - sistemaAmortizacao.getOpcoesAvancadas().getValorImovelInflacao()));
        saldoInvestido.setText(String.format("Saldo Valor Investido: R$ %,.2f", investimentos.getValorInvestido()));

        opcoesAvancadas = sistemaAmortizacao.getOpcoesAvancadas();

        System.out.println(simulationsList);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(10, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.setEnabled(true);
        contentPane.setPreferredSize(new Dimension(1500, 900));
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
        bottomLeftPanel.setLayout(new GridLayoutManager(13, 3, new Insets(20, 20, 20, 20), -1, -1));
        bottomLeftPanel.setMaximumSize(new Dimension(2147483647, 300));
        bottomLeftPanel.setMinimumSize(new Dimension(239, 300));
        bottomLeftPanel.setPreferredSize(new Dimension(264, 300));
        horizontalSplitPane.setRightComponent(bottomLeftPanel);
        bottomLeftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        valorPagoTotalField = new JLabel();
        valorPagoTotalField.setText("Valor Pago Total:");
        valorPagoTotalField.setToolTipText("Soma do valor pago a cada mes até o final do financiamento.");
        bottomLeftPanel.add(valorPagoTotalField, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numeroParcelasField = new JLabel();
        numeroParcelasField.setText("Número de Parcelas / Prazo:");
        numeroParcelasField.setToolTipText("Número de parcelas pagas em relação ao prazo inicial contratado");
        bottomLeftPanel.add(numeroParcelasField, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), new Dimension(-1, 20), 0, false));
        ratioValorPagoTotal = new JLabel();
        ratioValorPagoTotal.setText("Valor Pago Total / Valor Imovel:");
        ratioValorPagoTotal.setToolTipText(" Relação entre valor pago e valor do imóvel na compra.");
        bottomLeftPanel.add(ratioValorPagoTotal, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        bottomLeftPanel.add(spacer1, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        compararSimulacoesButton = new JButton();
        Font compararSimulacoesButtonFont = this.$$$getFont$$$(null, -1, -1, compararSimulacoesButton.getFont());
        if (compararSimulacoesButtonFont != null) compararSimulacoesButton.setFont(compararSimulacoesButtonFont);
        compararSimulacoesButton.setText("Comparar Simulações");
        compararSimulacoesButton.setToolTipText("Mostra tabela comparativa entre os resultados das simulações e outras opções para comparação gráfica.");
        bottomLeftPanel.add(compararSimulacoesButton, new GridConstraints(9, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        downloadExcelButton = new JButton();
        downloadExcelButton.setText("Download Excel");
        downloadExcelButton.setToolTipText("Realiza o download de um arquivo .xlsx contendo as informações do financiamento da aba atualmente selecionada.");
        downloadExcelButton.setVerticalTextPosition(3);
        bottomLeftPanel.add(downloadExcelButton, new GridConstraints(10, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        anosConclusaoField = new JLabel();
        anosConclusaoField.setText("Quitado em:");
        anosConclusaoField.setToolTipText("Tempo para quitar o financiamento em anos.");
        bottomLeftPanel.add(anosConclusaoField, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        valorImovelValorizado = new JLabel();
        valorImovelValorizado.setText("Valor Imóvel Valorização:");
        valorImovelValorizado.setToolTipText("Valor do imóvel considerando a valorização até o término do financiamento, caso tenha sido informada a taxa de expectativa de valorização nas opções avançadas.");
        bottomLeftPanel.add(valorImovelValorizado, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        valorImovelInflacao = new JLabel();
        valorImovelInflacao.setText("Valor Imóvel Inflação:");
        valorImovelInflacao.setToolTipText("Valor do imóvel considerando a inflação ao término do financiamento, caso tenha sido informada a taxa de expectativa de inflação nas opções avançadas.");
        bottomLeftPanel.add(valorImovelInflacao, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        saveButton = new JButton();
        saveButton.setText("Salvar");
        saveButton.setToolTipText("Salva as simulações na mesma pasta em que está sendo executado o programa.");
        bottomLeftPanel.add(saveButton, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, -1), new Dimension(200, -1), null, 0, false));
        loadButton = new JButton();
        loadButton.setText("Carregar");
        loadButton.setToolTipText("Carrega as simulações que estiverem salvas na mesma pasta em que está sendo executado o programa.");
        bottomLeftPanel.add(loadButton, new GridConstraints(12, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, -1), new Dimension(200, -1), null, 0, false));
        changeThemeButton = new JButton();
        Font changeThemeButtonFont = this.$$$getFont$$$(null, -1, -1, changeThemeButton.getFont());
        if (changeThemeButtonFont != null) changeThemeButton.setFont(changeThemeButtonFont);
        changeThemeButton.setText("Alterar Tema");
        changeThemeButton.setToolTipText("Altera entre tema claro e escuro");
        bottomLeftPanel.add(changeThemeButton, new GridConstraints(11, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        saldoInvestido = new JLabel();
        saldoInvestido.setText("Saldo Valor Investido:");
        saldoInvestido.setToolTipText("Saldo do valor investido, caso tenha sido informado no campo Investimentos.");
        bottomLeftPanel.add(saldoInvestido, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        valorizacaoReal = new JLabel();
        valorizacaoReal.setText("Valorização Real:");
        bottomLeftPanel.add(valorizacaoReal, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        upperLeftPanel = new JPanel();
        upperLeftPanel.setLayout(new GridLayoutManager(25, 6, new Insets(20, 20, 20, 20), -1, -1));
        upperLeftPanel.setFocusable(false);
        upperLeftPanel.setForeground(new Color(-13947600));
        horizontalSplitPane.setLeftComponent(upperLeftPanel);
        upperLeftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        nomeFinanciamentoPanel = new JPanel();
        nomeFinanciamentoPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(nomeFinanciamentoPanel, new GridConstraints(0, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        nomeFinanciamentoLabel = new JLabel();
        nomeFinanciamentoLabel.setText("Nome Financiamento");
        nomeFinanciamentoLabel.setToolTipText("Nome da simulação de financiamento");
        nomeFinanciamentoPanel.add(nomeFinanciamentoLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        nomeFinanciamento = new JTextField();
        nomeFinanciamentoPanel.add(nomeFinanciamento, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        valorImovelPanel = new JPanel();
        valorImovelPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(valorImovelPanel, new GridConstraints(1, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        valorImovelLabel = new JLabel();
        valorImovelLabel.setText("Valor Imóvel (R$)");
        valorImovelLabel.setToolTipText("Valor do Imóvel em reais");
        valorImovelPanel.add(valorImovelLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        valorImovel = new JTextField();
        valorImovelPanel.add(valorImovel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        percentualEntradaPanel = new JPanel();
        percentualEntradaPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(percentualEntradaPanel, new GridConstraints(2, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        percentualEntradaLabel = new JLabel();
        percentualEntradaLabel.setText("Percentual Entrada (%)");
        percentualEntradaLabel.setToolTipText("Percentual de entrada");
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
        jurosAnualLabel.setToolTipText("Júros Efetivo Anual");
        jurosAnualPanel.add(jurosAnualLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        jurosAnual = new JTextField();
        jurosAnualPanel.add(jurosAnual, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        valorExtraInicialPanel = new JPanel();
        valorExtraInicialPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        valorExtraInicialPanel.setEnabled(true);
        upperLeftPanel.add(valorExtraInicialPanel, new GridConstraints(7, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        valorExtraInicialLabel = new JLabel();
        valorExtraInicialLabel.setText("Valor Inicial (R$)");
        valorExtraInicialLabel.setToolTipText("Valor que será amortizado na primeira recorrência");
        valorExtraInicialPanel.add(valorExtraInicialLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        valorExtraInicial = new JTextField();
        valorExtraInicialPanel.add(valorExtraInicial, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        percentualProximoValorExtraPanel = new JPanel();
        percentualProximoValorExtraPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(percentualProximoValorExtraPanel, new GridConstraints(8, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        percentProximoValorExtraLabel = new JLabel();
        percentProximoValorExtraLabel.setText("Prox. Valor (%)");
        percentProximoValorExtraLabel.setToolTipText("Percentual para amortizar a partir do valor anterior. Caso seja 100%, o valor será constante.");
        percentProximoValorExtraLabel.putClientProperty("html.disable", Boolean.FALSE);
        percentualProximoValorExtraPanel.add(percentProximoValorExtraLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        percentProximoValorExtra = new JTextField();
        percentualProximoValorExtraPanel.add(percentProximoValorExtra, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        valorExtraMinimoPanel = new JPanel();
        valorExtraMinimoPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        valorExtraMinimoPanel.setToolTipText("Valor mínimo que deverá ser amortizado.");
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
        valorInvestidoLabel.setToolTipText("Valor investido em reais");
        valorInvestidoPanel.add(valorInvestidoLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        valorInvestido = new JTextField();
        valorInvestidoPanel.add(valorInvestido, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        rendimentoAnualPanel = new JPanel();
        rendimentoAnualPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(rendimentoAnualPanel, new GridConstraints(15, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        rendimentoAnualLabel = new JLabel();
        rendimentoAnualLabel.setText("Rendimento Anual (%)");
        rendimentoAnualLabel.setToolTipText("Rendimento Anual bruto do produto de investimento contratado.");
        rendimentoAnualPanel.add(rendimentoAnualLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        rendimentoAnual = new JTextField();
        rendimentoAnualPanel.add(rendimentoAnual, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        salarioBrutoPanel = new JPanel();
        salarioBrutoPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(salarioBrutoPanel, new GridConstraints(19, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Salário Bruto (R$)");
        label1.setToolTipText("Salário bruto em reais");
        salarioBrutoPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        salarioBruto = new JTextField();
        salarioBrutoPanel.add(salarioBruto, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        saldoAtualPanel = new JPanel();
        saldoAtualPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(saldoAtualPanel, new GridConstraints(20, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Saldo FGTS (R$)");
        label2.setToolTipText("Saldo inicial no FGTS");
        saldoAtualPanel.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        saldoFGTS = new JTextField();
        saldoAtualPanel.add(saldoFGTS, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        resetarCamposPanel = new JPanel();
        resetarCamposPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(resetarCamposPanel, new GridConstraints(21, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Resetar campos?");
        label3.setToolTipText("Caso marcado, todos os campos serão resetados após calcular a simulação");
        resetarCamposPanel.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        resetarCamposBox = new JCheckBox();
        resetarCamposBox.setText("");
        resetarCamposPanel.add(resetarCamposBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        escolherSistemaPanel = new JPanel();
        escolherSistemaPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(escolherSistemaPanel, new GridConstraints(3, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Sistema Amortização");
        label4.setToolTipText("Selecione o sistema de amortização");
        escolherSistemaPanel.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        sistemaAmortizacaoComboBox = new JComboBox();
        escolherSistemaPanel.add(sistemaAmortizacaoComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        prazoPanel = new JPanel();
        prazoPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(prazoPanel, new GridConstraints(5, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        prazoLabel = new JLabel();
        prazoLabel.setText("Prazo (meses)");
        prazoLabel.setToolTipText("Duração do financiamento");
        prazoPanel.add(prazoLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        prazo = new JTextField();
        prazoPanel.add(prazo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        mesInicialPanel = new JPanel();
        mesInicialPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(mesInicialPanel, new GridConstraints(10, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        mesInicialLabel = new JLabel();
        mesInicialLabel.setText("Mês Inicio");
        mesInicialLabel.setToolTipText("Mês da primeira amortização");
        mesInicialPanel.add(mesInicialLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(150, -1), null, 0, false));
        mesInicial = new JTextField();
        mesInicialPanel.add(mesInicial, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        intervaloPanel = new JPanel();
        intervaloPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(intervaloPanel, new GridConstraints(11, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        intervaloLabel = new JLabel();
        intervaloLabel.setText("Intervalo");
        intervaloLabel.setToolTipText("Intervalo entre cada amortização");
        intervaloPanel.add(intervaloLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(150, -1), null, 0, false));
        intervalo = new JTextField();
        intervaloPanel.add(intervalo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        quantidadePanel = new JPanel();
        quantidadePanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(quantidadePanel, new GridConstraints(12, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Repetições");
        label5.setToolTipText("Quantidade de vezes que será amortizado.");
        quantidadePanel.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        quantidadePagamentosExtra = new JTextField();
        quantidadePanel.add(quantidadePagamentosExtra, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        qtdOcorrenciasCheckBox = new JCheckBox();
        qtdOcorrenciasCheckBox.setSelected(true);
        qtdOcorrenciasCheckBox.setText("Sempre");
        qtdOcorrenciasCheckBox.setToolTipText("Caso selecionado, as amortizações serão recorrentes até o final do financiamento");
        quantidadePanel.add(qtdOcorrenciasCheckBox, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        amortizacaoExtraPanel = new JPanel();
        amortizacaoExtraPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(amortizacaoExtraPanel, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        recorrencia = new JLabel();
        Font recorrenciaFont = this.$$$getFont$$$(null, Font.BOLD, 14, recorrencia.getFont());
        if (recorrenciaFont != null) recorrencia.setFont(recorrenciaFont);
        recorrencia.setText("Recorrência");
        recorrencia.setToolTipText("Sessão destinada a considerar amortizações recorrentes, conforme valores, intervalo e repetições desejadas");
        amortizacaoExtraPanel.add(recorrencia, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
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
        investimentosLabel.setToolTipText("Sessão destinada a considerar amortizações a partir dos lucros de um investimento, ou simular o valor total investido ao término do financiamento. O IR considerado é de 15%, alíquota vigente para aplicações financeiras de no mínimo 2 anos.");
        investimentosPanel.add(investimentosLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        investimentosCheckBox = new JCheckBox();
        investimentosCheckBox.setText("");
        investimentosPanel.add(investimentosCheckBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fgtsPanel = new JPanel();
        fgtsPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(fgtsPanel, new GridConstraints(18, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fgtsLabel = new JLabel();
        fgtsLabel.setEnabled(true);
        Font fgtsLabelFont = this.$$$getFont$$$(null, Font.BOLD, 14, fgtsLabel.getFont());
        if (fgtsLabelFont != null) fgtsLabel.setFont(fgtsLabelFont);
        fgtsLabel.setText("FGTS");
        fgtsLabel.setToolTipText("Sessão destinada a considerar amortizações a partir do FGTS. O intervalo considerado para amortizar é a cada 2 anos, com rendimento de 3% ao ano, e com aportes iguais a 8% do salário bruto mensalmente.");
        fgtsPanel.add(fgtsLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        fgtsCheckBox = new JCheckBox();
        fgtsCheckBox.setText("");
        fgtsPanel.add(fgtsCheckBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        advancedOptionsButton = new JButton();
        advancedOptionsButton.setText("Opções Avançadas");
        advancedOptionsButton.setToolTipText("Permite informar outros parâmetros opcionais.");
        upperLeftPanel.add(advancedOptionsButton, new GridConstraints(23, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        final Spacer spacer2 = new Spacer();
        upperLeftPanel.add(spacer2, new GridConstraints(22, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 20), null, 0, false));
        calcularButton = new JButton();
        calcularButton.setEnabled(true);
        Font calcularButtonFont = this.$$$getFont$$$(null, -1, -1, calcularButton.getFont());
        if (calcularButtonFont != null) calcularButton.setFont(calcularButtonFont);
        calcularButton.setHideActionText(false);
        calcularButton.setHorizontalTextPosition(0);
        calcularButton.setText("Calcular");
        calcularButton.setToolTipText("Calcula a simulação com os dados informados.");
        upperLeftPanel.add(calcularButton, new GridConstraints(24, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        resgatePanel = new JPanel();
        resgatePanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(resgatePanel, new GridConstraints(16, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        resgateLabel = new JLabel();
        resgateLabel.setText("Resgate em (anos)");
        resgateLabel.setToolTipText("Tempo até o resgate em anos");
        resgatePanel.add(resgateLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        resgate = new JTextField();
        resgatePanel.add(resgate, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        acaoResgatePanel = new JPanel();
        acaoResgatePanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(acaoResgatePanel, new GridConstraints(17, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        acaoNoResgate = new JLabel();
        acaoNoResgate.setText("Ação");
        acaoNoResgate.setToolTipText("Ação a ser tomada no momento do resgate");
        acaoResgatePanel.add(acaoNoResgate, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        resgateComboBox = new JComboBox();
        acaoResgatePanel.add(resgateComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        verticalSplitPlane.setRightComponent(rightPanel);
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

    private void createUIComponents() {
        tabs = new JTabbedPaneCloseButton(simulationsList);
    }
}
