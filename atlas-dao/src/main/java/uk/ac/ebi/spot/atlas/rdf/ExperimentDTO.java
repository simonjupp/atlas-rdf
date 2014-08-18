package uk.ac.ebi.spot.atlas.rdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.ac.ebi.spot.atlas.rdf.cache.BaselineExperimentsCache;
import uk.ac.ebi.spot.atlas.rdf.cache.MicroarrayExperimentsCache;
import uk.ac.ebi.spot.atlas.rdf.cache.RnaSeqDiffExperimentsCache;
import uk.ac.ebi.spot.atlas.rdf.loader.BaselineProfilesLoader;
import uk.ac.ebi.spot.atlas.rdf.loader.MicroarrayProfilesLoader;
import uk.ac.ebi.spot.atlas.rdf.loader.RnaSeqDiffProfilesLoader;
import uk.ac.ebi.spot.rdf.model.*;
import uk.ac.ebi.spot.rdf.model.baseline.*;
import uk.ac.ebi.spot.rdf.model.differential.*;
import uk.ac.ebi.spot.rdf.model.differential.microarray.MicroarrayExperiment;
import uk.ac.ebi.spot.rdf.model.differential.microarray.MicroarrayProfile;
import uk.ac.ebi.spot.rdf.model.differential.rnaseq.RnaSeqProfile;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class ExperimentDTO {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private ConfigurationTrader trader;

    private MicroarrayExperimentsCache microarrayExperimentCache;
    private MicroarrayProfilesLoader microarrayProfilesLoader;

    private RnaSeqDiffExperimentsCache rnaReqDiffExperimentsCache;
    private RnaSeqDiffProfilesLoader rnaSeqDiffProfilesLoader;

    private BaselineExperimentsCache baselineExperimentsCache;
    private BaselineProfilesLoader baselineProfilesLoader;

    public BaselineExperimentsCache getBaselineExperimentsCache() {
        return baselineExperimentsCache;
    }

    public void setBaselineExperimentsCache(BaselineExperimentsCache baselineExperimentsCache) {
        this.baselineExperimentsCache = baselineExperimentsCache;
    }

    public BaselineProfilesLoader getBaselineProfilesLoader() {
        return baselineProfilesLoader;
    }

    public void setBaselineProfilesLoader(BaselineProfilesLoader baselineProfilesLoader) {
        this.baselineProfilesLoader = baselineProfilesLoader;
    }

    public RnaSeqDiffExperimentsCache getRnaReqDiffExperimentsCache() {
        return rnaReqDiffExperimentsCache;
    }

    public void setRnaReqDiffExperimentsCache(RnaSeqDiffExperimentsCache rnaReqDiffExperimentsCache) {
        this.rnaReqDiffExperimentsCache = rnaReqDiffExperimentsCache;
    }

    public RnaSeqDiffProfilesLoader getRnaSeqDiffProfilesLoader() {
        return rnaSeqDiffProfilesLoader;
    }

    public void setRnaSeqDiffProfilesLoader(RnaSeqDiffProfilesLoader rnaSeqDiffProfilesLoader) {
        this.rnaSeqDiffProfilesLoader = rnaSeqDiffProfilesLoader;
    }

    public MicroarrayProfilesLoader getMicroarrayProfilesLoader () {
        return microarrayProfilesLoader;
    }
    public void setMicroarrayProfilesLoader (MicroarrayProfilesLoader microarrayProfileLoader) {
        this.microarrayProfilesLoader = microarrayProfileLoader;
    }

    public void setMicroarrayExperimentCache(MicroarrayExperimentsCache microarrayExperimentCache) {
        this.microarrayExperimentCache = microarrayExperimentCache;
    }
    public MicroarrayExperimentsCache getMicroarrayExperimentCache() {
        return microarrayExperimentCache;
    }

    public ConfigurationTrader getTrader() {
        return trader;
    }

    public void setTrader(ConfigurationTrader trader) {
        this.trader = trader;
    }

    public CompleteExperiment build (String experimentAccession) {

        ExperimentConfiguration config = getTrader().getExperimentConfiguration(experimentAccession);

        log.info(String.format("Loading %s with accession %s from filesystem", config.getExperimentType().getDescription(), experimentAccession));
        if (config.getExperimentType().isMicroarray()) {
            final MicroarrayExperiment experiment = getMicroarrayExperimentCache().getExperiment(experimentAccession);
            final DifferentialProfilesList<MicroarrayProfile> profiles =  getMicroarrayProfilesLoader().load((MicroarrayExperiment) experiment);
            getMicroarrayExperimentCache().evictExperiment(experimentAccession);
            return new CompleteExperiment<MicroarrayExperiment, DifferentialProfilesList<MicroarrayProfile>>() {
                @Override
                public MicroarrayExperiment getExperiment() {
                    return experiment;
                }

                @Override
                public DifferentialProfilesList<MicroarrayProfile> getGeneProfilesList() {
                    return profiles;
                }
            };

        }
        else if (config.getExperimentType().isDifferential()) {
            final DifferentialExperiment experiment = getRnaReqDiffExperimentsCache().getExperiment(experimentAccession);
            final DifferentialProfilesList<RnaSeqProfile> profiles =  getRnaSeqDiffProfilesLoader().load(experiment);
            getRnaReqDiffExperimentsCache().evictExperiment(experimentAccession);

            return new CompleteExperiment<DifferentialExperiment, DifferentialProfilesList<RnaSeqProfile>>() {
                @Override
                public DifferentialExperiment getExperiment() {
                    return experiment;
                }

                @Override
                public DifferentialProfilesList<RnaSeqProfile> getGeneProfilesList() {
                    return profiles;
                }
            };
        }
        else if (config.getExperimentType().isBaseline()) {
            final BaselineExperiment experiment = getBaselineExperimentsCache().getExperiment(experimentAccession);
            final BaselineProfilesList profiles = getBaselineProfilesLoader().load(experiment);
            getBaselineExperimentsCache().evictExperiment(experimentAccession);

            return new CompleteExperiment<BaselineExperiment, BaselineProfilesList>() {
                @Override
                public BaselineExperiment getExperiment() {
                    return experiment;
                }

                @Override
                public BaselineProfilesList getGeneProfilesList() {
                    return profiles;
                }
            };
        }

        throw new RuntimeException("Can't load experiment: " + experimentAccession + ", unrecognised experiment type");

    }

    private void baselineToString(BaselineExperiment experiment, BaselineProfilesList profiles) {
        System.out.println(String.format("Experiment accesion: %s", experiment.getAccession()));
        System.out.println(String.format("Experiment description: %s", experiment.getDescription()));
        System.out.println(String.format("Experiment type: %s", experiment.getType().getDescription()));
        System.out.println(String.format("Experiment display name: %s", experiment.getDisplayName()));
        System.out.println(String.format("Experiment pubmedids: %s", experiment.getPubMedIds()));
        System.out.println(String.format("Experiment Species: %s", experiment.getFirstSpecies()));
        ExperimentDesign design = experiment.getExperimentDesign();

        for (String runs : experiment.getExperimentRunAccessions()) {
            System.out.println(String.format("Experiment run: %s", runs));
            for (SampleValue sample : design.getSamples(runs)) {
                System.out.println(String.format("\t sample: %s / %s (%s)", sample.getType(),  sample.getType(), sample.getOntologyTerm() ));
            }
            for (Factor factor : design.getFactors(runs)) {
                System.out.println(String.format("\t factor: %s / %s (%s)", factor.getType(), factor.getValue(), factor.getValueOntologyTerm() ));
            }

        }

        for (BaselineProfile profile : profiles) {
            for (Factor factor : profile.getConditions()) {
                if (profile.getExpression(factor).isKnown()) {
                    System.out.println(String.format("basleline factor: %s %s", factor.getType(), factor.getValue()));
                    System.out.println(String.format("Expression: %s %s %s",
                            profile.getId(),
                            profile.getName(),
                            profile.getExpression(factor).getLevel()));
                }
            }
        }
    }

    public void diffToString(DifferentialExperiment experiment, DifferentialProfilesList<RnaSeqProfile> profiles) {
        System.out.println(String.format("Experiment accesion: %s", experiment.getAccession()));
        System.out.println(String.format("Experiment description: %s", experiment.getDescription()));
        System.out.println(String.format("Experiment type: %s", experiment.getType().getDescription()));
        System.out.println(String.format("Experiment display name: %s", experiment.getDisplayName()));
        System.out.println(String.format("Experiment pubmedids: %s", experiment.getPubMedIds()));
        System.out.println(String.format("Experiment Species: %s", experiment.getFirstSpecies()));
        ExperimentDesign design = experiment.getExperimentDesign();
        for (String assays: experiment.getAssayAccessions()) {
            System.out.println(String.format("Experiment assays: %s", assays));
            for (SampleValue sample : design.getSamples(assays)) {
                System.out.println(String.format("\t sample: %s / %s (%s)", sample.getType(),  sample.getType(), sample.getOntologyTerm() ));
            }
            for (Factor factor : design.getFactors(assays)) {
                System.out.println(String.format("\t factor: %s / %s (%s)", factor.getType(), factor.getValue(), factor.getValueOntologyTerm() ));
            }
        }


        for (RnaSeqProfile profile : profiles) {
            for (Contrast contrast : profile.getConditions()) {

                for (String refGroup : contrast.getReferenceAssayGroup()) {
                    System.out.println(String.format("Reference assay group: %s", refGroup));
                }
                for (String control : contrast.getTestAssayGroup()) {
                    System.out.println(String.format("Test assay group: %s", control));
                }

                System.out.println(String.format("Expression: %s %s %s %s %s",
                        profile.getId(),
                        profile.getName(),
                        profile.getExpression(contrast).getPValue(),
                        profile.getExpression(contrast).getFoldChange(),
                        profile.getExpression(contrast).isOverExpressed() ? "UP" : "DOWN"));
            }
        }
    }

    public void microArrayToString(MicroarrayExperiment experiment, DifferentialProfilesList<MicroarrayProfile> profiles) {
        System.out.println(String.format("Experiment accesion: %s", experiment.getAccession()));
        System.out.println(String.format("Experiment description: %s", experiment.getDescription()));
        System.out.println(String.format("Experiment type: %s", experiment.getType().getDescription()));
        System.out.println(String.format("Experiment display name: %s", experiment.getDisplayName()));
        System.out.println(String.format("Experiment pubmedids: %s", experiment.getPubMedIds()));
        System.out.println(String.format("Experiment Species: %s", experiment.getFirstSpecies()));
        ExperimentDesign design = experiment.getExperimentDesign();
        for (String assays: experiment.getAssayAccessions()) {
            System.out.println(String.format("Experiment assays: %s", assays));
            for (SampleValue sample : design.getSamples(assays)) {
                System.out.println(String.format("\t sample: %s / %s (%s)", sample.getType(),  sample.getType(), sample.getOntologyTerm() ));
            }
            for (Factor factor : design.getFactors(assays)) {
                System.out.println(String.format("\t factor: %s / %s (%s)", factor.getType(), factor.getValue(), factor.getValueOntologyTerm() ));
            }
        }


        for (MicroarrayProfile profile : profiles) {
            for (Contrast contrast : profile.getConditions()) {
                System.out.println(String.format("Contrast for array design: %s", contrast.getArrayDesignAccession()));

                for (String refGroup : contrast.getReferenceAssayGroup()) {
                    System.out.println(String.format("Reference assay group: %s", refGroup));
                }
                for (String control : contrast.getTestAssayGroup()) {
                    System.out.println(String.format("Test assay group: %s", control));
                }

                System.out.println(String.format("Expression: %s %s %s %s %s %s %s",
                        profile.getId(),
                        profile.getName(),
                        profile.getDesignElementName(),
                        profile.getExpression(contrast).getTstatistic(),
                        profile.getExpression(contrast).getPValue(),
                        profile.getExpression(contrast).getFoldChange(),
                        profile.getExpression(contrast).isOverExpressed() ? "UP" : "DOWN"));
            }

        }
    }

    public static void main(String[] args) {

        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");


        ConfigurationTrader trader = (ConfigurationTrader) context.getBean("configurationTrader");
        MicroarrayExperimentsCache experimentsCache = (MicroarrayExperimentsCache) context.getBean("microarrayExperimentsCache");
        MicroarrayProfilesLoader maprofile = (MicroarrayProfilesLoader) context.getBean("microarrayProfilesLoader");

        RnaSeqDiffExperimentsCache rnaReqDiffexperimentsCache = (RnaSeqDiffExperimentsCache) context.getBean("rnaSeqDiffExperimentsCache");
        RnaSeqDiffProfilesLoader rnadiffprofile = (RnaSeqDiffProfilesLoader) context.getBean("rnaSeqDiffProfilesLoader");

        BaselineExperimentsCache baselineExperimentsCache = (BaselineExperimentsCache) context.getBean("baselineExperimentsCache");
        BaselineProfilesLoader baselineProfilesLoader= (BaselineProfilesLoader) context.getBean("baselineProfilesLoader");

        ExperimentDTO dto = new ExperimentDTO();
        dto.setTrader(trader);

        dto.setMicroarrayExperimentCache(experimentsCache);
        dto.setMicroarrayProfilesLoader(maprofile);
        dto.setRnaReqDiffExperimentsCache(rnaReqDiffexperimentsCache);
        dto.setRnaSeqDiffProfilesLoader(rnadiffprofile);
        dto.setBaselineExperimentsCache(baselineExperimentsCache);
        dto.setBaselineProfilesLoader(baselineProfilesLoader);


//        dto.setLimpopoUtils(utils);
        // micro array
//        dto.build("E-GEOD-2210");

        // rnaseq diff
//        dto.build("E-GEOD-38400");

        // baseline
        dto.build("E-GEOD-26284");
    }


}
