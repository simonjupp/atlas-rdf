package uk.ac.ebi.spot.atlas.rdf.profiles.differential;

import uk.ac.ebi.spot.atlas.rdf.profiles.ProfileStreamOptions;
import uk.ac.ebi.spot.atlas.rdf.profiles.RankProfiles;
import uk.ac.ebi.atlas.model.GeneProfilesList;
import uk.ac.ebi.atlas.model.Profile;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public interface RankProfilesFactory<P extends Profile, L extends GeneProfilesList<P>, O extends ProfileStreamOptions> {

    RankProfiles<P, L> create(O options);
}
