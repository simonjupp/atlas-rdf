package uk.ac.ebi.spot.atlas.rdf.profiles;

import uk.ac.ebi.atlas.model.Expression;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public abstract class TsvRowQueue<T extends Expression> {


    private Queue<String> tsvRow = new LinkedList<>();


    public TsvRowQueue reload(String... values) {
        checkState(this.tsvRow.isEmpty(), "Reload must be invoked only when readNext returns null");

        Collections.addAll(this.tsvRow, values);

        return this;
    }

    public T poll(){
        return pollExpression(tsvRow);
    }

    protected abstract T pollExpression(Queue<String> expressionLevelsBuffer);

}
