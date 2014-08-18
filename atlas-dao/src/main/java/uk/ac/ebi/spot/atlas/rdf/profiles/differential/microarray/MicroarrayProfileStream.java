package uk.ac.ebi.spot.atlas.rdf.profiles.differential.microarray;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.lang.ArrayUtils;
import uk.ac.ebi.spot.atlas.rdf.profiles.TsvInputStream;
import uk.ac.ebi.spot.rdf.model.differential.Contrast;
import uk.ac.ebi.spot.rdf.model.differential.microarray.MicroarrayExpression;
import uk.ac.ebi.spot.rdf.model.differential.microarray.MicroarrayProfile;

import java.util.List;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class MicroarrayProfileStream extends TsvInputStream<MicroarrayProfile, MicroarrayExpression> {

    private MicroarrayProfileReusableBuilder microarrayProfileBuilder;

    public MicroarrayProfileStream(CSVReader csvReader,
                                   String experimentAccession,
                                   MicroarrayExpressionsQueueBuilder expressionsBufferBuilder,
                                   MicroarrayProfileReusableBuilder microarrayProfileBuilder) {

        super(csvReader, experimentAccession, expressionsBufferBuilder);
        this.microarrayProfileBuilder = microarrayProfileBuilder;
    }

    public List<Contrast> getOrderedContrastsPresentInStream() {
        MicroarrayExpressionsQueue tsvRowBuffer = (MicroarrayExpressionsQueue) this.getTsvRowQueue();
        return tsvRowBuffer.getOrderedContrasts();
    }

    protected MicroarrayProfile createProfile() {
        MicroarrayProfile profile = microarrayProfileBuilder.create();
        return profile.isEmpty() ? null : profile;
    }

    protected void addExpressionToBuilder(MicroarrayExpression expression) {
        microarrayProfileBuilder.addExpression(expression);
    }

    @Override
    protected void addGeneInfoValueToBuilder(String[] values) {
        microarrayProfileBuilder.beginNewInstance(values[0], values[1], values[2]);
    }

    protected String[] removeGeneIDAndNameColumns(String[] columns) {
        return (String[]) ArrayUtils.subarray(columns, 3, columns.length);
    }
}

