package uk.ac.ebi.spot.atlas.rdf.profiles.baseline;

import au.com.bytecode.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import uk.ac.ebi.spot.atlas.rdf.commons.ObjectInputStream;
import uk.ac.ebi.spot.atlas.rdf.utils.CsvReaderFactory;
import uk.ac.ebi.spot.rdf.model.baseline.BaselineExpressions;

import java.text.MessageFormat;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class BaselineExpressionsInputStreamFactory {

    @Value("#{configuration['experiment.magetab.path.template']}")
    private String baselineExperimentDataFileUrlTemplate;

    @Value("${data.files.location}")
    private String dataFileLocation;


    private BaselineExpressionsQueueBuilder baselineExpressionsQueueBuilder;

    private CsvReaderFactory csvReaderFactory;

    public BaselineExpressionsInputStreamFactory(BaselineExpressionsQueueBuilder baselineExpressionsQueueBuilder,
                                                 CsvReaderFactory csvReaderFactory) {
        this.baselineExpressionsQueueBuilder = baselineExpressionsQueueBuilder;
        this.csvReaderFactory = csvReaderFactory;
    }

    public ObjectInputStream<BaselineExpressions> createGeneExpressionsInputStream(String experimentAccession) {
        String tsvFileURL = MessageFormat.format(dataFileLocation + baselineExperimentDataFileUrlTemplate, experimentAccession);
        CSVReader csvReader = csvReaderFactory.createTsvReader(tsvFileURL);
        return new BaselineExpressionsInputStream(csvReader, experimentAccession, baselineExpressionsQueueBuilder);
    }

}
