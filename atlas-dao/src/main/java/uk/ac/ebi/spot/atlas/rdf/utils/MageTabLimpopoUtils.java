package uk.ac.ebi.spot.atlas.rdf.utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.MAGETABInvestigation;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.magetab.parser.MAGETABParser;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class MageTabLimpopoUtils {

    private static final Logger LOGGER = Logger.getLogger(MageTabLimpopoUtils.class);

    @Value("#{configuration['experiment.magetab.idf.url.template']}")
    private String idfUrlTemplate;

    @Value("#{configuration['experiment.magetab.idf.path.template']}")
    private String idfPathTemplate;

    @Value("${data.files.location}")
    private String dataFileLocation;


    public List<String> extractPubMedIdsFromIDF(MAGETABInvestigation investigation) {
        return investigation.IDF.pubMedId;
    }

    public MAGETABInvestigation parseInvestigation(String experimentAccession)
            throws ParseException, MalformedURLException, FileNotFoundException {

        String idfFileLocation = MessageFormat.format(dataFileLocation + idfPathTemplate, experimentAccession);
        LOGGER.info("<parseInvestigation> idfFileLocation = " + idfFileLocation);

        MAGETABParser<MAGETABInvestigation> mageTabParser = new MAGETABParser<>();
        Path idfFilePath = Paths.get(idfFileLocation);
        if (Files.exists(idfFilePath)) {
            LOGGER.info("<parseInvestigation> MAGETAB investigation file exists on the filesystem, going to use it");
            return mageTabParser.parse(idfFilePath.toFile());
        } else {
            LOGGER.debug("<parseInvestigation> MAGETAB investigation file not found on the filesystem, going to use online file");
            throw new FileNotFoundException("<parseInvestigation> MAGETAB investigation file not found on the filesystem:"  + experimentAccession);
//            URL idfFileURL = new URL(MessageFormat.format(idfUrlTemplate, experimentAccession));
//            return mageTabParser.parse(idfFileURL);
        }

    }

    public String extractInvestigationTitle(MAGETABInvestigation investigation) {
        return investigation.IDF.investigationTitle;
    }
}