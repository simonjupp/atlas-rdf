package uk.ac.ebi.spot.atlas.rdf;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public enum Organism {

    SACCHAROMYCES_CEREVISIAE ("4932"),
    SUS_SCROFA        ("9823"),
    DASYPUS_NOVEMCINCTUS     ("9361"),
    MUS_MUSCULUS               ("10090"),
    ASPERGILLUS_FUMIGATUS_AF293 ("330879"),
    DROSOPHILA_MELANOGASTER    ("7227"),
    ANOPHELES_GAMBIAE         ("7165"),
    BOS_TAURUS              ("9913"),
    XENOPUS_TROPICALIS      ("8364"),
    EQUUS_CABALLUS         ("9796"),
    POPULUS_TRICHOCARPA     ("3694"),
    ARABIDOPSIS_THALIANA    ("3702"),
    ANOLIS_CAROLINENSIS  ("28377"),
    TETRAODON_NIGROVIRIDIS  ("99883"),
    DANIO_RERIO             ("7955"),
    CIONA_INTESTINALIS      ("7719"),
    GALLUS_GALLUS          ("9031"),
    HOMO_SAPIENS           ("9606"),
    XENOPUS_SILURANA_TROPICALIS ("8364"),
    MACACA_MULATTA         ("9544"),
    CANIS_FAMILIARIS      ("9615"),
    CIONA_SAVIGNYI       ("51511"),
    OVIS_ARIES             ("9940"),
    RATTUS_NORVEGICUS     ("10116"),
    CAENORHABDITIS_ELEGANS   ("6239"),
    ORYZA_SATIVA              ("4530"),
    ORYZA_SATIVA_JAPONICA  ("39947"),
    ORYZA_SATIVA_JAPONICA_GROUP  ("39947"),
    SCHIZOSACCHAROMYCES_POMBE    ("4896"),
    MONODELPHIS_DOMESTICA   ("13616"),
    BRASSICA_OLERACEA ("3712"),
    VITIS_VINIFERA       ("29760");

    String id;
    Organism(String id) {
        this.id = id;
    }

    public String getId () {
        return id;
    }

    public static boolean isValid(String name) {
        for(Organism v : Organism.values()) {
            if (v.name().equals(name.toUpperCase().replace(" ", "_"))) {
                return true;
            }
        }
        return false;
    }


    public static Organism getOrganimsByName(String speciesName) {
        for(Organism v : Organism.values()) {
            if (v.name().equals(speciesName.toUpperCase().replace(" ", "_"))) {
                return v;
            }
        }
        return null;
    }

    public static Organism getOrganimsById(String organsimId) {
        for (Organism o : Organism.values()) {
            if (organsimId.equals(o.getId())) {
                return o;
            }
        }
        return null;
    }
}
