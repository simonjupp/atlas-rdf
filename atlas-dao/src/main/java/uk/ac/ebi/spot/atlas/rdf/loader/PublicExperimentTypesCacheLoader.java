package uk.ac.ebi.spot.atlas.rdf.loader;

import com.google.common.cache.CacheLoader;
import uk.ac.ebi.spot.atlas.rdf.experimentimport.AtlasExperimentDTO;
import uk.ac.ebi.spot.atlas.rdf.experimentimport.ExperimentDAO;
import uk.ac.ebi.atlas.model.ExperimentType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class PublicExperimentTypesCacheLoader extends CacheLoader<String, ExperimentType> {

    private final ExperimentDAO experimentDAO;

    @Inject
    public PublicExperimentTypesCacheLoader(ExperimentDAO experimentDAO) {
        this.experimentDAO = experimentDAO;
    }

    @Override
    public ExperimentType load(@Nonnull String experimentAccession) {
        AtlasExperimentDTO experimentDTO = experimentDAO.findPublicExperiment(experimentAccession);

        return experimentDTO.getExperimentType();
    }
}
