package uk.ac.ebi.spot.atlas.rdf.profiles;

import uk.ac.ebi.spot.rdf.model.Expression;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public interface TsvRowQueueBuilder<T extends Expression> {

    TsvRowQueueBuilder forExperiment(String experimentAccession);

    TsvRowQueueBuilder withHeaders(String... tsvFileHeaders);

    TsvRowQueue<T> build();

}
