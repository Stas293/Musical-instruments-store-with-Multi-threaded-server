package org.project.db.model;

public class Status implements java.io.Serializable {
    private final Long id;
    private String code;
    private String name;
    private boolean closed;

    public Status(Long id, String code, String name, boolean closed) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.closed = closed;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", closed=" + closed +
                '}';
    }
}
