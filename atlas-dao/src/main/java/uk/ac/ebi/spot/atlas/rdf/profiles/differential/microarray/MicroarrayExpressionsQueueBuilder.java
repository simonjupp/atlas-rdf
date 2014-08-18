package uk.ac.ebi.spot.atlas.rdf.profiles.differential.microarray;

import uk.ac.ebi.spot.atlas.rdf.cache.MicroarrayExperimentsCache;
import uk.ac.ebi.spot.atlas.rdf.profiles.TsvRowQueue;
import uk.ac.ebi.spot.atlas.rdf.profiles.differential.DifferentialExpressionsQueueBuilder;
import uk.ac.ebi.spot.rdf.model.differential.Contrast;
import uk.ac.ebi.spot.rdf.model.differential.microarray.MicroarrayExperiment;
import uk.ac.ebi.spot.rdf.model.differential.microarray.MicroarrayExpression;

import java.util.List;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class MicroarrayExpressionsQueueBuilder extends DifferentialExpressionsQueueBuilder<MicroarrayExpression, MicroarrayExperiment> {

    public MicroarrayExpressionsQueueBuilder(MicroarrayExperimentsCache experimentsCache) {
        super(experimentsCache);
    }

    protected TsvRowQueue<MicroarrayExpression> getBufferInstance(List<Contrast> orderedContrasts) {
        return new MicroarrayExpressionsQueue(orderedContrasts);
    }

}