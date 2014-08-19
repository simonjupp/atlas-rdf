package uk.ac.ebi.spot.rdf.builder;


import uk.ac.ebi.spot.rdf.exception.UnknownOrganismTypeException;

import java.net.URI;
import java.util.Collection;

/**
 * @author Simon Jupp
 * @date 02/05/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 *
 * Interface for getting URIs for rescources in Atlas. These may be types, properties
 * or templates for constructing URIs from specified values
 *
 */
public interface URIProvider {


    /**
     * Get the URI for an individual experiment
     * @param accession the experiment accession that can optionally be used to create the URI
     * @return URI for the experiment
     *
     **/
    URI getExperimentUri(String accession);

    /**
     * Get the set of URI for typing an experiment
     * @param type This parameter is optional and mayb the type as defined by the databses that can be used t
     *             to comstruct the URI
     * @return URI experiment class URI
     **/
    URI getExperimentType(String type);

    /**
     * Get the URI for an identifier link
     * @return URI identifier URI
     **/
    URI getIdentifierRelUri();

    /**
     * Get the URI for a description link
     * @return URI descriptions URI
     **/
    URI getDescriptionRelUri();

    /**
     * Get the URI for publication link
     * @return URI publication URI
     **/
    URI getPubmedPredicateUri();

     /**
     * Get the URI for a submission date predicate
     * @return URI submission date URI
     **/
    // todo data not currently available in atlas (need to get from ArrayExpress)
//    URI getDateOfLastUpdateUri();

     /**
     * Get the set of URI for typing an design element / probe id
     * @return URI probe class URI
     **/
    URI getProbeDesignElementType();

    /**
     * Get the set of URI for p-value link
     * @return URI p value URI
     **/
    URI getPValueRelation();

    /**
     * Get the set of URI for a t statistic link
     * @return URI t statistic class URI
     **/
    URI getTStatRelation();

    /**
     * Get the set of URI for typing an experimental assay
     * @return URI assay class URI
     **/
    URI getAssayType();

    /**
     * Get the URI for an Organism instance
     * @param organismName name of the Organism e.g. homo sapiens
     * @return URI Organism, taxon or species URI
     **/
    URI getOrganismUri(String organismName) throws UnknownOrganismTypeException;

    /**
     * Given a short from ontology id, work out the full URI e.g. EFO:0000001 returns http://www.ebi.ac.uk/efo/EFO_0000001
     * @param shortform The ontology term id in shortform e.g. EFO:0000001
     * @return URI EFO URI for this URI fragment
     */
    URI getEfoUriFromFragment(String shortform);

    // property URIs

    /**
     * Set fo predicates for linking assays to samples
     * @return Assay to Sample URI predicate
     */
    URI getAssayToSampleRel();


    /**
     * URI for the annotation property for a atlas property type e.g. for the sample or factor
     * ORGANISM_PART / liver, the property type is ORGANISM_PART
     * @return URI for the property type relation
     */
    URI getPropertyTypeAnnotationProperty();

    /**
     * URI for the annotation property for a atlas property value e.g. for the sample or factor
     * ORGANISM_PART / liver, the property value is liver
     * @return    URI for the property value relation
     */
    URI getPropertyValueAnnotationProperty();

    /**
     * Generate a URI for a propbe design element
     * @param platform  the array platform
     * @param designElement  name
     * @return URI predicate URI
     */
    URI getDesignElementUri(String platform, String designElement);

    /**
     * relation URI between differential expression value and the probe design element
     * @return URI predicate URI
     */
    URI getDiffValueToProbeElementRel();

    /**
     * Get the URI for an Ensembl gene given a ensemble gene id
     * @param id id for the accession
     * @return URI class URI
     */
    URI getBioentityUri(String id);

    /**
     * relate an experiment to an assay
     * @return
     */
    URI getExperimentToAssayRel();

    /**
     * relate an experiment to an analysis
     * @return
     */
    URI getExperimentToAnalysisRel();

    /**
     * Get the URI for a pubmed resource
     * @param pubmedId
     * @return
     */
    URI getPubmedUri(String pubmedId);


    /**
     * Mint a URI for a platform
     * @param platform
     * @return
     */
    URI getPlatformUri(String platform);

    /**
     * Get URI for a platform type
     * @return
     */

    URI getPlatformTypeUri();

    /**
     * Get URI for a predicate to link a probe to a platform type
     * @return
     */
    URI getPartOfPlatfrom();

    /**
     * Get URI for the type of bioentity, given some typing information
     * @return
     */
    URI getBioentityTypeUri(String type);

    /**
     * Collection of URIs for typing an Organism class
     */
    URI getOrganismTypeUri();

    /**
     * Collection of URIs for relating some resource to an Organism identifier
     */
    URI getTaxonRelUri();

    /**
     * Get the predicate to link an RDF resource to another web page about this resource
     * For Atlas this is a link to the primary URL for each experiment
     */
    URI getAboutPageRelUri();

    /**
     * Get the full URL to the Atlas page for this experiment
     * For Atlas this is a link to the primary URL for each experiment
     */
    URI getAtlasExperimentUrl(String acc);

    /**
     * get the URI for the type of contrast based microarray differential analysis
     * @return
     */
    URI getMicrorrayAnalysisTypeUri();

    /**
     * get the URI for the type of contrast based RNA-seq differential analysis
     * @return
     */
    URI getRNASeqDiffAnalysisTypeUri();

    /**
     * get the URI for the type of contrast based RNA-seq differential analysis
     * @return
     */
    URI getRNASeqBaselineAnalysisTypeUri();


    /**
     * get the URI for the relationship between an analysis and reference assay
     * @return
     */
    URI getReferenceAssayRelUri();

    /**
     * get the URI for the relationship between an analysis and a test assay
     * @return
     */
    URI getTestAssayRelUri();

    /**
     * Link an expression value to the analysis from which it was calculated
     * @return
     */
    URI getExpressionValueToAnalysisRelUri();

    /**
     * Link an analysis to the ouput
     * @return
     */
    URI getAnalysisToExpressionValueRelUri();


    URI getFoldChangeRelation();

    URI getUnderExpressedType();

    URI getOverExpressedType();

    URI getBaselineExpressionValueType();


    URI getBaselineExpressionLevelRelation();


    URI getAssayToEfRel();

    /**
     * Get the URI for an individual assay
     * @param experimentAccession the experiment accession that can optionally be used to create the URI
     * @param assayAccession the assay accession that can optionally be used to create the URI
     * @return URI for the assay
     *
     **/
    URI getAssayUri(String experimentAccession, String assayAccession);

    /**
     * Get the URI for an individual sample
     * @param experimentAccession the experiment accession that can optionally be used to create the URI
     * @param type the sample type that can optionally be used to create the URI
     * @param value the sample value that can optionally be used to create the URI
     * @return URI for the sample
     *
     **/
    URI getSampleUri(String experimentAccession, String type, String value);

    URI getAnalysisUri(String accesion, String id);
    URI getExpressionUri(String accession, String id);
    URI getFactorUri (String accession, String type, String value);

    URI getParticipantRelUri();

    URI getExpressionToEfRelUri();


}
