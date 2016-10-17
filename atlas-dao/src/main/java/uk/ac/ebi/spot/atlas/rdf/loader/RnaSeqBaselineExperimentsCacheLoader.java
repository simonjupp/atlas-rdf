package uk.ac.ebi.spot.atlas.rdf.loader;

import uk.ac.ebi.spot.atlas.rdf.ConfigurationTrader;
import uk.ac.ebi.spot.atlas.rdf.SpeciesFactory;
import uk.ac.ebi.spot.rdf.model.ExperimentType;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class RnaSeqBaselineExperimentsCacheLoader extends BaselineExperimentsCacheLoader {

    @Inject
    public RnaSeqBaselineExperimentsCacheLoader(RnaSeqBaselineExperimentExpressionLevelFile expressionLevelFile,
                                                ConfigurationTrader configurationTrader,
                                                SpeciesFactory speciesFactory) {
        super(ExperimentType.RNASEQ_MRNA_BASELINE, expressionLevelFile,configurationTrader, speciesFactory);
    }
}
