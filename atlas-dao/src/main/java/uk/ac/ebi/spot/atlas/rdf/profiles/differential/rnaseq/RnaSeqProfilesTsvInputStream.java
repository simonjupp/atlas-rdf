package uk.ac.ebi.spot.atlas.rdf.profiles.differential.rnaseq;

import au.com.bytecode.opencsv.CSVReader;
import uk.ac.ebi.spot.atlas.rdf.profiles.TsvInputStream;
import uk.ac.ebi.spot.rdf.model.differential.DifferentialExpression;
import uk.ac.ebi.spot.rdf.model.differential.rnaseq.RnaSeqProfile;

public class RnaSeqProfilesTsvInputStream extends TsvInputStream<RnaSeqProfile, DifferentialExpression> {

    private RnaSeqProfileReusableBuilder rnaSeqProfileReusableBuilder;

    public RnaSeqProfilesTsvInputStream(CSVReader csvReader, String experimentAccession
            , ExpressionsRowDeserializerRnaSeqBuilder expressionsRowDeserializerRnaSeqBuilder
            , RnaSeqProfileReusableBuilder rnaSeqProfileReusableBuilder) {

        super(csvReader, experimentAccession, expressionsRowDeserializerRnaSeqBuilder);
        this.rnaSeqProfileReusableBuilder = rnaSeqProfileReusableBuilder;
    }

    @Override
    public RnaSeqProfile createProfile() {
        RnaSeqProfile profile = rnaSeqProfileReusableBuilder.create();
        return profile.isEmpty() ? null : profile;
    }

    @Override
    public void addExpressionToBuilder(DifferentialExpression expression) {
        rnaSeqProfileReusableBuilder.addExpression(expression);
    }

    @Override
    public void addGeneInfoValueToBuilder(String[] values) {
        rnaSeqProfileReusableBuilder.beginNewInstance(values[0], values[1]);
    }


}
