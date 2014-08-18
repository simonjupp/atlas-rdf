package uk.ac.ebi.spot.atlas.rdf.profiles.baseline;

import au.com.bytecode.opencsv.CSVReader;
import uk.ac.ebi.spot.atlas.rdf.profiles.TsvInputStream;
import uk.ac.ebi.spot.rdf.model.baseline.BaselineExpression;
import uk.ac.ebi.spot.rdf.model.baseline.BaselineProfile;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class BaselineProfilesInputStream extends TsvInputStream<BaselineProfile, BaselineExpression> {

    private BaselineProfileReusableBuilder baselineProfileReusableBuilder;


    public BaselineProfilesInputStream(CSVReader csvReader, String experimentAccession
            , BaselineExpressionsQueueBuilder baselineExpressionsQueueBuilder
            , BaselineProfileReusableBuilder baselineProfileReusableBuilder) {

        super(csvReader, experimentAccession, baselineExpressionsQueueBuilder);
        this.baselineProfileReusableBuilder = baselineProfileReusableBuilder;
    }

    @Override
    protected BaselineProfile createProfile() {
        BaselineProfile baselineProfile = baselineProfileReusableBuilder.create();
        return baselineProfile.isEmpty() ? null : baselineProfile;
    }

    @Override
    protected void addExpressionToBuilder(BaselineExpression expression) {
        baselineProfileReusableBuilder.addExpression(expression);
    }

    @Override
    protected void addGeneInfoValueToBuilder(String[] values) {
        baselineProfileReusableBuilder.beginNewInstance(values[0], values[1]);
    }

}

