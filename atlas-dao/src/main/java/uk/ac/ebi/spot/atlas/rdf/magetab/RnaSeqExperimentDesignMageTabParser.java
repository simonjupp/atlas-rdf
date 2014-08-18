package uk.ac.ebi.spot.atlas.rdf.magetab;

import com.google.common.collect.Sets;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.SDRF;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.graph.utils.GraphUtils;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.ScanNode;
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
public class RnaSeqExperimentDesignMageTabParser extends MageTabParser<ScanNode> {

    private static final String ENA_RUN = "ENA_RUN";

    public RnaSeqExperimentDesignMageTabParser(MageTabLimpopoUtils utils) {
        setMageTabLimpopoUtils(utils);
    }
    @Override
    protected Set<NamedSdrfNode<ScanNode>> getAssayNodes(SDRF sdrf) {

        Set<NamedSdrfNode<ScanNode>> namedSdrfNodes = Sets.newLinkedHashSet();
        for (ScanNode scanNode : sdrf.getNodes(ScanNode.class)) {
            namedSdrfNodes.add(new NamedSdrfNode(scanNode.comments.get(ENA_RUN).iterator().next(), scanNode));
        }
        return namedSdrfNodes;
    }

    @Override
    protected Collection<SourceNode> findUpstreamSourceNodes(NamedSdrfNode namedSdrfNode) {
        return GraphUtils.findUpstreamNodes(namedSdrfNode.getSdrfNode(), SourceNode.class);
    }

    @Override
    protected List<FactorValueAttribute> getFactorAttributes(NamedSdrfNode<ScanNode> namedSdrfNode) {
        ScanNode node = namedSdrfNode.getSdrfNode();
        Collection<uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.AssayNode> assayNodes = GraphUtils.findUpstreamNodes(node, uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.AssayNode.class);
        if (assayNodes.size() != 1) {
            throw new IllegalStateException("No assay corresponds to ENA run " + node.comments.get(ENA_RUN));
        }

        uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.AssayNode assayNode = assayNodes.iterator().next();

        return assayNode.factorValues;
    }

    protected void addArrays(ExperimentDesign experimentDesign, Set<NamedSdrfNode<ScanNode>> asseyNodes) {

    }
}

