package org.project.db.model.builder;

import org.project.db.model.Instrument;
import org.project.db.model.InstrumentOrder;
import org.project.db.model.builder_interface.InstrumentOrderBuilder;

public class InstrumentOrderBuilderImpl implements InstrumentOrderBuilder {
    private Instrument instrument;
    private double price;
    private int quantity;

    @Override
    public InstrumentOrderBuilder setInstrument(Instrument instrument) {
        this.instrument = instrument;
        return this;
    }

    @Override
    public InstrumentOrderBuilder setPrice(double price) {
        this.price = price;
        return this;
    }

    @Override
    public InstrumentOrderBuilder setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    @Override
    public InstrumentOrder createInstrumentOrder() {
        return new InstrumentOrder(instrument, price, quantity);
    }
}