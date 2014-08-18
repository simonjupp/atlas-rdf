package uk.ac.ebi.spot.atlas.rdf;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import uk.ac.ebi.spot.rdf.model.ExperimentConfiguration;
import uk.ac.ebi.spot.rdf.model.ExperimentType;
import uk.ac.ebi.spot.rdf.model.baseline.BaselineExperimentConfiguration;
import uk.ac.ebi.spot.rdf.model.differential.microarray.MicroarrayExperimentConfiguration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class ConfigurationTrader {

    @Value("#{configuration['experiment.factors.path.template']}")
    private String baselineFactorsConfigurationPathTemplate;

    @Value("#{configuration['experiment.configuration.path.template']}")
    private String configurationPathTemplate;

    @Value("${data.files.location}")
    private String dataFileLocation;

    public BaselineExperimentConfiguration getFactorsConfiguration(String experimentAccession) {
        XMLConfiguration xmlConfiguration = getXmlConfiguration(dataFileLocation + baselineFactorsConfigurationPathTemplate, experimentAccession, true);
        return new BaselineExperimentConfiguration(xmlConfiguration);
    }

    public ExperimentConfiguration getExperimentConfiguration (String experimentAccession) {

        XMLConfiguration xmlConfiguration = getXmlConfiguration(dataFileLocation + configurationPathTemplate, experimentAccession, false);
        xmlConfiguration.setExpressionEngine(new XPathExpressionEngine());
        Document document = parseConfigurationXml(experimentAccession);

        ExperimentConfiguration config = new ExperimentConfiguration(xmlConfiguration, document);
        if (config.getExperimentType().isMicroarray()) {
            return new MicroarrayExperimentConfiguration(xmlConfiguration, document);
        }
        return config;
    }

    private Document parseConfigurationXml(String experimentAccession) {
        Path path = Paths.get(MessageFormat.format(dataFileLocation + configurationPathTemplate, experimentAccession));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(Files.newInputStream(path));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new IllegalStateException("Problem parsing configuration file: " + path.toString(), e);
        }
    }
    private XMLConfiguration getXmlConfiguration(String pathTemplate, String experimentAccession, boolean splitOnComma) {
        Path path = Paths.get(MessageFormat.format(pathTemplate, experimentAccession));

        File file = path.toFile();

        if (!file.exists()) {
            throw new IllegalStateException("Configuration file " + path.toString() + " does not exist");
        }

        try {
            XMLConfiguration xmlConfiguration = new XMLConfiguration();
            if (!splitOnComma) {
                xmlConfiguration.setDelimiterParsingDisabled(true);
            }
            xmlConfiguration.load(path.toFile());
            return xmlConfiguration;
        } catch (ConfigurationException cex) {
            throw new IllegalStateException("Cannot read configuration from path " + path.toString(), cex);
        }
    }
}
