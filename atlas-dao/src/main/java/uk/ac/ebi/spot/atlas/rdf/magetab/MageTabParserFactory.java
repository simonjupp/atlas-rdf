package uk.ac.ebi.spot.atlas.rdf.magetab;

import uk.ac.ebi.spot.rdf.model.ExperimentType;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class MageTabParserFactory {

    private MicroarrayExperimentDesignMageTabParser microarrayMageTabParser;
    private RnaSeqExperimentDesignMageTabParser rnaSeqMageTabParser;
    private TwoColourExperimentDesignMageTabParser twoColourMageTabParser;

    // TODO: (OM) This is a little bit inefficient because each time we instantiate we end up creating every type of parser,
    // even though we will only use one. Probably better to have the experiment-type specific behaviour in a
    // strategy class that has no dependencies, then they can be easily created with new (), OR change this factory
    // to a singleton which requires making MageTabParser stateless.

    public MageTabParserFactory(MicroarrayExperimentDesignMageTabParser microarrayMageTabParser,
                                RnaSeqExperimentDesignMageTabParser rnaSeqMageTabParser,
                                TwoColourExperimentDesignMageTabParser twoColourMageTabParser) {
        this.microarrayMageTabParser = microarrayMageTabParser;
        this.rnaSeqMageTabParser = rnaSeqMageTabParser;
        this.twoColourMageTabParser = twoColourMageTabParser;
    }

    public MageTabParser create(ExperimentType experimentType)  {
        switch(experimentType){
            case MICROARRAY_1COLOUR_MICRORNA_DIFFERENTIAL:
            case MICROARRAY_1COLOUR_MRNA_DIFFERENTIAL:
                return microarrayMageTabParser;
            case MICROARRAY_2COLOUR_MRNA_DIFFERENTIAL:
                return twoColourMageTabParser;
            case RNASEQ_MRNA_BASELINE:
            case RNASEQ_MRNA_DIFFERENTIAL:
                return rnaSeqMageTabParser;
            default:
                throw new IllegalStateException("Unknown experimentType: " + experimentType);
        }
    }
}
