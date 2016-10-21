package uk.ac.ebi.spot.atlas.rdf.profiles.differential.rnaseq;

import uk.ac.ebi.spot.atlas.rdf.cache.RnaSeqDiffExperimentsCache;
import uk.ac.ebi.spot.atlas.rdf.profiles.TsvRowQueue;
import uk.ac.ebi.spot.atlas.rdf.profiles.differential.DifferentialExpressionsQueueBuilder;
import uk.ac.ebi.atlas.model.differential.Contrast;
import uk.ac.ebi.atlas.model.differential.DifferentialExperiment;
import uk.ac.ebi.atlas.model.differential.DifferentialExpression;

import java.util.List;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class RnaSeqExpressionsQueueBuilder extends DifferentialExpressionsQueueBuilder<DifferentialExpression, DifferentialExperiment> {

    public RnaSeqExpressionsQueueBuilder(RnaSeqDiffExperimentsCache experimentsCache) {
        super(experimentsCache);

    }

    @Override
    protected TsvRowQueue<DifferentialExpression> getBufferInstance(List<Contrast> orderedContrasts) {
        return new RnaSeqDiffExpressionsQueue(orderedContrasts);
    }

}
