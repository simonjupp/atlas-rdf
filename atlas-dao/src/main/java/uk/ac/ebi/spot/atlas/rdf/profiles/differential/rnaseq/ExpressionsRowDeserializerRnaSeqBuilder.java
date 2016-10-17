package uk.ac.ebi.spot.atlas.rdf.profiles.differential.rnaseq;

import org.springframework.context.annotation.Scope;
import uk.ac.ebi.spot.atlas.rdf.cache.RnaSeqDiffExperimentsCache;
import uk.ac.ebi.spot.atlas.rdf.profiles.ExpressionsRowTsvDeserializer;
import uk.ac.ebi.spot.atlas.rdf.profiles.differential.ExpressionsRowDeserializerDifferentialBuilder;
import uk.ac.ebi.spot.rdf.model.differential.Contrast;
import uk.ac.ebi.spot.rdf.model.differential.DifferentialExperiment;
import uk.ac.ebi.spot.rdf.model.differential.DifferentialExpression;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@Scope("prototype")
public class ExpressionsRowDeserializerRnaSeqBuilder extends ExpressionsRowDeserializerDifferentialBuilder<DifferentialExpression, DifferentialExperiment> {

    @Inject
    public ExpressionsRowDeserializerRnaSeqBuilder(RnaSeqDiffExperimentsCache experimentsCache) {
        super(experimentsCache);

    }

    @Override
    protected ExpressionsRowTsvDeserializer<DifferentialExpression> getBufferInstance(List<Contrast> orderedContrasts) {
        return new ExpressionsRowTsvDeserializerRnaSeq(orderedContrasts);
    }

}