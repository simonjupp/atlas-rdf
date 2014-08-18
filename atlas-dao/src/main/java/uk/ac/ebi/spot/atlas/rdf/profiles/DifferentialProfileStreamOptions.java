package uk.ac.ebi.spot.atlas.rdf.profiles;

import uk.ac.ebi.spot.rdf.model.differential.Contrast;
import uk.ac.ebi.spot.rdf.model.differential.Regulation;

import java.util.Set;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public interface DifferentialProfileStreamOptions extends ProfileStreamOptions {

    Set<String> getSelectedGeneIDs();

    boolean isSpecific();

    Set<Contrast> getSelectedQueryFactors();

    Set<Contrast> getAllQueryFactors();

    Regulation getRegulation();

    String getExperimentAccession();

    double getPValueCutOff();

    double getFoldChangeCutOff();
}
