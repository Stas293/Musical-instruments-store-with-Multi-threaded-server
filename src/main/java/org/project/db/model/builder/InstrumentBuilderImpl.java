package org.project.db.model.builder;

import org.project.db.model.Instrument;
import org.project.db.model.Status;
import org.project.db.model.builder_interface.InstrumentBuilder;

import java.util.Date;

public class InstrumentBuilderImpl implements InstrumentBuilder {
    private Long id;
    private Date dateCreated;
    private Date dateUpdated;
    private String description;
    private String title;
    private Status status;
    private double price;

    @Override
    public InstrumentBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public InstrumentBuilder setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    @Override
    public InstrumentBuilder setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
        return this;
    }

    @Override
    public InstrumentBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public InstrumentBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public InstrumentBuilder setStatus(Status status) {
        this.status = status;
        return this;
    }

    @Override
    public InstrumentBuilder setPrice(double price) {
        this.price = price;
        return this;
    }

    @Override
    public Instrument createInstrument() {
        return new Instrument(id, dateCreated, dateUpdated, description, title, status, price);
    }
}