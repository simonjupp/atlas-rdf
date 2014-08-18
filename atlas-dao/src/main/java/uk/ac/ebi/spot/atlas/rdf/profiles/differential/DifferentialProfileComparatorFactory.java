package uk.ac.ebi.spot.atlas.rdf.profiles.differential;

import uk.ac.ebi.spot.atlas.rdf.profiles.DifferentialProfileStreamOptions;
import uk.ac.ebi.spot.rdf.model.differential.DifferentialProfile;
import uk.ac.ebi.spot.atlas.rdf.profiles.differential.DifferentialProfileComparator;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class DifferentialProfileComparatorFactory<T extends DifferentialProfile> {

    public DifferentialProfileComparator<T> create (DifferentialProfileStreamOptions options) {
        return new DifferentialProfileComparator<>(options.isSpecific(),
                options.getSelectedQueryFactors(),
                options.getAllQueryFactors(),
                options.getRegulation());
    }
}