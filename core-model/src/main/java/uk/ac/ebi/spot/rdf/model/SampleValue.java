package uk.ac.ebi.spot.rdf.model;

/**
 * @author Simon Jupp
 * @date 13/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class SampleValue {
    private String type;
    private String value;

    public String getOntologyTerm() {
        return ontologyTerm;
    }

    public void setOntologyTerm(String ontologyTerm) {
        this.ontologyTerm = ontologyTerm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    String ontologyTerm;

    public SampleValue(String type, String value, String ontologyTerm) {
        this.type = type;
        this.value = value;
        this.ontologyTerm = ontologyTerm;
    }

}