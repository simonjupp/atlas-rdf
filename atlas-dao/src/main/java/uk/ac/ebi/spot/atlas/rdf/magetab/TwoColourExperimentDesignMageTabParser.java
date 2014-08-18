package uk.ac.ebi.spot.atlas.rdf.magetab;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.SDRF;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.graph.utils.GraphUtils;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.HybridizationNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.LabeledExtractNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.SourceNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.attribute.FactorValueAttribute;
import uk.ac.ebi.spot.atlas.rdf.utils.MageTabLimpopoUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class TwoColourExperimentDesignMageTabParser extends MicroarrayExperimentDesignMageTabParser {

    public TwoColourExperimentDesignMageTabParser(MageTabLimpopoUtils utils) {
        super(utils);
    }

    @Override
    protected Set<NamedSdrfNode<HybridizationNode>> getAssayNodes(SDRF sdrf) {
        Set<NamedSdrfNode<HybridizationNode>> namedSdrfNodes = Sets.newLinkedHashSet();

        Collection<? extends HybridizationNode> hybridizationNodes = sdrf.getNodes(HybridizationNode.class);

        if (hybridizationNodes.size() == 0) {
            //this is required because of a bug in limpopo...
            hybridizationNodes = sdrf.getNodes(uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.AssayNode.class);
        }

        for (HybridizationNode node : hybridizationNodes) {
            // create separate node for each each channel
            for (int channelNo = 1; channelNo <= 2; channelNo++) {
                namedSdrfNodes.add(new NamedSdrfNode<>(buildTwoColourExperimentAssayName(node.getNodeName(), sdrf.getLabelForChannel(channelNo)), node, channelNo));
            }
        }
        return namedSdrfNodes;
    }

    @Override
    protected Collection<SourceNode> findUpstreamSourceNodes(NamedSdrfNode namedSdrfNode) {
        Collection<SourceNode> upstreamSources = null;

        for (LabeledExtractNode labeledExtractNode : GraphUtils.findUpstreamNodes(namedSdrfNode.getSdrfNode(), LabeledExtractNode.class)) {
            if (extractLabelFromAssayName(namedSdrfNode.getName()).equals(labeledExtractNode.label.getAttributeValue())) {
                upstreamSources =
                        GraphUtils.findUpstreamNodes(labeledExtractNode, SourceNode.class);
            }
        }
        return upstreamSources;
    }

    @Override
    protected List<FactorValueAttribute> getFactorAttributes(NamedSdrfNode<HybridizationNode> namedSdrfNode) {
        HybridizationNode node = namedSdrfNode.getSdrfNode();

        ImmutableList.Builder<FactorValueAttribute> builder = ImmutableList.builder();

        for (FactorValueAttribute factorValueAttribute : node.factorValues) {
            // only extract factor values for the appropriate channel
            if (factorValueAttribute.scannerChannel == namedSdrfNode.getChannel()) {
                builder.add(factorValueAttribute);
            }
        }
        return builder.build();
    }

    protected String buildTwoColourExperimentAssayName(String assayName, String label) {
        return Joiner.on(".").join(assayName, label);
    }

    protected String extractLabelFromAssayName(String assayName) {
        return StringUtils.substringAfter(assayName, ".");
    }

}

