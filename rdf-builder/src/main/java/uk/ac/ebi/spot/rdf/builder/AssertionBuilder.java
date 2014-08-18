package uk.ac.ebi.spot.rdf.builder;

import java.io.OutputStream;
import java.net.URI;
import java.util.Collection;
import java.util.Date;

/**
 * @author Simon Jupp
 * @date 02/05/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public interface AssertionBuilder {

    void createTypeInstance(URI instanceUri, URI classUri);

    void createAnnotationAssertion(URI subjectUri, URI annotationProperty, String value);
    void createAnnotationAssertion(URI subjectUri, URI annotationProperty, URI object);
    void createAnnotationAssertion(URI subjectUri, URI annotationProperty, Date value);
    void createDataPropertyAssertion(URI subjectUri, URI dataProperty, double value);

    /**
     * Create a triple along a named predicate
     * @param subjectUri
     * @param objectProperty
     * @param objectUri
     */
    void createObjectPropertyAssertion(URI subjectUri, URI objectProperty, URI objectUri);

    void createLabel(URI propertyUri, String label);

    void renderToStream(OutputStream stream, String format);

}