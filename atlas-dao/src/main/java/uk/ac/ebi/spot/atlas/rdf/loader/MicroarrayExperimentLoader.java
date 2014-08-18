package uk.ac.ebi.spot.atlas.rdf.loader;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Value;
import uk.ac.ebi.spot.atlas.rdf.ConfigurationTrader;
import uk.ac.ebi.spot.atlas.rdf.magetab.MageTabParser;
import uk.ac.ebi.spot.atlas.rdf.magetab.MageTabParserOutput;
import uk.ac.ebi.spot.atlas.rdf.magetab.MicroarrayExperimentDesignMageTabParser;
import uk.ac.ebi.spot.atlas.rdf.magetab.TwoColourExperimentDesignMageTabParser;
import uk.ac.ebi.spot.atlas.rdf.utils.MageTabLimpopoUtils;
import uk.ac.ebi.spot.rdf.model.ExperimentConfiguration;
import uk.ac.ebi.spot.rdf.model.ExperimentDesign;
import uk.ac.ebi.spot.rdf.model.ExperimentType;
import uk.ac.ebi.spot.rdf.model.SampleValue;
import uk.ac.ebi.spot.rdf.model.differential.microarray.MicroarrayExperiment;
import uk.ac.ebi.spot.rdf.model.differential.microarray.MicroarrayExperimentConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Simon Jupp
 * @date 08/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class MicroarrayExperimentLoader {

    private ConfigurationTrader trader;
    private MageTabLimpopoUtils limpopoUtils;

    @Value("#{configuration['microarray.log-fold-changes.data.path.template']}")
    private String logFoldChangePathTemplate;

    @Value("${data.files.location}")
    private String dataFileLocation;


    public MageTabLimpopoUtils getLimpopoUtils() {
        return limpopoUtils;
    }

    public void setLimpopoUtils(MageTabLimpopoUtils limpopoUtils) {
        this.limpopoUtils = limpopoUtils;
    }

    public ConfigurationTrader getTrader() {
        return trader;
    }

    public void setTrader(ConfigurationTrader trader) {
        this.trader = trader;
    }


    public MicroarrayExperiment load(String experimentAccession) {


        try {
            MicroarrayExperimentConfiguration config = (MicroarrayExperimentConfiguration) getTrader().getExperimentConfiguration(experimentAccession);
            SortedSet<String> arrayDesignAccessions =  config.getArrayDesignAccessions();


            String logFoldChangeFileLocation = MessageFormat.format(dataFileLocation + logFoldChangePathTemplate, experimentAccession, arrayDesignAccessions.first());
            boolean hasLogFoldChangeFile = Files.exists(Paths.get(logFoldChangeFileLocation));

            MageTabParser mageTabParser = null;
            if (config.getExperimentType().isTwoColour()) {
                mageTabParser = new TwoColourExperimentDesignMageTabParser(getLimpopoUtils());
            }
            else {
                mageTabParser = new MicroarrayExperimentDesignMageTabParser(getLimpopoUtils());
            }
            MageTabParserOutput output = mageTabParser.parse(experimentAccession);
            ExperimentDesign design = output.getExperimentDesign();
            String description = output.getDescription();
            Set<String> pubMedIds = output.getPubMedIds();

            Set<String> species = Sets.newHashSet();
            for (String assayAccession: config.getAssayAccessions()){
                Collection<SampleValue> assaySamples = design.getSamples(assayAccession);

                checkNotNull(assaySamples, String.format("Assay accession %s does not exist or has no samples", assayAccession));

                for (SampleValue sample : assaySamples) {
                    if ("organism".equalsIgnoreCase(sample.getType())){
                        species.add(sample.getValue());
                    }
                }
            }

            return new MicroarrayExperiment(
                    config.getExperimentType(),
                    experimentAccession,
                    null,
                    config.getContrasts(),
                    description,
                    false,
                    species,
                    arrayDesignAccessions,
                    hasLogFoldChangeFile,
                    pubMedIds,
                    design );

        }
        catch (ClassCastException e) {
            throw new RuntimeException("Can't read " + experimentAccession + " configuration, not a micro array experiment");
        } catch (IOException e) {
            throw new RuntimeException("Can't parse magetab file for " + experimentAccession, e);
        }

    }


}
