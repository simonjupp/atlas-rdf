package uk.ac.ebi.spot.atlas.rdf.profiles.baseline;

import com.google.common.collect.ImmutableSetMultimap;
import uk.ac.ebi.spot.atlas.rdf.profiles.ProfileStreamOptions;
import uk.ac.ebi.spot.rdf.model.baseline.Factor;

import java.util.Set;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public interface BaselineProfileStreamOptions extends ProfileStreamOptions {

    Set<String> getSelectedGeneIDs();

    boolean isSpecific();

    Set<Factor> getSelectedQueryFactors();

    Set<Factor> getAllQueryFactors();

    ImmutableSetMultimap<String, String> getGeneSetIdsToGeneIds();

    String getExperimentAccession();

    double getCutoff();

    String getQueryFactorType();

    Set<Factor> getSelectedFilterFactors();

    boolean asGeneSets();
}
