package uk.ac.ebi.spot.atlas.rdf.loader;

import uk.ac.ebi.spot.atlas.rdf.commons.ObjectInputStream;
import uk.ac.ebi.spot.atlas.rdf.profiles.differential.rnaseq.RnaSeqProfileStreamFactory;
import uk.ac.ebi.spot.rdf.model.differential.DifferentialExperiment;
import uk.ac.ebi.spot.rdf.model.differential.DifferentialProfilesList;
import uk.ac.ebi.spot.rdf.model.differential.Regulation;
import uk.ac.ebi.spot.rdf.model.differential.rnaseq.RnaSeqProfile;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class RnaSeqDiffProfilesLoader {

    private RnaSeqProfileStreamFactory diffProfileStreamFactory;

    @Inject
    public RnaSeqDiffProfilesLoader(RnaSeqProfileStreamFactory rnaSeqProfileStreamFactory) {
        this.diffProfileStreamFactory = rnaSeqProfileStreamFactory;
    }

    public DifferentialProfilesList<RnaSeqProfile> load (DifferentialExperiment experiment) {
        ObjectInputStream<RnaSeqProfile> stream = diffProfileStreamFactory.create(experiment.getAccession(), 0.05d, 1d, Regulation.UP_DOWN);
        Collection<RnaSeqProfile> profiles = new HashSet<RnaSeqProfile>();
        RnaSeqProfile profile1;
        while ( (profile1 = stream.readNext()) != null) {
            profiles.add(profile1);
        }
        return new DifferentialProfilesList<>(profiles);
    }
}
