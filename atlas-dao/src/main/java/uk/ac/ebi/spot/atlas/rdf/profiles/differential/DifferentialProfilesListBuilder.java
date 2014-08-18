package uk.ac.ebi.spot.atlas.rdf.profiles.differential;

import uk.ac.ebi.spot.atlas.rdf.profiles.GeneProfilesListBuilder;
import uk.ac.ebi.spot.rdf.model.differential.DifferentialProfile;
import uk.ac.ebi.spot.rdf.model.differential.DifferentialProfilesList;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class DifferentialProfilesListBuilder<P extends DifferentialProfile> implements GeneProfilesListBuilder<DifferentialProfilesList<P>> {

    @Override
    public DifferentialProfilesList<P> create() {
        return new DifferentialProfilesList<>();
    }
}