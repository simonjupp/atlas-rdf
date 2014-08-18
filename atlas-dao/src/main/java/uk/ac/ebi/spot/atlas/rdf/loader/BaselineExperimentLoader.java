package uk.ac.ebi.spot.atlas.rdf.loader;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.util.StringUtils;
import uk.ac.ebi.spot.atlas.rdf.ConfigurationTrader;
import uk.ac.ebi.spot.atlas.rdf.magetab.MageTabParser;
import uk.ac.ebi.spot.atlas.rdf.magetab.MageTabParserOutput;
import uk.ac.ebi.spot.atlas.rdf.magetab.RnaSeqExperimentDesignMageTabParser;
import uk.ac.ebi.spot.atlas.rdf.utils.MageTabLimpopoUtils;
import uk.ac.ebi.spot.rdf.model.*;
import uk.ac.ebi.spot.rdf.model.baseline.*;
import uk.ac.ebi.spot.rdf.model.differential.DifferentialExperiment;

import java.io.IOException;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Simon Jupp
 * @date 08/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class BaselineExperimentLoader {

    private ConfigurationTrader trader;
    private MageTabLimpopoUtils limpopoUtils;
    private final BaselineExperimentExpressionLevelFile baselineExperimentExpressionLevelFile;


    public static final int ASSAY_GROUP_HEADER_START_INDEX = 2;


    public BaselineExperimentLoader(BaselineExperimentExpressionLevelFile baselineExperimentExpressionLevelFile, ConfigurationTrader trader, MageTabLimpopoUtils limpopoUtils) {
        this.baselineExperimentExpressionLevelFile = baselineExperimentExpressionLevelFile;
        this.trader = trader;
        this.limpopoUtils = limpopoUtils;
    }

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


    public BaselineExperiment load(String experimentAccession) {


        try {
            BaselineExperimentConfiguration baselineExperimentConfiguration = getTrader().getFactorsConfiguration(experimentAccession);

            ExperimentConfiguration config = getTrader().getExperimentConfiguration(experimentAccession);
            AssayGroups assayGroups = config.getAssayGroups();

            String[] orderedAssayGroupIds = baselineExperimentExpressionLevelFile.readOrderedAssayGroupIds(experimentAccession);

            MageTabParser mageTabParser = new RnaSeqExperimentDesignMageTabParser(getLimpopoUtils());
            MageTabParserOutput output = mageTabParser.parse(experimentAccession);
            ExperimentDesign design = output.getExperimentDesign();
            String description = output.getDescription();
            Set<String> pubMedIds = output.getPubMedIds();


            ExperimentalFactors experimentalFactors = createExperimentalFactors(design, baselineExperimentConfiguration, assayGroups, orderedAssayGroupIds);

            Set<String> species = Sets.newHashSet();
            for (String assayAccession: config.getAssayAccessions()){
                Collection<SampleValue> assaySamples = design.getSamples(assayAccession);

                checkNotNull(assaySamples, String.format("Assay accession %s does not exist or has no samples", assayAccession));

                for (SampleValue sample : assaySamples){
                    if ("organism".equalsIgnoreCase(sample.getType())){
                        species.add(sample.getValue());
                    }
                }
            }

            return new BaselineExperiment(
                    experimentAccession,
                    null,
                              experimentalFactors,
                    description,
                    baselineExperimentConfiguration.getExperimentDisplayName(),
                    species,
                    new HashMap<String, String>(),
                    false,
                    pubMedIds,
                    design,
                    assayGroups);
        }
        catch (ClassCastException e) {
            throw new RuntimeException("Can't read " + experimentAccession + " configuration, not a differential experiment");
        } catch (IOException e) {
            throw new RuntimeException("Can't parse magetab file for " + experimentAccession, e);
        }

    }

    private ExperimentalFactors createExperimentalFactors(ExperimentDesign experimentDesign, BaselineExperimentConfiguration factorsConfig, AssayGroups assayGroups, String[] orderedAssayGroupIds) {
        String defaultQueryFactorType = factorsConfig.getDefaultQueryFactorType();
        Set<Factor> defaultFilterFactors = factorsConfig.getDefaultFilterFactors();
        Set<String> requiredFactorTypes = getRequiredFactorTypes(defaultQueryFactorType, defaultFilterFactors);
        Map<String, String> factorNamesByType = getFactorDisplayNameByType(experimentDesign.getFactorHeaders(), requiredFactorTypes);

        List<FactorGroup> orderedFactorGroups = extractOrderedFactorGroups(orderedAssayGroupIds, assayGroups, experimentDesign);
        Map<String, FactorGroup> orderedFactorGroupsByAssayGroup = extractOrderedFactorGroupsByAssayGroup(orderedAssayGroupIds, assayGroups, experimentDesign);

        ExperimentalFactorsBuilder experimentalFactorsBuilder = new ExperimentalFactorsBuilder();

        return experimentalFactorsBuilder
                .withOrderedFactorGroups(orderedFactorGroups)
                .withOrderedFactorGroupsByAssayGroup(orderedFactorGroupsByAssayGroup)
                .withMenuFilterFactorTypes(factorsConfig.getMenuFilterFactorTypes())
                .withFactorNamesByType(factorNamesByType)
                .withDefaultQueryType(factorsConfig.getDefaultQueryFactorType())
                .withDefaultFilterFactors(defaultFilterFactors)
                .create();
    }

    Set<String> getRequiredFactorTypes(String defaultQueryFactorType, Set<Factor> defaultFilterFactors) {
        Set<String> requiredFactorTypes = Sets.newHashSet(defaultQueryFactorType);

        for (Factor defaultFilterFactor : defaultFilterFactors) {
            requiredFactorTypes.add(defaultFilterFactor.getType());
        }
        return requiredFactorTypes;
    }

    List<FactorGroup> extractOrderedFactorGroups(String[] orderedAssayGroupIds, final AssayGroups assayGroups, ExperimentDesign experimentDesign) {

        List<FactorGroup> factorGroups = Lists.newArrayList();

        for (String groupId : orderedAssayGroupIds) {
            AssayGroup assayGroup = assayGroups.getAssayGroup(groupId);

            checkNotNull(assayGroup, String.format("No assay group \"%s\"", groupId));

            FactorGroup factorGroup = experimentDesign.getFactors(assayGroup.getFirstAssayAccession());
            factorGroups.add(factorGroup);

        }
        return factorGroups;

    }

    Map<String, FactorGroup> extractOrderedFactorGroupsByAssayGroup(String[] orderedAssayGroupIds, final AssayGroups assayGroups, ExperimentDesign experimentDesign) {

        Map<String, FactorGroup> factorGroups = Maps.newLinkedHashMap();

        for (String groupId : orderedAssayGroupIds) {
            AssayGroup assayGroup = assayGroups.getAssayGroup(groupId);

            FactorGroup factorGroup = experimentDesign.getFactors(assayGroup.getFirstAssayAccession());
            factorGroups.put(groupId, factorGroup);

        }
        return factorGroups;

    }

    protected Map<String, String> getFactorDisplayNameByType(SortedSet<String> factorHeaders, Set<String> requiredFactorTypes) {
        Map<String, String> factorDisplayNameByType = Maps.newHashMap();

        for (String factorType : factorHeaders) {
            String normalizedFactorType = Factor.normalize(factorType);
            if (requiredFactorTypes.contains(normalizedFactorType)) {
                factorDisplayNameByType.put(normalizedFactorType, prettifyFactorType(factorType));
            }
        }
        return factorDisplayNameByType;
    }

    //TODO: move this into ExperimentFactorsLoader
    protected String prettifyFactorType(String factorType) {
        StringBuilder result = new StringBuilder();
        String[] split = factorType.replaceAll("_", " ").split(" ");
        boolean firstTokenCapitalized = false;
        for (String token : split) {
            int nbUpperCase = countUpperCaseLetters(token);
            if (nbUpperCase > 1) {
                result.append(token);
            } else {
                token = token.toLowerCase();

                if (!firstTokenCapitalized) {
                    token = StringUtils.capitalize(token);
                    firstTokenCapitalized = true;
                }
                result.append(token);
            }
            result.append(" ");
        }

        return result.toString().trim();
    }

    protected int countUpperCaseLetters(String token) {
        int nbUpperCase = 0;
        for (int i = 0; i < token.length(); i++) {
            if (Character.isUpperCase(token.charAt(i))) {
                nbUpperCase++;
            }
        }
        return nbUpperCase;
    }



}
