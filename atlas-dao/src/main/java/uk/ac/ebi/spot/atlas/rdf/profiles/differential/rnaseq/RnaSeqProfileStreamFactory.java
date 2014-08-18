package uk.ac.ebi.spot.atlas.rdf.profiles.differential.rnaseq;

import au.com.bytecode.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import uk.ac.ebi.spot.atlas.rdf.profiles.DifferentialProfileStreamOptions;
import uk.ac.ebi.spot.atlas.rdf.profiles.differential.IsDifferentialExpressionAboveCutOff;
import uk.ac.ebi.spot.atlas.rdf.utils.CsvReaderFactory;
import uk.ac.ebi.spot.rdf.model.differential.Regulation;

import java.text.MessageFormat;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class RnaSeqProfileStreamFactory {

    @Value("#{configuration['diff.experiment.data.path.template']}")
    private String experimentDataFileUrlTemplate;

    @Value("${data.files.location}")
    private String dataFileLocation;


    private RnaSeqExpressionsQueueBuilder expressionsQueueBuilder;

    private CsvReaderFactory csvReaderFactory;

    public RnaSeqProfileStreamFactory(RnaSeqExpressionsQueueBuilder expressionsQueueBuilder,
                                      CsvReaderFactory csvReaderFactory) {
        this.expressionsQueueBuilder = expressionsQueueBuilder;
        this.csvReaderFactory = csvReaderFactory;
    }

    public RnaSeqProfileStream create(DifferentialProfileStreamOptions options) {
        String experimentAccession = options.getExperimentAccession();
        double pValueCutOff = options.getPValueCutOff();
        double foldChangeCutOff = options.getFoldChangeCutOff();
        Regulation regulation = options.getRegulation();

        return create(experimentAccession, pValueCutOff, foldChangeCutOff, regulation);
    }

    public RnaSeqProfileStream create(String experimentAccession, double pValueCutOff, double foldChangeCutOff, Regulation regulation) {
        String tsvFileURL = MessageFormat.format(dataFileLocation + experimentDataFileUrlTemplate, experimentAccession);
        CSVReader csvReader = csvReaderFactory.createTsvReader(tsvFileURL);

        IsDifferentialExpressionAboveCutOff expressionFilter = new IsDifferentialExpressionAboveCutOff();
        expressionFilter.setPValueCutoff(pValueCutOff);
        expressionFilter.setFoldChangeCutOff(foldChangeCutOff);
        expressionFilter.setRegulation(regulation);

        RnaSeqProfileReusableBuilder rnaSeqProfileReusableBuilder = new RnaSeqProfileReusableBuilder(expressionFilter);

        return new RnaSeqProfileStream(csvReader, experimentAccession, expressionsQueueBuilder, rnaSeqProfileReusableBuilder);
    }

}

