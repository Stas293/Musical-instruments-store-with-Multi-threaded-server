package org.project.db.model;

import org.project.db.model.builder.InstrumentOrderBuilderImpl;
import org.project.db.model.builder_interface.InstrumentOrderBuilder;

public class InstrumentOrder implements java.io.Serializable {
    private Instrument instrument;
    private double price;
    private int quantity;

    public InstrumentOrder(Instrument instrument, double price, int quantity) {
        this.instrument = instrument;
        this.price = price;
        this.quantity = quantity;
    }

    public static InstrumentOrderBuilder builder() {
        return new InstrumentOrderBuilderImpl();
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "InstrumentOrder{" +
                "instrument=" + instrument +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
