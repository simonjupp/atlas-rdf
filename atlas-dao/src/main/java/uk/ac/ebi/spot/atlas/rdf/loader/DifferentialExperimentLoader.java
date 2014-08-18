package uk.ac.ebi.spot.atlas.rdf.loader;

import com.google.common.collect.Sets;
import uk.ac.ebi.spot.atlas.rdf.ConfigurationTrader;
import uk.ac.ebi.spot.atlas.rdf.magetab.MageTabParser;
import uk.ac.ebi.spot.atlas.rdf.magetab.MageTabParserOutput;
import uk.ac.ebi.spot.atlas.rdf.magetab.RnaSeqExperimentDesignMageTabParser;
import uk.ac.ebi.spot.atlas.rdf.utils.MageTabLimpopoUtils;
import uk.ac.ebi.spot.rdf.model.ExperimentConfiguration;
import uk.ac.ebi.spot.rdf.model.ExperimentDesign;
import uk.ac.ebi.spot.rdf.model.SampleValue;
import uk.ac.ebi.spot.rdf.model.differential.DifferentialExperiment;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Simon Jupp
 * @date 08/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class DifferentialExperimentLoader {

    private ConfigurationTrader trader;
    private MageTabLimpopoUtils limpopoUtils;



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


    public DifferentialExperiment load(String experimentAccession) {


        try {
            ExperimentConfiguration config = getTrader().getExperimentConfiguration(experimentAccession);

            MageTabParser mageTabParser = new RnaSeqExperimentDesignMageTabParser(getLimpopoUtils());
            MageTabParserOutput output = mageTabParser.parse(experimentAccession);
            ExperimentDesign design = output.getExperimentDesign();
            String description = output.getDescription();
            Set<String> pubMedIds = output.getPubMedIds();

            Set<String> species = Sets.newHashSet();
            for (String assayAccession: config.getAssayAccessions()){
                Collection<SampleValue> sampleValue = design.getSamples(assayAccession);

                checkNotNull(sampleValue, String.format("Assay accession %s does not exist or has no samples", assayAccession));

                for (SampleValue sample : sampleValue){
                    if ("organism".equalsIgnoreCase(sample.getType())){
                        species.add(sample.getValue());
                    }
                }
            }

            return new DifferentialExperiment(
                    experimentAccession,
                    null,
                    config.getContrasts(),
                    description,
                    false,
                    species,
                    pubMedIds,
                    design );
        }
        catch (ClassCastException e) {
            throw new RuntimeException("Can't read " + experimentAccession + " configuration, not a differential experiment");
        } catch (IOException e) {
            throw new RuntimeException("Can't parse magetab file for " + experimentAccession, e);
        }

    }


}
