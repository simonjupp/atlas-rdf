package uk.ac.ebi.spot.atlas.rdf.profiles.differential;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;
import uk.ac.ebi.spot.atlas.rdf.cache.ExperimentsCache;
import uk.ac.ebi.spot.atlas.rdf.profiles.TsvRowQueue;
import uk.ac.ebi.spot.atlas.rdf.profiles.TsvRowQueueBuilder;
import uk.ac.ebi.spot.rdf.model.Expression;
import uk.ac.ebi.spot.rdf.model.differential.Contrast;
import uk.ac.ebi.spot.rdf.model.differential.DifferentialExperiment;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public abstract class DifferentialExpressionsQueueBuilder<T extends Expression, K extends DifferentialExperiment> implements TsvRowQueueBuilder<T> {

    private static final Logger LOGGER = Logger.getLogger(DifferentialExpressionsQueueBuilder.class);
    private ExperimentsCache<K> experimentsCache;
    private String experimentAccession;
    private List<Contrast> orderedContrasts;

    public DifferentialExpressionsQueueBuilder(ExperimentsCache<K> experimentsCache) {
        this.experimentsCache = experimentsCache;
    }

    @Override
    public DifferentialExpressionsQueueBuilder forExperiment(String experimentAccession) {

        this.experimentAccession = experimentAccession;

        return this;

    }

    @Override
    public DifferentialExpressionsQueueBuilder withHeaders(String... tsvFileHeaders) throws ExecutionException {

        LOGGER.debug("<withHeaders> data file headers: " + Arrays.toString(tsvFileHeaders));

        checkState(experimentAccession != null, "Builder not properly initialized!");

        DifferentialExperiment experiment = experimentsCache.getExperiment(experimentAccession);

        List<String> columnHeaders = Arrays.asList(tsvFileHeaders);

        orderedContrasts = new LinkedList<>();
        for (String columnHeader : columnHeaders) {
            if (columnHeader.endsWith(".p-value")) {
                String contrastId = StringUtils.substringBefore(columnHeader, ".");
                orderedContrasts.add(experiment.getContrast(contrastId));
            }
        }

        return this;
    }

    @Override
    public TsvRowQueue<T> build() {

        checkState(!CollectionUtils.isEmpty(orderedContrasts), "Builder state not ready for creating the ExpressionBuffer");

        return getBufferInstance(orderedContrasts);

    }

    protected abstract TsvRowQueue<T> getBufferInstance(List<Contrast> orderedContrasts);
}

