package uk.ac.ebi.spot.rdf.builder;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.spot.rdf.exception.UnknownOrganismTypeException;
import uk.ac.ebi.spot.rdf.model.Experiment;
import uk.ac.ebi.spot.rdf.model.ExperimentDesign;
import uk.ac.ebi.spot.rdf.model.SampleValue;
import uk.ac.ebi.spot.rdf.model.baseline.Factor;
import uk.ac.ebi.spot.rdf.model.GeneProfilesList;
import uk.ac.ebi.spot.rdf.model.baseline.FactorSet;

import java.io.OutputStream;
import java.net.URI;

/**
 * @author Simon Jupp
 * @date 02/05/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public abstract class AbstractExperimentBuilder <T extends Experiment, V extends GeneProfilesList> implements ExperimentBuilder<T,V>  {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected AbstractExperimentBuilder(URIProvider uriProvider, AssertionBuilder builder) {
        this.uriProvider = uriProvider;
        this.builder = builder;
    }

    private URIProvider uriProvider;
    private AssertionBuilder builder;

    public URIProvider getUriProvider() {
        return uriProvider;
    }

    public AssertionBuilder getBuilder() {
        return builder;
    }

    public void flushModel(OutputStream stream, String format) {
        getBuilder().renderToStream(stream, format);
    }

    public void buildExperimentDesign(T experiment) {
        AssertionBuilder builder = getBuilder();
        String accession = experiment.getAccession();
        URI experimentUri = getUriProvider().getExperimentUri(accession);

        ExperimentDesign experimentDesign = experiment.getExperimentDesign();

        for (String runOrAssay : experimentDesign.getAllRunOrAssay()) {

            URI runUri = getUriProvider().getAssayUri(accession, runOrAssay);
            builder.createObjectPropertyAssertion(experimentUri, getUriProvider().getExperimentToAssayRel(), runUri);
            // type it
            builder.createTypeInstance(runUri, getUriProvider().getAssayType());
            builder.createLabel(runUri, "(Assay) " + runOrAssay);
            builder.createAnnotationAssertion(runUri, getUriProvider().getIdentifierRelUri(), runOrAssay);

            // assert array design if applicable
            String arrayDesign = experimentDesign.getArrayDesign(runOrAssay);
            if (arrayDesign != null) {
                builder.createAnnotationAssertion(runUri, getUriProvider().getParticipantRelUri(), getUriProvider().getPlatformUri(arrayDesign));
            }

            // assert factor values
            FactorSet factorSet = experimentDesign.getFactors(runOrAssay);
            if (factorSet != null) {
                for (Factor factor: factorSet) {
                    URI experimentalFactorUri = getUriProvider().getFactorUri(experiment.getAccession(), factor.getType(),  factor.getValue());
                    builder.createAnnotationAssertion(experimentalFactorUri, getUriProvider().getPropertyTypeAnnotationProperty(), factor.getType());
                    builder.createAnnotationAssertion(experimentalFactorUri, getUriProvider().getPropertyValueAnnotationProperty(), factor.getValue());
                    builder.createLabel(experimentalFactorUri, "(Factor value) " + factor.getType() + "/" + factor.getValue());

                    // todo only one ontology term possible?
                    if (StringUtils.isNotBlank(factor.getValueOntologyTerm())) {
                        builder.createTypeInstance(
                                experimentalFactorUri,
                                getUriProvider().getEfoUriFromFragment(factor.getValueOntologyTerm())
                        );
                    }
                    else {
                        builder.createTypeInstance(
                                experimentalFactorUri,
                                URI.create("http://www.ebi.ac.uk/efo/EFO_0000001")
                        );
                    }

                    builder.createObjectPropertyAssertion(
                            runUri,
                            getUriProvider().getAssayToEfRel(),
                            experimentalFactorUri
                    );
                }
            }

            // sample info
            for (SampleValue sample : experimentDesign.getSamples(runOrAssay)) {

                URI sampleUri = getUriProvider().getSampleUri(accession, sample.getType(),  sample.getValue());
                builder.createAnnotationAssertion(sampleUri, getUriProvider().getPropertyTypeAnnotationProperty(), sample.getType());
                builder.createAnnotationAssertion(sampleUri, getUriProvider().getPropertyValueAnnotationProperty(), sample.getValue());
                builder.createLabel(sampleUri, "(Sample) " + sample.getType() + "/" + sample.getValue());
                if (StringUtils.isNotBlank(sample.getOntologyTerm())) {
                    builder.createTypeInstance(
                            sampleUri,
                            getUriProvider().getEfoUriFromFragment(sample.getOntologyTerm())
                    );
                }
                else {
                    builder.createTypeInstance(
                            sampleUri,
                            URI.create("http://www.ebi.ac.uk/efo/EFO_0000001")
                    );
                }
                builder.createObjectPropertyAssertion(
                        runUri,
                        getUriProvider().getAssayToSampleRel(),
                        sampleUri
                );
            }
        }
    }

    public void buildExperimentDescription (Experiment experiment) {

        String accession = experiment.getAccession();

        URI experimentUri = getUriProvider().getExperimentUri(accession);
        builder.createTypeInstance(
                experimentUri,
                getUriProvider().getExperimentType(experiment.getType().getDescription()));

        builder.createAnnotationAssertion(
                experimentUri,
                getUriProvider().getIdentifierRelUri(),
                accession);

        builder.createAnnotationAssertion(
                experimentUri,
                getUriProvider().getAboutPageRelUri(),
                getUriProvider().getAtlasExperimentUrl(accession));

        builder.createAnnotationAssertion(
                experimentUri, getUriProvider().getDescriptionRelUri(),
                experiment.getDescription());

        for (String pubmedId : experiment.getPubMedIds()) {
            builder.createAnnotationAssertion(
                    experimentUri,
                    getUriProvider().getPubmedPredicateUri(),
                    getUriProvider().getPubmedUri(pubmedId));
        }

//        builder.createAnnotationAssertion(
//                experimentUri,
//                getUriProvider().getDateOfLastUpdateUri(),
//                experiment.getLastUpdate());

        for (String speciesName : experiment.getSpecies()) {
            URI speciesUri = null;
            try {
                speciesUri = getUriProvider().getOrganismUri(speciesName);
                builder.createObjectPropertyAssertion(
                        experimentUri,
                        getUriProvider().getTaxonRelUri(),
                        speciesUri);
                builder.createTypeInstance(
                        speciesUri,
                        getUriProvider().getOrganismTypeUri()
                );
                builder.createLabel(speciesUri, speciesName);
            } catch (UnknownOrganismTypeException e) {
                log.error("Unknown organism in " + experiment.getAccession() + " " + speciesName);
            }

        }
    }



}
