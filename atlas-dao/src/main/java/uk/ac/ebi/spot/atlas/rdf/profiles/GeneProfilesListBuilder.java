package uk.ac.ebi.spot.atlas.rdf.profiles;

import uk.ac.ebi.spot.rdf.model.GeneProfilesList;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public interface GeneProfilesListBuilder<L extends GeneProfilesList> {

    L create();

}