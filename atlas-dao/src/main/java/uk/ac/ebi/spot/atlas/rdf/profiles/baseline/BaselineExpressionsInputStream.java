package uk.ac.ebi.spot.atlas.rdf.profiles.baseline;

import au.com.bytecode.opencsv.CSVReader;
import uk.ac.ebi.spot.atlas.rdf.profiles.TsvInputStream;
import uk.ac.ebi.spot.rdf.model.baseline.BaselineExpression;
import uk.ac.ebi.spot.rdf.model.baseline.BaselineExpressions;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class BaselineExpressionsInputStream extends TsvInputStream<BaselineExpressions, BaselineExpression> {

    public BaselineExpressionsInputStream(CSVReader csvReader, String experimentAccession, BaselineExpressionsQueueBuilder baselineExpressionsQueueBuilder) {
        super(csvReader, experimentAccession, baselineExpressionsQueueBuilder);
    }

    @Override
    protected BaselineExpressions buildObjectFromTsvValues(String[] values) {

        BaselineExpressions baselineExpressions = new BaselineExpressions();
        //we need to reload because the first line can only be used to extract the gene ID
        getTsvRowQueue().reload(removeGeneIDAndNameColumns(values));

        BaselineExpression expression;

        while ((expression = getTsvRowQueue().poll()) != null) {

            baselineExpressions.addExpression(expression);
        }

        return baselineExpressions;
    }

    @Override
    protected BaselineExpressions createProfile() {
        return null;
    }

    @Override
    protected void addExpressionToBuilder(BaselineExpression expression) {

    }

    @Override
    protected void addGeneInfoValueToBuilder(String[] values) {

    }
}
