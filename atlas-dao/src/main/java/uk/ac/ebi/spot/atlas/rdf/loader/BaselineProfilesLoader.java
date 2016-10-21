package uk.ac.ebi.spot.atlas.rdf.loader;

import uk.ac.ebi.spot.atlas.rdf.profiles.ExpressionProfileInputStream;
import uk.ac.ebi.spot.atlas.rdf.profiles.baseline.BaselineProfileInputStreamFactory;
import uk.ac.ebi.spot.rdf.model.baseline.*;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class BaselineProfilesLoader {

    public BaselineProfileInputStreamFactory getBaselineExpressionsInputStreamFactory() {
        return baselineExpressionsInputStreamFactory;
    }

    @Inject
    public void setBaselineExpressionsInputStreamFactory(BaselineProfileInputStreamFactory baselineExpressionsInputStreamFactory) {
        this.baselineExpressionsInputStreamFactory = baselineExpressionsInputStreamFactory;
    }

    private BaselineProfileInputStreamFactory baselineExpressionsInputStreamFactory;


    public BaselineProfilesList load (BaselineExperiment experiment) {
        ExperimentalFactors factors = experiment.getExperimentalFactors();
        Collection<BaselineProfile> profiles = new HashSet<BaselineProfile>();

            for (Factor factor : factors.getAllFactors()) {
                ExpressionProfileInputStream stream = getBaselineExpressionsInputStreamFactory().createBaselineProfileInputStream(
                        experiment.getAccession(),
                        factor.getType(),
                        0.5d,
                        Collections.singleton(factor)
                );

                BaselineProfile profile1;
                while ( (profile1 = (BaselineProfile) stream.readNext()) != null) {
                    profiles.add(profile1);
                }

            }
        return new BaselineProfilesList(profiles);
    }
}
