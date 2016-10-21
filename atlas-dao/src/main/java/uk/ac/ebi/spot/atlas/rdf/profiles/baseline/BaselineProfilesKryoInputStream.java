package uk.ac.ebi.spot.atlas.rdf.profiles.baseline;

import uk.ac.ebi.spot.atlas.rdf.profiles.KryoInputStream;
import uk.ac.ebi.atlas.model.baseline.BaselineExpression;
import uk.ac.ebi.atlas.model.baseline.BaselineProfile;

public class BaselineProfilesKryoInputStream extends KryoInputStream<BaselineProfile, BaselineExpression> {

    private BaselineProfileReusableBuilder baselineProfileReusableBuilder;


    public BaselineProfilesKryoInputStream(BaselineExpressionsKryoReader baselineExpressionsKryoReader, String experimentAccession,
                                           ExpressionsRowRawDeserializerBaselineBuilder expressionsRowDeserializerBaselineBuilder,
                                           BaselineProfileReusableBuilder baselineProfileReusableBuilder) {

        super(baselineExpressionsKryoReader, experimentAccession, expressionsRowDeserializerBaselineBuilder);
        this.baselineProfileReusableBuilder = baselineProfileReusableBuilder;
    }

    @Override
    public BaselineProfile createProfile() {
        BaselineProfile baselineProfile = baselineProfileReusableBuilder.create();
        return baselineProfile.isEmpty() ? null : baselineProfile;
    }

    @Override
    public void addExpressionToBuilder(BaselineExpression expression) {
        baselineProfileReusableBuilder.addExpression(expression);
    }

    @Override
    public void addGeneInfoValueToBuilder(String[] values) {
        baselineProfileReusableBuilder.beginNewInstance(values[0], values[1]);
    }

}
