package uk.ac.ebi.spot.atlas.rdf.profiles.baseline;

import com.google.common.base.Predicate;
import org.apache.commons.collections.CollectionUtils;
import uk.ac.ebi.spot.rdf.model.baseline.BaselineExpression;
import uk.ac.ebi.spot.rdf.model.baseline.Factor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class IsBaselineExpressionAboveCutoffAndForFilterFactors implements Predicate<BaselineExpression>, Serializable {

    private double cutoff;

    private Set<Factor> filterFactors = new HashSet<>();

    public IsBaselineExpressionAboveCutoffAndForFilterFactors() {
    }

    public IsBaselineExpressionAboveCutoffAndForFilterFactors setFilterFactors(Set<Factor> filterFactors) {
        this.filterFactors = filterFactors;
        return this;
    }

    public IsBaselineExpressionAboveCutoffAndForFilterFactors setCutoff(double cutoff) {
        this.cutoff = cutoff;
        return this;
    }

    @Override
    public boolean apply(BaselineExpression expression) {
        return !expression.isKnown() || (expression.isGreaterThan(cutoff) && checkFilterFactors(expression));
    }

    protected boolean checkFilterFactors(BaselineExpression expression) {
        return (CollectionUtils.isEmpty(filterFactors)
        || expression.containsAll(filterFactors));
    }


}
