package uk.ac.ebi.spot.atlas.rdf.profiles.differential.rnaseq;

import com.google.common.base.Predicate;
import uk.ac.ebi.atlas.model.differential.DifferentialExpression;
import uk.ac.ebi.atlas.model.differential.rnaseq.RnaSeqProfile;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class RnaSeqProfileReusableBuilder {

    private RnaSeqProfile profile;

    private Predicate<DifferentialExpression> expressionFilter;

    public RnaSeqProfileReusableBuilder(Predicate<DifferentialExpression> expressionFilter) {
        this.expressionFilter = expressionFilter;
    }

    public RnaSeqProfileReusableBuilder beginNewInstance(String geneId, String geneName) {
        profile = new RnaSeqProfile(geneId, geneName);
        return this;
    }

    public RnaSeqProfileReusableBuilder addExpression(DifferentialExpression expression) {
        checkState(profile != null, "Please invoke beginNewInstance before create");
        if (expressionFilter.apply(expression)) {
            profile.add(expression);
        }
        return this;
    }

    public RnaSeqProfile create() {
        checkState(profile != null, "Please invoke beginNewInstance before create");
        return profile;
    }
}
