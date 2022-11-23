package org.project.db.model.builder_interface;

import org.project.db.model.Instrument;
import org.project.db.model.Status;

import java.util.Date;

public interface InstrumentBuilder {
    InstrumentBuilder setId(Long id);

    InstrumentBuilder setDateCreated(Date dateCreated);

    InstrumentBuilder setDateUpdated(Date dateUpdated);

    InstrumentBuilder setDescription(String description);

    InstrumentBuilder setTitle(String title);

    InstrumentBuilder setStatus(Status status);

    InstrumentBuilder setPrice(double price);

    Instrument createInstrument();
}
