package org.springframework.data.rdf.exception;

public class RdfException extends RuntimeException {
    private static final long serialVersionUID = 7810889795588640177L;

    public RdfException() {
        super();

    }

    public RdfException(String message) {
        super(message);
    }

    public RdfException(String message, Throwable e) {
        super(message, e);
    }

    public RdfException(Throwable e) {
        super(e);
    }

}
