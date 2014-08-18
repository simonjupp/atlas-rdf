package uk.ac.ebi.spot.rdf.exception;

/**
 * @author Simon Jupp
 * @date 02/05/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class UnknownOrganismTypeException extends Exception {
    public UnknownOrganismTypeException() {
        super();
    }

    public UnknownOrganismTypeException(String message) {
        super(message);
    }

    public UnknownOrganismTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownOrganismTypeException(Throwable cause) {
        super(cause);
    }

    protected UnknownOrganismTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
