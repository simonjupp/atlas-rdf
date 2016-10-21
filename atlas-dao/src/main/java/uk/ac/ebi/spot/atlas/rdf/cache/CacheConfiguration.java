package uk.ac.ebi.spot.atlas.rdf.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.spot.atlas.rdf.loader.DifferentialExperimentsCacheLoader;
import uk.ac.ebi.spot.atlas.rdf.loader.MicroarrayExperimentsCacheLoader;
import uk.ac.ebi.spot.atlas.rdf.loader.ProteomicsBaselineExperimentsCacheLoader;
import uk.ac.ebi.spot.atlas.rdf.loader.PublicExperimentTypesCacheLoader;
import uk.ac.ebi.spot.atlas.rdf.loader.RnaSeqBaselineExperimentsCacheLoader;
import uk.ac.ebi.atlas.model.ExperimentType;
import uk.ac.ebi.atlas.model.baseline.BaselineExperiment;
import uk.ac.ebi.atlas.model.differential.DifferentialExperiment;
import uk.ac.ebi.atlas.model.differential.microarray.MicroarrayExperiment;

import javax.inject.Inject;

@Configuration
public class CacheConfiguration {

    @Bean(name="rnaSeqBaselineExperimentsLoadingCache")
    @Inject
    public LoadingCache<String, BaselineExperiment> baselineExperimentsCache(RnaSeqBaselineExperimentsCacheLoader cacheLoader){

        return CacheBuilder.newBuilder().build(cacheLoader);

    }

    @Bean(name="proteomicsBaselineExperimentsLoadingCache")
    @Inject
    public LoadingCache<String, BaselineExperiment> proteomicsBaselineExperimentsCache(ProteomicsBaselineExperimentsCacheLoader cacheLoader){

        return CacheBuilder.newBuilder().build(cacheLoader);

    }


    @Bean(name="differentialExperimentsLoadingCache")
    @Inject
    public LoadingCache<String, DifferentialExperiment> differentialExperimentsCache(DifferentialExperimentsCacheLoader cacheLoader){

        return CacheBuilder.newBuilder().build(cacheLoader);

    }

    @Bean(name="microarrayExperimentsLoadingCache")
    @Inject
    public LoadingCache<String, MicroarrayExperiment> microarrayExperimentsCache(MicroarrayExperimentsCacheLoader cacheLoader){

        return CacheBuilder.newBuilder().build(cacheLoader);

    }

    @Bean(name="publicExperimentTypesLoadingCache")
    @Inject
    public LoadingCache<String, ExperimentType> experimentTypesCache(PublicExperimentTypesCacheLoader cacheLoader) {

        return CacheBuilder.newBuilder().build(cacheLoader);

    }
}