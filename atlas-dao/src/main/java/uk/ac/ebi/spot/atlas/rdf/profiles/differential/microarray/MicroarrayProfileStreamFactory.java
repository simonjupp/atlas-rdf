package uk.ac.ebi.spot.atlas.rdf.profiles.differential.microarray;

import au.com.bytecode.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import uk.ac.ebi.spot.atlas.rdf.commons.ObjectInputStream;
import uk.ac.ebi.spot.atlas.rdf.commons.SequenceObjectInputStream;
import uk.ac.ebi.spot.atlas.rdf.profiles.differential.IsDifferentialExpressionAboveCutOff;
import uk.ac.ebi.spot.atlas.rdf.utils.CsvReaderFactory;
import uk.ac.ebi.spot.rdf.model.differential.Regulation;
import uk.ac.ebi.spot.rdf.model.differential.microarray.MicroarrayProfile;

import java.text.MessageFormat;
import java.util.Vector;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class MicroarrayProfileStreamFactory {

    @Value("#{configuration['microarray.experiment.data.path.template']}")
    private String experimentDataFileUrlTemplate;

    @Value("${data.files.location}")
    private String dataFileLocation;

    private MicroarrayExpressionsQueueBuilder expressionsQueueBuilder;

    private CsvReaderFactory csvReaderFactory;

    public MicroarrayProfileStreamFactory(MicroarrayExpressionsQueueBuilder expressionsQueueBuilder,
                                          CsvReaderFactory csvReaderFactory) {
        this.expressionsQueueBuilder = expressionsQueueBuilder;
        this.csvReaderFactory = csvReaderFactory;
    }


    public ObjectInputStream<MicroarrayProfile> createForAllArrayDesigns(MicroarrayProfileStreamOptions options) {
        String experimentAccession = options.getExperimentAccession();
        double pValueCutOff = options.getPValueCutOff();
        double foldChangeCutOff = options.getFoldChangeCutOff();
        Regulation regulation = options.getRegulation();
        Iterable<String> arrayDesignAccessions = options.getArrayDesignAccessions();

        return create(experimentAccession, pValueCutOff, foldChangeCutOff, regulation, arrayDesignAccessions);
    }

    public MicroarrayProfileStream create(MicroarrayProfileStreamOptions options, String arrayDesign) {
        String experimentAccession = options.getExperimentAccession();
        double pValueCutOff = options.getPValueCutOff();
        double foldChangeCutOff = options.getFoldChangeCutOff();
        Regulation regulation = options.getRegulation();

        return create(experimentAccession, pValueCutOff, foldChangeCutOff, regulation, arrayDesign);
    }

    public ObjectInputStream<MicroarrayProfile> create(String experimentAccession, double pValueCutOff, double foldChangeCutOff, Regulation regulation, Iterable<String> arrayDesignAccessions) {
        Vector<ObjectInputStream<MicroarrayProfile>> inputStreams = new Vector<>();
        for (String arrayDesignAccession : arrayDesignAccessions) {
            ObjectInputStream<MicroarrayProfile> stream = create(experimentAccession, pValueCutOff, foldChangeCutOff, regulation, arrayDesignAccession);
            inputStreams.add(stream);
        }

        return new SequenceObjectInputStream<>(inputStreams.elements());
    }

    public MicroarrayProfileStream create(String experimentAccession, double pValueCutOff, double foldChangeCutOff, Regulation regulation, String arrayDesignAccession) {
        MicroarrayProfileReusableBuilder profileBuilder = createProfileBuilder(pValueCutOff, foldChangeCutOff, regulation);
        CSVReader csvReader = createCsvReader(experimentAccession, arrayDesignAccession);

        return new MicroarrayProfileStream(csvReader, experimentAccession, expressionsQueueBuilder, profileBuilder);
    }


    private CSVReader createCsvReader(String experimentAccession, String arrayDesignAccession) {
        String tsvFileURL = MessageFormat.format(dataFileLocation + experimentDataFileUrlTemplate, experimentAccession, arrayDesignAccession);
        return csvReaderFactory.createTsvReader(tsvFileURL);
    }

    private MicroarrayProfileReusableBuilder createProfileBuilder(double pValueCutOff, double foldChangeCutOff, Regulation regulation) {
        IsDifferentialExpressionAboveCutOff expressionFilter = new IsDifferentialExpressionAboveCutOff();
        expressionFilter.setPValueCutoff(pValueCutOff);
        expressionFilter.setFoldChangeCutOff(foldChangeCutOff);
        expressionFilter.setRegulation(regulation);

        return new MicroarrayProfileReusableBuilder(expressionFilter);
    }

}
