package uk.ac.ebi.spot.atlas.rdf.cache;

import uk.ac.ebi.spot.rdf.model.Experiment;

import java.util.concurrent.ExecutionException;

public interface ExperimentsCache<T extends Experiment> {

    T getExperiment(String experimentAccession) throws ExecutionException;

    void evictExperiment(String experimentAccession);

    void evictAll();
}
