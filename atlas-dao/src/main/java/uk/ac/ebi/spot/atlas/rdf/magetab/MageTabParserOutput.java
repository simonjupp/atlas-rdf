package uk.ac.ebi.spot.atlas.rdf.magetab;

import com.google.common.collect.SetMultimap;
import uk.ac.ebi.spot.rdf.model.ExperimentDesign;

import java.util.Collection;
import java.util.Set;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class MageTabParserOutput {

    private final SetMultimap<String, String> characteristicsOntologyTerms;
    private ExperimentDesign experimentDesign;
    private Set<String> pubMedIds;
    private String description;

    MageTabParserOutput(ExperimentDesign experimentDesign, SetMultimap<String, String> characteristicsOntologyTerms, String description, Set<String> pubMedIds) {
        this.experimentDesign = experimentDesign;
        this.characteristicsOntologyTerms = characteristicsOntologyTerms;
        this.pubMedIds = pubMedIds;
        this.description = description;
    }

    public ExperimentDesign getExperimentDesign() {
        return experimentDesign;
    }

    // ontology terms by assay group ID
    public SetMultimap<String, String> getCharacteristicsOntologyTerms() {
        return characteristicsOntologyTerms;
    }

    public Set<String> getPubMedIds() {
        return pubMedIds;
    }

    public String getDescription() {
        return description;
    }
}