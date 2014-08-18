package uk.ac.ebi.spot.atlas.rdf.magetab;

import com.google.common.base.Objects;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.AbstractSDRFNode;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class NamedSdrfNode<T extends AbstractSDRFNode> {
    private final int channel;
    private String name;
    private T sdrfNode;

    public NamedSdrfNode(String name, T sdrfNode) {
        this(name, sdrfNode, 1);
    }

    public NamedSdrfNode(String name, T sdrfNode, int channel) {
        this.name = name;
        this.sdrfNode = sdrfNode;
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public T getSdrfNode() {
        return sdrfNode;
    }

    // used to determine factor value in multichannel (ie: Two Colour) experiments
    public int getChannel() {
        return channel;
    }

    @Override
    public boolean equals(Object other){
        if (other == null || getClass() != other.getClass()){
            return false;
        }
        NamedSdrfNode otherSdrfNode = (NamedSdrfNode) other;
        return Objects.equal(name, otherSdrfNode.name);
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(name);
    }

    @Override
    public String toString(){
        return "AssayNode[name:" + name + ", sdrfNode:" + sdrfNode + "]";
    }
}
