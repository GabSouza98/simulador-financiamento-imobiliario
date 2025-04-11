package simulador.financiamento.utils;

import lombok.Setter;
import simulador.financiamento.dominio.sistemas.SistemaAmortizacao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

@Setter
public class JTabbedPaneCloseButton extends JTabbedPane {

    private ArrayList<SistemaAmortizacao> simulationsList;

    public JTabbedPaneCloseButton(ArrayList<SistemaAmortizacao> simulationsList) {
        super();
        this.simulationsList = simulationsList;
    }

    /* Override Addtab in order to add the close Button everytime */
    @Override
    public void addTab(String title, Icon icon, Component component, String tip) {
        super.addTab(title, icon, component, tip);
        int count = this.getTabCount() - 1;
        setTabComponentAt(count, new CloseButtonTab(component, title, icon));
    }

    @Override
    public void addTab(String title, Icon icon, Component component) {
        addTab(title, icon, component, null);
    }

    @Override
    public void addTab(String title, Component component) {
        addTab(title, null, component);
    }

    /* Button */
    public class CloseButtonTab extends JPanel {
        private Component tab;

        public CloseButtonTab(final Component tab, String title, Icon icon) {
            this.tab = tab;
            setOpaque(false);

            FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 3, 3);
            setLayout(flowLayout);

            JLabel jLabel = new JLabel(title);
            jLabel.setIcon(icon);
            add(jLabel);

            JButton button = new JButton("X");
            button.setMargin(new Insets(0, 0, 0, 0));
            button.addMouseListener(new CloseListener(tab));
            add(button);
        }
    }

    /* ClickListener */
    public class CloseListener implements MouseListener
    {
        private final Component tab;

        public CloseListener(Component tab){
            this.tab=tab;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getSource() instanceof JButton){
                JButton clickedButton = (JButton) e.getSource();
                JTabbedPane tabbedPane = (JTabbedPane) clickedButton.getParent().getParent().getParent();

                //seleciona a tab que foi clicada para fechar (pode não ser a tab atualmente selecionada)
                tabbedPane.setSelectedComponent(tab);

                //pega o índice da tab atual
                var selectedIndex = tabbedPane.getSelectedIndex();

                //remove o índice do map
                simulationsList.remove(selectedIndex);

                //remove a tab
                tabbedPane.remove(tab);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {
            if(e.getSource() instanceof JButton){
                JButton clickedButton = (JButton) e.getSource();
//                System.out.println("Mouse entered");
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(e.getSource() instanceof JButton){
                JButton clickedButton = (JButton) e.getSource();
//                System.out.println("Mouse exited");
            }
        }
    }
}
