package uk.ac.ebi.spot.rdf.builder;


import uk.ac.ebi.spot.rdf.model.AssayGroup;
import uk.ac.ebi.spot.rdf.model.ExperimentDesign;
import uk.ac.ebi.spot.rdf.model.GeneProfilesList;
import uk.ac.ebi.spot.rdf.model.baseline.BaselineExperiment;
import uk.ac.ebi.spot.rdf.model.baseline.BaselineExpression;
import uk.ac.ebi.spot.rdf.model.baseline.BaselineProfile;
import uk.ac.ebi.spot.rdf.model.baseline.Factor;
import uk.ac.ebi.spot.rdf.utils.HashingIdGenerator;

import java.net.URI;
import java.util.*;

/**
 * @author Simon Jupp
 * @date 28/07/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class RnaSeqBaselineRDFBuilder extends AbstractExperimentBuilder<BaselineExperiment, GeneProfilesList<BaselineProfile>> {

    protected RnaSeqBaselineRDFBuilder(URIProvider uriProvider, AssertionBuilder builder) {
        super(uriProvider, builder);
    }

    @Override
    public void build(BaselineExperiment experiment, GeneProfilesList<BaselineProfile> profiles) {
        super.buildExperimentDescription(experiment);
        buildExperimentDesign(experiment);
        buildBaseline(experiment, profiles);
    }

    public void buildBaseline(BaselineExperiment experiment, GeneProfilesList<BaselineProfile> profiles) {

        AssertionBuilder builder = getBuilder();

        String id = HashingIdGenerator.generateHashEncodedID(experiment.getAssayGroups().toString());
        URI analysisURI = getUriProvider().getAnalysisUri(experiment.getAccession(), id);

        builder.createTypeInstance(
                analysisURI,
                getUriProvider().getRNASeqBaselineAnalysisTypeUri()
        );

        // link experiment to analysis
        URI experimentUri = getUriProvider().getExperimentUri(experiment.getAccession());
        builder.createObjectPropertyAssertion(
                experimentUri,
                getUriProvider().getExperimentToAnalysisRel(),
                analysisURI
        );

        // get the groups and link to assays,
        for (AssayGroup group : experiment.getAssayGroups()) {
            for (String assay : group) {
                URI assayUri = getUriProvider().getAssayUri(experiment.getAccession(), assay);
                builder.createObjectPropertyAssertion(
                        analysisURI,
                        getUriProvider().getReferenceAssayRelUri(),
                        assayUri
                );
            }
        }



        for (BaselineProfile profile : profiles) {
            for (Factor factor : profile.getConditions()) {

                BaselineExpression expression = profile.getExpression(factor);

                if (!expression.isKnown()) {
                    continue;
                }
                String frag = HashingIdGenerator.generateHashEncodedID(
                        experiment.getAccession(),
                        profile.getId(),
                        analysisURI.toString(),
                        factor.toString(),
                        String.valueOf(expression.getLevel()));

                URI baselineValueUri = getUriProvider().getExpressionUri(experiment.getAccession(), frag);
                builder.createTypeInstance(
                        baselineValueUri,
                        getUriProvider().getBaselineExpressionValueType()
                );
                builder.createLabel(
                        baselineValueUri,
                        profile.getName() + " expressed in " + factor.getValue()
                );

                // get the factor
                URI experimentalFactorUri = getUriProvider().getFactorUri(experiment.getAccession(), factor.getType() , factor.getValue());
                builder.createObjectPropertyAssertion(
                        baselineValueUri,
                        getUriProvider().getExpressionToEfRelUri(),
                        experimentalFactorUri
                );

                builder.createObjectPropertyAssertion(
                        baselineValueUri,
                        getUriProvider().getExpressionValueToAnalysisRelUri(),
                        analysisURI
                );
                builder.createObjectPropertyAssertion(
                        analysisURI,
                        getUriProvider().getAnalysisToExpressionValueRelUri(),
                        baselineValueUri
                );

                URI geneidUri = getUriProvider().getBioentityUri(profile.getId());
                builder.createTypeInstance(
                        geneidUri,
                        getUriProvider().getBioentityTypeUri("EnsemblDatabaseReference")
                );
                builder.createLabel(
                        geneidUri,
                        profile.getName()
                );
                builder.createAnnotationAssertion(
                        geneidUri,
                        getUriProvider().getIdentifierRelUri(),
                        profile.getId()
                );


                builder.createObjectPropertyAssertion(
                        baselineValueUri,
                        getUriProvider().getDiffValueToProbeElementRel(),
                        geneidUri
                );

                buildExpressionForFactor(baselineValueUri, expression);
            }
        }
    }

    public void buildExpressionForFactor (URI baselineExpressionUri, BaselineExpression expression) {
        AssertionBuilder builder = getBuilder();

        builder.createDataPropertyAssertion(
                baselineExpressionUri,
                getUriProvider().getBaselineExpressionLevelRelation(),
                expression.getLevel()
        );
    }
}
