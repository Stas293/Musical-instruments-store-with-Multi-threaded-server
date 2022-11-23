package org.project.db.client;

import org.project.db.dto.OrderDto;
import org.project.db.model.*;
import org.project.db.model.builder.InstrumentOrderBuilderImpl;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class MakeOrderListener implements ActionListener {
    private final DatabaseClient databaseClient;

    private static final Logger logger = Logger.getLogger(MakeOrderListener.class.getName());

    public MakeOrderListener(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            databaseClient.getContentPane().removeAll();
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            databaseClient.toServer.writeObject("getAllInstruments");
            ArrayList<Instrument> originalInstruments = (ArrayList<Instrument>) databaseClient.fromServer.readObject();
            ArrayList<Instrument> instruments = originalInstruments.stream().filter(originalInstrument ->
                    originalInstrument.getStatus().getName().equals("Available")).collect(Collectors.toCollection(ArrayList::new));
            infoPanel.setLayout(new GridLayout(instruments.size() + 1, 6 + 1));
            addTableLabels(infoPanel);
            JLabel lbQuantity = new JLabel("quantity");
            infoPanel.add(lbQuantity);
            JComboBox<Integer> cbQuantity = new JComboBox<>();
            IntStream.rangeClosed(0, 10).forEachOrdered(cbQuantity::addItem);
            JComboBox[] cbQuantitys = new JComboBox[instruments.size()];
            addInstrumentsToTable(infoPanel, instruments, cbQuantitys);
            JScrollPane scrollPanel = new JScrollPane(infoPanel);
            ArrayList<InstrumentOrder> instrumentOrders = new ArrayList<>();
            JPanel controlPanel = new JPanel();
            JButton btMakeOrder = new JButton("Make order");
            btMakeOrder.addActionListener(new AddInstrumentsToOrder(instruments, cbQuantitys, instrumentOrders).invoke());
            mainPanel.add(scrollPanel, BorderLayout.CENTER);
            JButton btBack = new JButton(Constants.BACK);
            btBack.addActionListener((event) -> databaseClient.loggedInUser());
            controlPanel.add(btBack);
            controlPanel.add(btMakeOrder);
            mainPanel.add(controlPanel, BorderLayout.SOUTH);
            databaseClient.add(mainPanel);
            databaseClient.setSize(600, 300);
            databaseClient.repaint();
        } catch (IOException | ClassNotFoundException e1) {
            logger.log(Level.WARNING, "Exception", e1);
        }
    }

    private static void addInstrumentsToTable(JPanel infoPanel, ArrayList<Instrument> instruments, JComboBox[] cbQuantitys) {
        for (int i = 0; i < instruments.size(); i++) {
            JLabel lbId1 = new JLabel(instruments.get(i).getId() + "");
            infoPanel.add(lbId1);
            lbId1.setPreferredSize(new Dimension(1, 1));
            JLabel lbTitle1 = new JLabel(instruments.get(i).getTitle());
            infoPanel.add(lbTitle1);
            lbTitle1.setPreferredSize(new Dimension(1, 1));
            JLabel lbDescription1 = new JLabel(instruments.get(i).getDescription());
            infoPanel.add(lbDescription1);
            lbDescription1.setPreferredSize(new Dimension(1, 1));
            JLabel lbStatus1 = new JLabel(instruments.get(i).getStatus().getName());
            infoPanel.add(lbStatus1);
            lbStatus1.setPreferredSize(new Dimension(1, 1));
            JLabel lbPrice1 = new JLabel(instruments.get(i).getPrice() + "");
            infoPanel.add(lbPrice1);
            lbPrice1.setPreferredSize(new Dimension(1, 1));
            JLabel lbDateUpdated1 = new JLabel(instruments.get(i).getDateUpdated() + "");
            infoPanel.add(lbDateUpdated1);
            lbDateUpdated1.setPreferredSize(new Dimension(1, 1));
            cbQuantitys[i] = new JComboBox<>();
            for (int j = 0; j <= 10; j++) {
                cbQuantitys[i].addItem(j);
            }
            infoPanel.add(cbQuantitys[i]);
        }
    }

    static void addTableLabels(JPanel infoPanel) {
        addIdTitleDescription(infoPanel);
        JLabel lbStatus = new JLabel(LabelConstants.STATUS);
        infoPanel.add(lbStatus);
        lbStatus.setPreferredSize(new Dimension(1, 1));
        JLabel lbPrice = new JLabel(LabelConstants.PRICE);
        infoPanel.add(lbPrice);
        lbPrice.setPreferredSize(new Dimension(1, 1));
        JLabel lbDateUpdated = new JLabel(LabelConstants.DATE_UPDATED);
        infoPanel.add(lbDateUpdated);
    }

    static void addIdTitleDescription(JPanel infoPanel) {
        JLabel lbId = new JLabel(LabelConstants.ID);
        infoPanel.add(lbId);
        lbId.setPreferredSize(new Dimension(1, 1));
        JLabel lbTitle = new JLabel(LabelConstants.TITLE);
        infoPanel.add(lbTitle);
        lbTitle.setPreferredSize(new Dimension(1, 1));
        JLabel lbDescription = new JLabel(LabelConstants.DESCRIPTION);
        infoPanel.add(lbDescription);
        lbDescription.setPreferredSize(new Dimension(1, 1));
    }

    private class AddInstrumentsToOrder {
        private final List<Instrument> instruments;
        private final JComboBox[] cbQuantitys;
        private final List<InstrumentOrder> instrumentOrders;

        public AddInstrumentsToOrder(List<Instrument> instruments, JComboBox[] cbQuantitys, List<InstrumentOrder> instrumentOrders) {
            this.instruments = instruments;
            this.cbQuantitys = cbQuantitys;
            this.instrumentOrders = instrumentOrders;
        }

        public ActionListener invoke() {
            return event -> {
                try {
                    IntStream.range(0, instruments.size()).filter(i -> (Integer) cbQuantitys[i].getSelectedItem() > 0)
                            .mapToObj(i -> new InstrumentOrderBuilderImpl().setInstrument(instruments.get(i)).setPrice(instruments.get(i).getPrice()).setQuantity((Integer) cbQuantitys[i].getSelectedItem()).createInstrumentOrder()).forEachOrdered(instrumentOrders::add);
                    if (!instrumentOrders.isEmpty()) {
                        databaseClient.toServer.writeObject("makeOrder");
                        Status nextstatus = (Status) databaseClient.fromServer.readObject();
                        databaseClient.toServer.writeObject(new OrderDto(databaseClient.getUserDto() + " " + new Date(), databaseClient.getUserDto().getLogin(), nextstatus));
                        databaseClient.toServer.writeObject(instrumentOrders);
                        System.out.println(databaseClient.fromServer.readObject());
                        databaseClient.loggedInUser();
                    }

                } catch (IOException | ClassNotFoundException e1) {
                    logger.log(Level.WARNING, "Exception", e1);
                }
            };
        }
    }
}
