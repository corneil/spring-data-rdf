package org.springframework.data.rdf.exception;

import com.viceversatech.rdfbeans.exceptions.RDFBeanException;

public class RdfException extends RuntimeException {

    private static final long serialVersionUID = -4012256742713293160L;

    public RdfException(String message, RDFBeanException exception) {
        super(message, exception);
    }

    public RdfException(RDFBeanException exception) {
        super(exception);
    }

}
