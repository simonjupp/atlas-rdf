package uk.ac.ebi.spot.atlas.rdf.commons;

import java.io.Closeable;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public interface ObjectInputStream<T> extends Closeable {

    // returns null when stream is empty
    T readNext();

}

