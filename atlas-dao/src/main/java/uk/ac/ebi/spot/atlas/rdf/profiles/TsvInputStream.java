package uk.ac.ebi.spot.atlas.rdf.profiles;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import uk.ac.ebi.spot.atlas.rdf.commons.ObjectInputStream;
import uk.ac.ebi.spot.rdf.model.Expression;

import java.io.IOException;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public abstract class TsvInputStream<T, K extends Expression> implements ObjectInputStream<T> {

    private static final Logger LOGGER = Logger.getLogger(TsvInputStream.class);

    private CSVReader csvReader;

    private TsvRowQueue<K> tsvRowQueue;

    protected TsvInputStream(CSVReader csvReader, String experimentAccession
            , TsvRowQueueBuilder tsvRowQueueBuilder) {

        this.csvReader = csvReader;

        String[] firstCsvLine = readCsvLine();
        String[] headersWithoutGeneIdColumn = removeGeneIDAndNameColumns(firstCsvLine);
        tsvRowQueue = tsvRowQueueBuilder.forExperiment(experimentAccession)
                .withHeaders(headersWithoutGeneIdColumn).build();
    }


    @Override
    public T readNext() {
        T geneProfile;

        do {
            String[] values = readCsvLine();

            if (values == null) {
                return null;
            }
            geneProfile = buildObjectFromTsvValues(values);

        } while (geneProfile == null);

        return geneProfile;

    }

    private String[] readCsvLine() {
        try {
            return csvReader.readNext();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new IllegalStateException("Exception thrown while reading next csv line.", e);
        }
    }


    protected T buildObjectFromTsvValues(String[] values) {

        addGeneInfoValueToBuilder(values);

        //we need to reload because the first line can only be used to extract the gene ID
        getTsvRowQueue().reload(removeGeneIDAndNameColumns(values));

        K expression;

        while ((expression = getTsvRowQueue().poll()) != null) {

            addExpressionToBuilder(expression);

        }

        return createProfile();


    }

    protected abstract T createProfile();

    protected abstract void addExpressionToBuilder(K expression);

    protected abstract void addGeneInfoValueToBuilder(String[] values);

    protected TsvRowQueue<K> getTsvRowQueue() {
        return tsvRowQueue;
    }

    @Override
    public void close() throws IOException {
        csvReader.close();
    }

    protected String[] removeGeneIDAndNameColumns(String[] columns) {
        return (String[]) ArrayUtils.subarray(columns, 2, columns.length);
    }
}
