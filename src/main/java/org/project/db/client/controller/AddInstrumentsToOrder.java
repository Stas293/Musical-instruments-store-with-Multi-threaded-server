package org.project.db.client.controller;

import org.project.db.client.view.DatabaseClient;
import org.project.db.dto.OrderDto;
import org.project.db.model.Instrument;
import org.project.db.model.InstrumentOrder;
import org.project.db.model.Status;
import org.project.db.model.builder.InstrumentOrderBuilderImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class AddInstrumentsToOrder implements ActionListener {
    private final List<Instrument> instruments;
    private final JComboBox[] cbQuantitys;
    private final List<InstrumentOrder> instrumentOrders;
    private static final Logger logger = Logger.getLogger(AddInstrumentsToOrder.class.getName());
    private final ObjectInputStream fromServer;
    private final ObjectOutputStream toServer;
    private final DatabaseClient databaseClient;

    public AddInstrumentsToOrder(List<Instrument> instruments, JComboBox[] cbQuantitys, List<InstrumentOrder> instrumentOrders, ObjectInputStream fromServer, ObjectOutputStream toServer, DatabaseClient databaseClient) {
        this.instruments = instruments;
        this.cbQuantitys = cbQuantitys;
        this.instrumentOrders = instrumentOrders;
        this.fromServer = fromServer;
        this.toServer = toServer;
        this.databaseClient = databaseClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            IntStream.range(0, instruments.size()).filter(i -> (Integer) cbQuantitys[i].getSelectedItem() > 0)
                    .mapToObj(i -> new InstrumentOrderBuilderImpl().setInstrument(instruments.get(i)).setPrice(instruments.get(i).getPrice()).setQuantity((Integer) cbQuantitys[i].getSelectedItem()).createInstrumentOrder()).forEachOrdered(instrumentOrders::add);
            if (!instrumentOrders.isEmpty()) {
                toServer.writeObject("makeOrder");
                Status nextstatus = (Status) fromServer.readObject();
                toServer.writeObject(new OrderDto(databaseClient.getUserDto() + " " + new Date(), databaseClient.getUserDto().getLogin(), nextstatus));
                toServer.writeObject(instrumentOrders);
                System.out.println(fromServer.readObject());
                databaseClient.loggedInUser();
            }

        } catch (IOException | ClassNotFoundException e1) {
            logger.log(Level.WARNING, "Exception", e1);
        }
    }
}
