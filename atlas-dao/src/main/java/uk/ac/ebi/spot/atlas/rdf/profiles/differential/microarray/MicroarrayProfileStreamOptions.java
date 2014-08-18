package uk.ac.ebi.spot.atlas.rdf.profiles.differential.microarray;

import uk.ac.ebi.spot.atlas.rdf.profiles.DifferentialProfileStreamOptions;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public interface MicroarrayProfileStreamOptions extends DifferentialProfileStreamOptions {

    Iterable<String> getArrayDesignAccessions();
}

