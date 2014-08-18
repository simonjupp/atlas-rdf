package uk.ac.ebi.spot.atlas.rdf.loader;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import uk.ac.ebi.spot.atlas.rdf.commons.readers.TsvReader;
import uk.ac.ebi.spot.atlas.rdf.commons.readers.TsvReaderBuilder;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class BaselineExperimentExpressionLevelFile {

    private static final int HEADER_LINE_INDEX = 0;

    private final TsvReaderBuilder tsvReaderBuilder;

    @Value("#{configuration['experiment.magetab.path.template']}")
    public String experimentDataFilePathTemplate;

    @Value("${data.files.location}")
    private String dataFileLocation;



    public BaselineExperimentExpressionLevelFile(TsvReaderBuilder tsvReaderBuilder) {
        this.tsvReaderBuilder = tsvReaderBuilder;
    }

    public String[] readOrderedAssayGroupIds(String experimentAccession) {
        TsvReader experimentDataTsvReader = tsvReaderBuilder.withExperimentAccession(experimentAccession).forTsvFilePathTemplate(dataFileLocation + experimentDataFilePathTemplate).build();

        String[] experimentRunHeaders = experimentDataTsvReader.readLine(HEADER_LINE_INDEX);

        return ArrayUtils.subarray(experimentRunHeaders, BaselineExperimentLoader.ASSAY_GROUP_HEADER_START_INDEX, experimentRunHeaders.length);
    }
}
