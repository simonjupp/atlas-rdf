package uk.ac.ebi.spot.atlas.rdf.profiles.baseline;

import au.com.bytecode.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import uk.ac.ebi.spot.atlas.rdf.utils.CsvReaderFactory;
import uk.ac.ebi.spot.rdf.model.baseline.Factor;

import java.text.MessageFormat;
import java.util.Set;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class BaselineProfileInputStreamFactory {

    @Value("#{configuration['experiment.magetab.path.template']}")
    private String baselineExperimentDataFileUrlTemplate;

    @Value("${data.files.location}")
    private String dataFileLocation;


    private BaselineExpressionsQueueBuilder baselineExpressionsQueueBuilder;

    private CsvReaderFactory csvReaderFactory;

    public BaselineProfileInputStreamFactory(BaselineExpressionsQueueBuilder baselineExpressionsQueueBuilder,
                              CsvReaderFactory csvReaderFactory) {
        this.baselineExpressionsQueueBuilder = baselineExpressionsQueueBuilder;
        this.csvReaderFactory = csvReaderFactory;
    }

    public BaselineProfilesInputStream createBaselineProfileInputStream(String experimentAccession, String queryFactorType, double cutOff, Set<Factor> filterFactors) {
        String tsvFileURL = MessageFormat.format(dataFileLocation+ baselineExperimentDataFileUrlTemplate, experimentAccession);
        CSVReader csvReader = csvReaderFactory.createTsvReader(tsvFileURL);

        IsBaselineExpressionAboveCutoffAndForFilterFactors baselineExpressionFilter = new IsBaselineExpressionAboveCutoffAndForFilterFactors();
        baselineExpressionFilter.setCutoff(cutOff);
        baselineExpressionFilter.setFilterFactors(filterFactors);

        BaselineProfileReusableBuilder baselineProfileReusableBuilder = new BaselineProfileReusableBuilder(baselineExpressionFilter, queryFactorType);

        return new BaselineProfilesInputStream(csvReader, experimentAccession, baselineExpressionsQueueBuilder, baselineProfileReusableBuilder);
    }

    public BaselineProfilesInputStream create(BaselineProfileStreamOptions options) {
        String experimentAccession = options.getExperimentAccession();

        double cutOff = options.getCutoff();
        String queryFactorType = options.getQueryFactorType();
        Set<Factor> filterFactors = options.getSelectedFilterFactors();

        return createBaselineProfileInputStream(experimentAccession, queryFactorType, cutOff, filterFactors);
    }
}

