package uk.ac.ebi.spot.atlas.rdf.magetab;

import com.google.common.collect.Sets;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.SDRF;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.graph.utils.GraphUtils;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.HybridizationNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.SourceNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.attribute.FactorValueAttribute;
import uk.ac.ebi.spot.atlas.rdf.utils.MageTabLimpopoUtils;
import uk.ac.ebi.spot.rdf.model.ExperimentDesign;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class MicroarrayExperimentDesignMageTabParser extends MageTabParser<HybridizationNode> {

    public MicroarrayExperimentDesignMageTabParser(MageTabLimpopoUtils utils) {
        setMageTabLimpopoUtils(utils);
    }

    protected Set<NamedSdrfNode<HybridizationNode>> getAssayNodes(SDRF sdrf) {
        Set<NamedSdrfNode<HybridizationNode>> namedSdrfNodes = Sets.newLinkedHashSet();

        Collection<? extends HybridizationNode> hybridizationNodes = sdrf.getNodes(HybridizationNode.class);

        if (hybridizationNodes.size() == 0) {
            //this is required because of a bug in limpopo...
            hybridizationNodes = sdrf.getNodes(uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.AssayNode.class);
        }

        for (HybridizationNode node : hybridizationNodes) {
            namedSdrfNodes.add(new NamedSdrfNode<>(node.getNodeName(), node));
        }
        return namedSdrfNodes;

    }

    @Override
    protected Collection<SourceNode> findUpstreamSourceNodes(NamedSdrfNode namedSdrfNode) {
        return GraphUtils.findUpstreamNodes(namedSdrfNode.getSdrfNode(), SourceNode.class);
    }

    @Override
    protected List<FactorValueAttribute> getFactorAttributes(NamedSdrfNode<HybridizationNode> namedSdrfNode) {
        return namedSdrfNode.getSdrfNode().factorValues;
    }

    @Override
    protected void addArrays(ExperimentDesign experimentDesign, Set<NamedSdrfNode<HybridizationNode>> namedSdrfNodes) {
        for (NamedSdrfNode<? extends HybridizationNode> namedSdrfNode : namedSdrfNodes) {

            if (namedSdrfNode.getSdrfNode().arrayDesigns.size() != 1) {
                throw new IllegalStateException("Assays with multiple array designs are not supported.");
            }
            experimentDesign.putArrayDesign(namedSdrfNode.getName(), namedSdrfNode.getSdrfNode().arrayDesigns.get(0).getAttributeValue());
        }
    }

}
