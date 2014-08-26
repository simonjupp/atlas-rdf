package uk.ac.ebi.spot.rdf.builder;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.spot.atlas.rdf.Organism;
import uk.ac.ebi.spot.rdf.exception.UnknownOrganismTypeException;
import uk.ac.ebi.spot.rdf.utils.HashingIdGenerator;
import uk.ac.ebi.spot.rdf.utils.PropertyReader;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author Simon Jupp
 * @date 31/07/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class PropertiesBasedURIProvider implements URIProvider {

    private PropertyReader propertyReader;

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public String buildVersionNumber = "UNKNOWN";

    private static String HASH = "#";
    private static String DASH = "-";
    private static String SLASH = "/";

    private static String ASSAY = "assay";
    private static String SAMPLE = "sample";
    private static String CHAR = "characteristic";
    private static String EXPR = "expression";
    private static String EXPERIMENT = "experiment";

    public PropertyReader getPropertyReader() {
        return propertyReader;
    }

    public void setPropertyReader(PropertyReader propertyReader) {
        this.propertyReader = propertyReader;
    }

    public PropertiesBasedURIProvider(PropertyReader propertyReader) {
        this.propertyReader = propertyReader;
        try {
            if (getPropertyReader().getProp().contains("gxa.rdf.version")) {
                if (!getPropertyReader().getProp().get("gxa.rdf.version").equals("")) {
                    this.buildVersionNumber = getPropertyReader().getProp().get("gxa.rdf.version").toString();
                    log.info("setting gxa version number to " + buildVersionNumber);
                }
            }
        }
        catch (Exception e) {
            log.warn("Can't read version number from properties file");
        }
    }

    public String read (String property) {
        return  getPropertyReader().getProp().getProperty(property);
    }

    public URI getExperimentUri(String accession) {
        String path = MessageFormat.format(read("gxa.accesion.uri"), accession);
        return URI.create(path.toString());
    }

    public URI getAssayUri(String experimentAccession, String assayAccession) {
        String path = MessageFormat.format(
                read("gxa.assay.uri"),
                experimentAccession,
                HashingIdGenerator.generateHashEncodedID(assayAccession.toLowerCase()));
        return URI.create(path.toString());
    }

    public URI getSampleUri(String experimentAccession, String type, String value) {
        String path = MessageFormat.format(
                read("gxa.sample.uri"),
                experimentAccession,
                HashingIdGenerator.generateHashEncodedID(type.toLowerCase(), value.toLowerCase()));

        return URI.create(path.toString());
    }

    @Override
    public URI getExpressionUri(String experimentAccession, String id) {
        String path = null;
        try {
            path = MessageFormat.format(read("gxa.expression.uri"), experimentAccession, URLEncoder.encode(id, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return URI.create(path.toString());
    }

    @Override
    public URI getFactorUri(String experimentAccession, String type, String value) {
        String path = MessageFormat.format(
                read("gxa.factor.uri"),
                experimentAccession,
                HashingIdGenerator.generateHashEncodedID(type.toLowerCase(), value.toLowerCase()));
        return URI.create(path.toString());
    }

    public URI getExperimentType(String description) {
        return URI.create(read("rdf.terms") + description);
    }

    public URI getIdentifierRelUri() {
        return URI.create(read("gxa.identifier.relation"));
    }

    public URI getDescriptionRelUri() {
        return URI.create(read("gxa.description.relation"));
    }

    public URI getPubmedPredicateUri() {
        return URI.create(read("gxa.pmid.relation"));
    }

    public URI getProbeDesignElementType() {
        return URI.create(read("microarray.probe.type"));
    }

    public URI getPValueRelation() {
        return URI.create(read("gxa.pvalue.relation"));
    }

    public URI getTStatRelation() {
        return URI.create(read("gxa.tstat.relation"));
    }


    public URI getAssayType() {
        return URI.create(read("gxa.assay.type"));
    }


    public URI getOrganismUri(String organismName) throws UnknownOrganismTypeException {
        if (Organism.isValid(organismName)) {
            return URI.create(
                    read("obo.base") +
                            "NCBITaxon_" + Organism.getOrganimsByName(organismName).getId()
            ) ;
        }
        throw new UnknownOrganismTypeException("Unknown organism class:" + organismName);

    }


    public URI getEfoUriFromFragment(String semanticTag) {
        return getEFOIri(semanticTag);
    }

    public URI getAssayToSampleRel() {
        return URI.create(read("gxa.assay2sample.relation"));
    }


    public URI getPropertyTypeAnnotationProperty() {
        return URI.create(read("gxa.propertyType.relation"));
    }


    public URI getPropertyValueAnnotationProperty() {
        return URI.create(read("gxa.propertyValue.relation"));
    }

    public URI getDesignElementUri(String platform, String designElement) {
        String path = MessageFormat.format(read("gxa.probe.uri"), platform, designElement);
        return URI.create(path.toString());
    }


    public URI getDiffValueToProbeElementRel() {
        return URI.create(read("gxa.refersto.relation"));
    }


    public Collection<URI> getBioentityUri(String id, String speciesName) {

        Collection<URI> uris = new HashSet<URI>();

        String path = MessageFormat.format(read("ensembl.identifiers.uri"), "", id);
        String path2 = MessageFormat.format(read("ensembl.resource.uri"), id);
        if (Organism.isValid(speciesName)) {
            String kingdom = Organism.getOrganimsByName(speciesName).getKingdom();

            if (!kingdom.equals("eukaryote")) {
                path = MessageFormat.format(read("ensembl.resource.uri"), "." + kingdom, id);
            }
        }
        else if (id.startsWith("MIMAT"))  {
            path = MessageFormat.format(read("mirBase.mature.resource.uri"),  id);
        }
        else if (id.startsWith("MI"))  {
            path = MessageFormat.format(read("mirBase.resource.uri"),  id);
        }
        uris.add(URI.create(path));
        uris.add(URI.create(path2));

        return uris;
    }


    public URI getExperimentToAssayRel() {
        return URI.create(read("gxa.hasPart.relation"));
    }

    public URI getExperimentToAnalysisRel() {
        return URI.create(read("gxa.hasPart.relation"));
    }

    public URI getPubmedUri(String pubmedId) {
        String path = MessageFormat.format(read("pubmed.resource.uri"), pubmedId);
        return URI.create(path.toString());
    }

    @Override
    public URI getParticipantRelUri() {
        return URI.create(read("gxa.participant.relation"));    }

    public URI getPlatformUri(String platform) {
        String path = MessageFormat.format(read("gxa.platfrom.uri"), platform);
        return URI.create(path.toString());
    }


    public URI getPlatformTypeUri() {
        return URI.create(read("microarray.platform.type"));
    }


    public URI getPartOfPlatfrom() {
        return URI.create(read("gxa.partOf.relation"));
    }


    public URI getBioentityTypeUri(String type) {
        return URI.create(read("rdf.terms") + type);
    }


    public URI getOrganismTypeUri() {
        return URI.create(read("gxa.organism.type"));
    }


    public URI getTaxonRelUri() {
        return URI.create(read("ro.intaxon.relation"));
    }


    public URI getAboutPageRelUri() {
        return URI.create(read("foaf.page.relation"));
    }


    public URI getAtlasExperimentUrl(String acc) {
        String path = MessageFormat.format(read("atlas.experiment.url"), acc);
        return URI.create(path.toString());
    }


    public URI getReferenceAssayRelUri() {
        return URI.create(read("gxa.referenceAssay.relation"));
    }

    @Override
    public URI getTestAssayRelUri() {
        return URI.create(read("gxa.testAssay.relation"));
    }

    public URI getExpressionValueToAnalysisRelUri() {
        return URI.create(read("gxa.isOutputOf.relation"));
    }

    @Override
    public URI getAnalysisToExpressionValueRelUri() {
        return URI.create(read("gxa.hasOutput.relation"));
    }

    @Override
    public URI getMicrorrayAnalysisTypeUri() {
        return URI.create(read("gxa.arraydiffanalysis.type"));
    }

    @Override
    public URI getRNASeqDiffAnalysisTypeUri() {
        return URI.create(read("gxa.rnaseqdiffanalysis.type"));
    }

    @Override
    public URI getRNASeqBaselineAnalysisTypeUri() {
        return URI.create(read("gxa.baselinediffanalysis.type"));
    }

    public URI getFoldChangeRelation() {
        return URI.create(read("gxa.foldchange.relation"));
    }


    public URI getUnderExpressedType() {
        return URI.create(read("gxa.decreasedexpression.type"));
    }


    public URI getOverExpressedType() {
        return URI.create(read("gxa.increasedexpression.type"));
    }

    @Override
    public URI getExpressionToEfRelUri() {
        return URI.create(read("gxa.hasFactor.relation"));    }

    public URI getBaselineExpressionValueType() {
        return URI.create(read("gxa.baselineexpression.type"));
    }


    public URI getBaselineExpressionLevelRelation() {
        return URI.create(read("gxa.fpkm.relation"));
    }

    @Override
    public URI getAnalysisUri(String accesion, String id) {
        String path = MessageFormat.format(read("gxa.analysis.uri"), accesion, id);
        return URI.create(path.toString());    }

    public URI getAssayToEfRel() {
        return URI.create(read("gxa.hasFactor.relation"));
    }

    private URI getEFOIri(String s) {

        try {


            if (s.startsWith("CL")) {
                return URI.create(read("obo.base") + s.replace(":", "_"));
            }
            else if (s.startsWith("GO")) {
                return URI.create(read("obo.base") + s.replace(":", "_"));
            }
            else if (s.startsWith("UBERON")) {
                return URI.create(read("obo.base") + s.replace(":", "_"));
            }
            else if (s.startsWith("OBI")) {
                return URI.create(read("obo.base") + s.replace(":", "_"));
            }
            else if (s.startsWith("NCBI")) {
                return URI.create(read("obo.base") + s.replace(":", "_"));
            }
            else if (s.startsWith("PATO")) {
                return URI.create(read("obo.base") + s.replace(":", "_"));
            }
            else if (s.startsWith("UO")) {
                return URI.create(read("obo.base") + s.replace(":", "_"));
            }
            else if (s.startsWith("CHEBI")) {
                return URI.create(read("obo.base")+ s.replace(":", "_"));
            }
            else if (s.startsWith("PO")) {
                return URI.create(read("obo.base") + s.replace(":", "_"));
            }
            else if (s.startsWith("BTO")) {
                return URI.create(read("obo.base") + s.replace(":", "_"));
            }
            else if (s.contains("Orphanet_")) {
                return URI.create(read("obo.base") + s.replace(":", "_"));
            }
            else if (s.startsWith("http")) {
                s = s.replace("http_", "http:");
                return URI.create(s);
            }
            else if (!s.startsWith("EFO")) {
                log.error("no matching EFO URI for:" + s);
            }
            return URI.create(read("efo.base") + s.replace(":", "_"));
        }
        catch (IllegalArgumentException e) {
            log.error("Malformed URI found, using EFO_0000001");
            return URI.create(read("efo.base") + "EFO_0000001");
        }
    }

}
