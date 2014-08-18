package uk.ac.ebi.spot.atlas.rdf.cache;

import com.google.common.cache.LoadingCache;
import org.apache.log4j.Logger;
import uk.ac.ebi.spot.atlas.rdf.loader.MicroarrayExperimentLoader;
import uk.ac.ebi.spot.rdf.model.baseline.BaselineExperiment;
import uk.ac.ebi.spot.rdf.model.differential.microarray.MicroarrayExperiment;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class MicroarrayExperimentsCache implements ExperimentsCache<MicroarrayExperiment> {

    private static final Logger LOGGER = Logger.getLogger(MicroarrayExperimentsCache.class);

    private MicroarrayExperimentLoader loader;

    //this is the name of the implementation being injected, required because LoadingCache is an interface
    public MicroarrayExperimentsCache(MicroarrayExperimentLoader loader) {
        this.loader = loader;
    }
    private Map<String, MicroarrayExperiment> experimentMap = new HashMap<>();

    @Override
    public MicroarrayExperiment getExperiment(String experimentAccession) {
        try {

            if (!experimentMap.containsKey(experimentAccession)) {
                experimentMap.put(experimentAccession, loader.load(experimentAccession));
            }

            return experimentMap.get(experimentAccession);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new IllegalStateException(String.format("Exception while loading experiment %s: %s", experimentAccession, e.getMessage()), e.getCause());
        }
    }

    @Override
    public void evictExperiment(String experimentAccession) {

        experimentMap.remove(experimentAccession);

    }

    @Override
    public void evictAll() {

        experimentMap.clear();


    }

}