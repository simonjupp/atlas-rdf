package uk.ac.ebi.spot.atlas.rdf.profiles.baseline;

import com.google.common.base.Predicate;
import uk.ac.ebi.spot.rdf.model.baseline.BaselineExpression;
import uk.ac.ebi.spot.rdf.model.baseline.BaselineProfile;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class BaselineProfileReusableBuilder {

    private final String queryFactorType;
    private BaselineProfile baselineProfile;

    private Predicate<BaselineExpression> baselineExpressionFilter;

    public BaselineProfileReusableBuilder(Predicate<BaselineExpression> baselineExpressionFilter,
                                          String queryFactorType) {
        this.baselineExpressionFilter = baselineExpressionFilter;
        this.queryFactorType = queryFactorType;
    }

    public BaselineProfileReusableBuilder beginNewInstance(String geneId, String geneName) {
        baselineProfile = new BaselineProfile(geneId, geneName);
        return this;
    }

    public BaselineProfileReusableBuilder addExpression(BaselineExpression expression) {
        checkState(baselineProfile != null, "Please invoke beginNewInstance before create");
        if (baselineExpressionFilter.apply(expression)) {
            baselineProfile.add(queryFactorType, expression);
        }
        return this;
    }

    public BaselineProfile create() {
        checkState(baselineProfile != null, "Please invoke beginNewInstance before create");
        return baselineProfile;
    }
}