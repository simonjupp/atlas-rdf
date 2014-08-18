package uk.ac.ebi.spot.atlas.rdf.cache;

import org.apache.log4j.Logger;
import uk.ac.ebi.spot.atlas.rdf.loader.BaselineExperimentLoader;
import uk.ac.ebi.spot.atlas.rdf.loader.DifferentialExperimentLoader;
import uk.ac.ebi.spot.rdf.model.baseline.BaselineExperiment;
import uk.ac.ebi.spot.rdf.model.differential.DifferentialExperiment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class BaselineExperimentsCache implements ExperimentsCache<BaselineExperiment> {

    private static final Logger LOGGER = Logger.getLogger(BaselineExperimentsCache.class);

    private BaselineExperimentLoader loader;

    //this is the name of the implementation being injected, required because LoadingCache is an interface
    public BaselineExperimentsCache(BaselineExperimentLoader loader) {
        this.loader = loader;
    }

    private Map<String, BaselineExperiment> experimentMap = new HashMap<>();
    @Override
    public BaselineExperiment getExperiment(String experimentAccession) {
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
        experimentMap.clear();
    }

    @Override
    public void evictAll() {

        throw new UnsupportedOperationException();


    }

}