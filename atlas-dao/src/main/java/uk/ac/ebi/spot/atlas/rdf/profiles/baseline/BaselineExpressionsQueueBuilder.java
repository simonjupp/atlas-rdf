package uk.ac.ebi.spot.atlas.rdf.profiles.baseline;

import uk.ac.ebi.spot.atlas.rdf.cache.RnaSeqBaselineExperimentsCache;
import uk.ac.ebi.spot.atlas.rdf.profiles.TsvRowQueueBuilder;
import uk.ac.ebi.atlas.model.baseline.BaselineExperiment;
import uk.ac.ebi.atlas.model.baseline.BaselineExpression;

import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class BaselineExpressionsQueueBuilder implements TsvRowQueueBuilder<BaselineExpression> {

    private String experimentAccession;

    private RnaSeqBaselineExperimentsCache experimentsCache;

    public BaselineExpressionsQueueBuilder(RnaSeqBaselineExperimentsCache experimentsCache) {
        this.experimentsCache = experimentsCache;
    }

    @Override
    public BaselineExpressionsQueueBuilder forExperiment(String experimentAccession) {

        this.experimentAccession = experimentAccession;

        return this;

    }

    @Override
    public BaselineExpressionsQueueBuilder withHeaders(String... tsvFileHeaders) {
        //We don't need to process the headers for Baseline
        //because orderedFactorGroups is already available from BaselineExperiment
        return this;
    }

    @Override
    public BaselineExpressionsQueue build() throws ExecutionException {
        checkState(experimentAccession != null, "Please invoke forExperiment before invoking the build method");

        BaselineExperiment baselineExperiment = experimentsCache.getExperiment(experimentAccession);

        //TODO: maybe we should use what we get from withHeaders - then we can remove dependency on experimentsCache
        return new BaselineExpressionsQueue(baselineExperiment.getExperimentalFactors().getFactorGroupsInOrder());

    }

}
