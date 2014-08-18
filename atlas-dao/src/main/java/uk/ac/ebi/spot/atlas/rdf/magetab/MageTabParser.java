package uk.ac.ebi.spot.atlas.rdf.magetab;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.IDF;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.MAGETABInvestigation;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.SDRF;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.AbstractSDRFNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.SourceNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.attribute.CharacteristicsAttribute;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.attribute.FactorValueAttribute;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.attribute.UnitAttribute;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.spot.atlas.rdf.utils.MageTabLimpopoUtils;
import uk.ac.ebi.spot.rdf.model.ExperimentDesign;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public abstract class MageTabParser<T extends AbstractSDRFNode> {

    private static final Set<String> FACTORS_NEEDING_DOSE = Sets.newHashSet("compound", "irradiate");

    private static final String DOSE = "dose";

    private MageTabLimpopoUtils mageTabLimpopoUtils;

    private ValueAndUnitJoiner valueAndUnitJoiner;

    public void setValueAndUnitJoiner(ValueAndUnitJoiner valueAndUnitJoiner) {
        this.valueAndUnitJoiner = valueAndUnitJoiner;
    }

    protected void setMageTabLimpopoUtils(MageTabLimpopoUtils mageTabLimpopoUtils) {
        this.mageTabLimpopoUtils = mageTabLimpopoUtils;
    }

    public MageTabParserOutput parse(String experimentAccession)  throws IOException {

        Set<NamedSdrfNode<T>> namedSdrfNodes;
        ImmutableMap<String, String> factorNamesToType;
        String description = "";
        List<String> pubMedIds = new ArrayList<>();
        try {
            MAGETABInvestigation investigation = mageTabLimpopoUtils.parseInvestigation(experimentAccession);

            description = investigation.IDF.experimentDescription;
            pubMedIds = investigation.IDF.pubMedId;
            namedSdrfNodes = getAssayNodes(investigation.SDRF);
            factorNamesToType = buildFactorNameToTypeMap(investigation.IDF);
        } catch (ParseException | MalformedURLException e) {
            throw new IOException("Cannot read or parse SDRF file: ", e);
        }

        ExperimentDesign experimentDesign = new ExperimentDesign();

        SetMultimap<String, String> characteristicsOntologyTerms = HashMultimap.create();

        for (NamedSdrfNode<T> namedSdrfNode : namedSdrfNodes) {
            SourceNode sourceNode = findFirstUpstreamSourceNode(namedSdrfNode);

            for (CharacteristicsAttribute characteristicsAttribute : sourceNode.characteristics) {
                addCharacteristicToExperimentDesign(experimentDesign, namedSdrfNode.getName(), characteristicsAttribute);
                if (!Strings.isNullOrEmpty(characteristicsAttribute.termAccessionNumber)) {
                    characteristicsOntologyTerms.put(namedSdrfNode.getName(), characteristicsAttribute.termAccessionNumber);
                }
            }

            addFactorValues(experimentDesign, namedSdrfNode, factorNamesToType);
        }

        addArrays(experimentDesign, namedSdrfNodes);

        return new MageTabParserOutput(experimentDesign, characteristicsOntologyTerms, description, new HashSet<String>(pubMedIds));
    }

    private ImmutableMap<String, String> buildFactorNameToTypeMap(IDF idf) {
        Iterator<String> experimentalFactorNames = idf.experimentalFactorName.iterator();
        Iterator<String> experimentalFactorTypes = idf.experimentalFactorType.iterator();

        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

        while (experimentalFactorNames.hasNext()) {
            String name = experimentalFactorNames.next();
            String type = experimentalFactorTypes.next();
            builder.put(name, type);
        }

        return builder.build();
    }

    private void addCharacteristicToExperimentDesign(ExperimentDesign experimentDesign, String name, CharacteristicsAttribute characteristicsAttribute) {
        String value = cleanValueAndUnitIfNeeded(characteristicsAttribute.getNodeName(), characteristicsAttribute.unit);
        experimentDesign.putSample(name, characteristicsAttribute.type, value, characteristicsAttribute.termAccessionNumber);
    }

    private SourceNode findFirstUpstreamSourceNode(NamedSdrfNode<T> namedSdrfNode) {
        Collection<SourceNode> sourceNodes = findUpstreamSourceNodes(namedSdrfNode);

        if (sourceNodes.size() != 1) {
            throw new IllegalStateException("There is no one to one mapping between sdrfNode and sourceNode for sdrfNode: " + namedSdrfNode);
        }

        return sourceNodes.iterator().next();
    }

    protected void addFactorValues(ExperimentDesign experimentDesign, NamedSdrfNode<T> namedSdrfNode, ImmutableMap<String, String> factorNamesToType) {

        String compoundFactorValue = null;
        String compoundFactorName = null;
        String compoundFactorValueOntologyTerm = null;

        for (FactorValueAttribute factorValueAttribute : getFactorAttributes(namedSdrfNode)) {

            String factorName = factorValueAttribute.type; // the SDRF calls this type, but in the IDF the same value is actually factor name
            String factorValue = cleanValueAndUnitIfNeeded(factorValueAttribute.getNodeName(), factorValueAttribute.unit);
            String factorValueOntologyTerm = factorValueAttribute.termAccessionNumber;

            if (isFactorThatHasADose(factorValueAttribute)) {

                compoundFactorName = factorName;
                compoundFactorValue = factorValue;
                compoundFactorValueOntologyTerm = factorValueOntologyTerm;

            } else if (isDoseFactor(factorValueAttribute)) {

                if (StringUtils.isNotEmpty(compoundFactorValue)) {
                    factorValue = Joiner.on(" ").join(compoundFactorValue, factorValue);
                    factorName = compoundFactorName;
                    factorValueOntologyTerm = compoundFactorValueOntologyTerm;

                    compoundFactorName = null;
                    compoundFactorValue = null;
                    compoundFactorValueOntologyTerm = null;
                } else {
                    throw new IllegalStateException(DOSE + " : " + factorValue + " has no corresponding value for any of the following factors: " + FACTORS_NEEDING_DOSE);
                }

            }

            String factorType = factorNamesToType.get(factorName);
            experimentDesign.putFactor(namedSdrfNode.getName(), factorType, factorValue, factorValueOntologyTerm);
        }

        //Add compound factor in a case there was no dose corresponding to it
        if (StringUtils.isNotEmpty(compoundFactorName) && StringUtils.isNotEmpty(compoundFactorValue)) {
            String compoundFactorType = factorNamesToType.get(compoundFactorName);
            experimentDesign.putFactor(namedSdrfNode.getName(), compoundFactorType, compoundFactorValue, compoundFactorValueOntologyTerm);
        }
    }

    private boolean isFactorThatHasADose(FactorValueAttribute factorValueAttribute) {
        return FACTORS_NEEDING_DOSE.contains(factorValueAttribute.type.toLowerCase());
    }

    private boolean isDoseFactor(FactorValueAttribute factorValueAttribute) {
        return DOSE.equals(factorValueAttribute.type.toLowerCase());
    }

    protected String cleanValueAndUnitIfNeeded(String value, UnitAttribute unit) {
        if (!StringUtils.isEmpty(value)) {
            value.replaceAll("( )+", " ").replaceAll("(_)+", "_").trim();
            if (unit != null) {
                if (StringUtils.isEmpty(unit.getAttributeType())) {
                    throw new IllegalStateException("Unable to find unit value for factor value: " + value);
                }
//  todo check this is needed (SJ)  value = valueAndUnitJoiner.pluraliseAndJoin(value, unit.getAttributeValue());
            }
        }
        return value;
    }

    protected abstract List<FactorValueAttribute> getFactorAttributes(NamedSdrfNode<T> sdrfNodeWrapper);

    protected abstract void addArrays(ExperimentDesign experimentDesign, Set<NamedSdrfNode<T>> namedSdrfNodes);

    protected abstract Set<NamedSdrfNode<T>> getAssayNodes(SDRF sdrf);

    protected abstract Collection<SourceNode> findUpstreamSourceNodes(NamedSdrfNode<T> namedSdrfNode);

}

