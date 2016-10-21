package uk.ac.ebi.spot.atlas.rdf.profiles.baseline;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import uk.ac.ebi.spot.atlas.rdf.profiles.TsvRowQueue;
import uk.ac.ebi.atlas.model.baseline.BaselineExpression;
import uk.ac.ebi.atlas.model.baseline.FactorGroup;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class BaselineExpressionsQueue extends TsvRowQueue<BaselineExpression> {

    private final int expectedNumberOfValues;
    private Iterator<FactorGroup> factorGroups;

    public BaselineExpressionsQueue(List<FactorGroup> orderedFactorGroups) {
        expectedNumberOfValues = orderedFactorGroups.size();
        factorGroups = Iterables.cycle(orderedFactorGroups).iterator();
    }

    @Override
    public TsvRowQueue reload(String... values) {
        checkArgument(values.length == expectedNumberOfValues, String.format("Expected %s values but got [%s]", expectedNumberOfValues, Joiner.on(",").join(values)));
        return super.reload(values);
    }

    @Override
    public BaselineExpression pollExpression(Queue<String> tsvRow) {
        String expressionLevelString = tsvRow.poll();

        if (expressionLevelString == null) {
            return null;
        }

        return new BaselineExpression(expressionLevelString, factorGroups.next());
    }


}
