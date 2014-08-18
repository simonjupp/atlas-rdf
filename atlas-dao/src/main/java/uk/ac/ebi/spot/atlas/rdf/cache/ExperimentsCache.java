package uk.ac.ebi.spot.atlas.rdf.cache;

import uk.ac.ebi.spot.rdf.model.Experiment;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public interface ExperimentsCache<T extends Experiment> {

    T getExperiment(String experimentAccession);

    void evictExperiment(String experimentAccession);

    void evictAll();
}