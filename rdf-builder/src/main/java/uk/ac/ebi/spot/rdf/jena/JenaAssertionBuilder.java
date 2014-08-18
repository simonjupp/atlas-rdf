package uk.ac.ebi.spot.rdf.jena;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;
import uk.ac.ebi.spot.rdf.builder.AssertionBuilder;

import java.io.OutputStream;
import java.net.URI;
import java.util.Collection;
import java.util.Date;

/**
 * @author Simon Jupp
 * @date 28/07/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class JenaAssertionBuilder implements AssertionBuilder {

    private static String RESOURCE = "http://rdf.ebi.ac.uk/resource/";
    private static String TERMS = "http://rdf.ebi.ac.uk/terms/";

    Model model;
    public JenaAssertionBuilder () {

        this.model = ModelFactory.createDefaultModel();
        model.setNsPrefix("atlas", RESOURCE + "atlas");
        model.setNsPrefix("atlasterms", TERMS + "atlas");
        model.setNsPrefix("efo", "http://www.ebi.ac.uk/efo/");
        model.setNsPrefix("obo", "http://purl.obolibrary.org/obo/");
        model.setNsPrefix("dcterms", "http://purl.org/dc/terms/");
        model.setNsPrefix("cito", "http://purl.org/spar/cito/");
        model.setNsPrefix("foaf", "http://xmlns.com/foaf/0.1/");

    }

    public Resource createResourceFromUri (URI uri)  {
        return model.createResource(uri.toString());
    }

    public Property createPropertyFromUri (URI uri)  {
        return model.createProperty(uri.toString());
    }

    public Literal createLiteralFromString (String literal)  {
        return model.createLiteral(literal);
    }

    public void createTypeInstance(URI instanceUri, URI classUri) {
        model.add(
                createResourceFromUri(instanceUri),
                RDF.type,
                createResourceFromUri(classUri)
        );
    }

    public void createAnnotationAssertion(URI subjectUri, URI annotationProperty, String value) {
        model.add(
                createResourceFromUri(subjectUri),
                createPropertyFromUri(annotationProperty),
                createLiteralFromString(value))
        ;
    }

    public void createAnnotationAssertion(URI subjectUri, URI annotationProperty, URI object) {
        model.add(
                createResourceFromUri(subjectUri),
                createPropertyFromUri(annotationProperty),
                createResourceFromUri(object))
        ;
    }

    public void createAnnotationAssertion(URI subjectUri, URI annotationProperty, Date value) {
        model.add(
                createResourceFromUri(subjectUri),
                createPropertyFromUri(annotationProperty),
                model.createTypedLiteral(value.toString(), XSDDatatype.XSDdateTime))
        ;
    }

    public void createDataPropertyAssertion(URI subjectUri, URI dataProperty, double value) {
        model.add(
                createResourceFromUri(subjectUri),
                createPropertyFromUri(dataProperty),
                model.createTypedLiteral(value, XSDDatatype.XSDdouble))
        ;
    }

    public void createObjectPropertyAssertion(URI subjectUri, URI objectProperty, URI objectUri) {
        createAnnotationAssertion(subjectUri, objectProperty, objectUri);
    }

    public void createLabel(URI propertyUri, String label) {
        model.add(
                createResourceFromUri(propertyUri),
                RDFS.label,
                createLiteralFromString(label))
        ;
    }

    public void renderToStream(OutputStream stream, String format) {
        model.write(stream, format);
        discardModel();
    }

    public void discardModel() {
        model.close();

    }
}
