package uk.ac.ebi.spot.atlas.rdf.profiles.differential.rnaseq;

import au.com.bytecode.opencsv.CSVReader;
import uk.ac.ebi.spot.atlas.rdf.profiles.TsvInputStream;
import uk.ac.ebi.spot.rdf.model.differential.DifferentialExpression;
import uk.ac.ebi.spot.rdf.model.differential.rnaseq.RnaSeqProfile;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class RnaSeqProfileStream extends TsvInputStream<RnaSeqProfile, DifferentialExpression> {

    private RnaSeqProfileReusableBuilder rnaSeqProfileReusableBuilder;

    public RnaSeqProfileStream(CSVReader csvReader, String experimentAccession
            , RnaSeqExpressionsQueueBuilder expressionsBufferBuilder
            , RnaSeqProfileReusableBuilder rnaSeqProfileReusableBuilder) {

        super(csvReader, experimentAccession, expressionsBufferBuilder);
        this.rnaSeqProfileReusableBuilder = rnaSeqProfileReusableBuilder;
    }

    @Override
    protected RnaSeqProfile createProfile() {
        RnaSeqProfile profile = rnaSeqProfileReusableBuilder.create();
        return profile.isEmpty() ? null : profile;
    }

    @Override
    protected void addExpressionToBuilder(DifferentialExpression expression) {
        rnaSeqProfileReusableBuilder.addExpression(expression);
    }

    @Override
    protected void addGeneInfoValueToBuilder(String[] values) {
        rnaSeqProfileReusableBuilder.beginNewInstance(values[0], values[1]);
    }


}

