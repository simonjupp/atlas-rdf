package uk.ac.ebi.spot.atlas.rdf.profiles.differential.microarray;

import com.google.common.collect.Iterables;
import uk.ac.ebi.spot.atlas.rdf.profiles.TsvRowQueue;
import uk.ac.ebi.atlas.model.differential.Contrast;
import uk.ac.ebi.atlas.model.differential.microarray.MicroarrayExpression;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class MicroarrayExpressionsQueue extends TsvRowQueue<MicroarrayExpression> {


    private Iterator<Contrast> expectedContrasts;

    private List<Contrast> orderedContrasts;

    MicroarrayExpressionsQueue(List<Contrast> orderedContrasts) {
        this.expectedContrasts = Iterables.cycle(orderedContrasts).iterator();
        this.orderedContrasts = orderedContrasts;
    }

    public List<Contrast> getOrderedContrasts() {
        return orderedContrasts;
    }

    public MicroarrayExpression pollExpression(Queue<String> tsvRow) {
        String pValueString = tsvRow.poll();
        if (pValueString == null) {
            return null;
        }

        String tStatisticString = tsvRow.poll();
        checkState(tStatisticString != null, "missing tStatistic column in the analytics file");

        String foldChangeString = tsvRow.poll();
        checkState(foldChangeString != null, "missing fold change column in the analytics file");


        if ("NA".equalsIgnoreCase(pValueString) || "NA".equalsIgnoreCase(tStatisticString) || "NA".equalsIgnoreCase(foldChangeString)) {
            expectedContrasts.next();
            return pollExpression(tsvRow);
        }

        double pValue = parseDouble(pValueString);
        double tStatistic = parseDouble(tStatisticString);
        double foldChange = parseDouble(foldChangeString);

        Contrast contrast = expectedContrasts.next();
        return new MicroarrayExpression(pValue, foldChange, tStatistic, contrast);
    }

    double parseDouble(String value) {
        if (value.equalsIgnoreCase("inf")) {
            return Double.POSITIVE_INFINITY;
        }
        if (value.equalsIgnoreCase("-inf")) {
            return Double.NEGATIVE_INFINITY;
        }
        return Double.parseDouble(value);
    }

}
