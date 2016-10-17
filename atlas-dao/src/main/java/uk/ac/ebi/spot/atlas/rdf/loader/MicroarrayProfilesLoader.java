package uk.ac.ebi.spot.atlas.rdf.loader;

import uk.ac.ebi.spot.atlas.rdf.commons.ObjectInputStream;
import uk.ac.ebi.spot.atlas.rdf.profiles.differential.microarray.MicroarrayProfileStreamFactory;
import uk.ac.ebi.spot.rdf.model.differential.DifferentialProfilesList;
import uk.ac.ebi.spot.rdf.model.differential.Regulation;
import uk.ac.ebi.spot.rdf.model.differential.microarray.MicroarrayExperiment;
import uk.ac.ebi.spot.rdf.model.differential.microarray.MicroarrayProfile;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author Simon Jupp
 * @date 08/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class MicroarrayProfilesLoader {

    private MicroarrayProfileStreamFactory microarrayProfileStreamFactory;

    @Inject
    public MicroarrayProfilesLoader(MicroarrayProfileStreamFactory microarrayProfileStreamFactory){
        this.microarrayProfileStreamFactory = microarrayProfileStreamFactory;
    }

    public DifferentialProfilesList<MicroarrayProfile> load (MicroarrayExperiment experiment) {
        ObjectInputStream<MicroarrayProfile> stream = microarrayProfileStreamFactory.create(experiment.getAccession(), 0.05d, 1d, Regulation.UP_DOWN, experiment.getArrayDesignAccessions());
        Collection<MicroarrayProfile> profiles = new HashSet<MicroarrayProfile>();
        MicroarrayProfile profile1;
        while ( (profile1 = stream.readNext()) != null) {
            profiles.add(profile1);
        }
        return new DifferentialProfilesList<>(profiles);
    }

}
