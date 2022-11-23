package org.project.db.model.builder_interface;

import org.project.db.model.Instrument;
import org.project.db.model.InstrumentOrder;

public interface InstrumentOrderBuilder {
    InstrumentOrderBuilder setInstrument(Instrument instrument);

    InstrumentOrderBuilder setPrice(double price);

    InstrumentOrderBuilder setQuantity(int quantity);

    InstrumentOrder createInstrumentOrder();
}
