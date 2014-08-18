

/*
 * Copyright (c) 2013 EMBL - European Bioinformatics Institute
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

var exampleQueries = [

    {
        shortname : "Query 1",
        description: "Get experiments where the sample description contains diabetes",
        query: "SELECT DISTINCT ?experiment ?description WHERE\n{" +
            "?experiment\n" +
            "a atlasterms:Experiment ;\n" +
            "dcterms:description ?description ;\n" +
            " atlasterms:hasAssay\n" +
            " [atlasterms:hasSample\n" +
            "  [atlasterms:hasSampleCharacteristic\n" +
            "   [ atlasterms:propertyType ?propertyType ;\n" +
            "     atlasterms:propertyValue ?propertyValue]\n" +
            "  ]\n" +
            " ]\n" +
            "filter regex (?description, \"diabetes\", \"i\")\n" +
            "}"
    },
    {
        shortname : "Query 2",
        description: "Get differentially expressed genes where factor is asthma",
        query: "SELECT distinct ?expressionValue ?dbXref ?pvalue ?propertyValue       \n" +
            "WHERE {            \n" +
            "?expUri atlasterms:hasAnalysis ?analysis .     \n" +
            "?analysis atlasterms:hasExpressionValue ?value .        \n" +
            "?value a atlasterms:IncreasedDifferentialExpressionRatio .      \n" +
            "?value rdfs:label ?expressionValue .      \n" +
            "?value atlasterms:pValue ?pvalue .       \n" +
            "?value atlasterms:hasFactorValue ?factor .       \n" +
            "?value atlasterms:isMeasurementOf ?probe .      \n" +
            "?probe atlasterms:dbXref ?dbXref .        \n" +
            "?factor atlasterms:propertyType ?propertyType .        \n" +
            "?factor atlasterms:propertyValue ?propertyValue .      \n" +
            "?factor rdf:type efo:EFO_0000270 .       \n" +
            "}          \n"
    },
    {
        shortname : "Query 3",
        description: "Show expression for ENSG00000129991 (TNNI3)",
        query:   "PREFIX identifiers:<http://identifiers.org/ensembl/>\n\n" +

            "SELECT distinct ?diffValue ?expUri ?propertyType ?propertyValue ?pvalue   \n" +
            "WHERE {             \n" +
            "?expUri atlasterms:hasAnalysis ?analysis .       \n" +
            "?analysis atlasterms:hasExpressionValue ?value .   \n" +
            "?value rdfs:label ?diffValue .       \n" +
            "?value atlasterms:hasFactorValue ?factor .   \n" +
            "?factor atlasterms:propertyType ?propertyType .    \n" +
            "?factor atlasterms:propertyValue ?propertyValue .  \n" +
            "?value atlasterms:pValue ?pvalue .      \n" +
            "?value atlasterms:isMeasurementOf ?probe .    \n" +
            "?probe atlasterms:dbXref identifiers:ENSG00000129991 .  \n" +
            "}           \n" +
            "ORDER BY ASC (?pvalue)"

    },
    {
        shortname : "Query 4",
        description: "Show expression for ENSG00000129991 (TNNI3) with its GO annotations from Uniprot (Federated query to http://beta.sparql.uniprot.org/sparql)",
        query: "PREFIX upc:<http://purl.uniprot.org/core/>\n" +
            "PREFIX identifiers:<http://identifiers.org/ensembl/>\n\n" +
            "SELECT distinct ?valueLabel ?goid ?golabel \n" +
            "WHERE { \n" +

            "?value rdfs:label ?valueLabel .\n" +
            "?value atlasterms:isMeasurementOf ?probe  .\n" +
            "?probe atlasterms:dbXref identifiers:ENSG00000129991  .\n" +
            "?probe atlasterms:dbXref ?uniprot .\n" +

            "SERVICE <http://beta.sparql.uniprot.org/sparql> {   \n" +
            "?uniprot a upc:Protein  .\n" +
            "?uniprot upc:classifiedWith ?keyword  .\n" +
            "?keyword rdfs:seeAlso ?goid  .\n" +
            "?goid rdfs:label ?golabel  .\n" +
            "}   \n" +
            "}"
    },
    {
        shortname : "Query 5",
        description: "For the genes differentially expressed in asthma, get the gene products associated to a Reactome pathway",
        query: "PREFIX biopax3:<http://www.biopax.org/release/biopax-level3.owl#>\n\n" +
            "SELECT distinct ?pathwayname ?expressionValue ?pvalue\n" +
            "WHERE {\n" +
            "?protein rdf:type biopax3:Protein .\n" +
            "?protein biopax3:memberPhysicalEntity \n" +
            "  [biopax3:entityReference ?dbXref] .\n" +
            "?pathway rdf:type biopax3:Pathway .\n" +
            "?pathway biopax3:displayName ?pathwayname .\n" +
            "?pathway biopax3:pathwayComponent ?reaction .\n" +
            "?reaction rdf:type biopax3:BiochemicalReaction .\n" +
            " {\n" +
            "  {?reaction ?rel ?protein .}\n" +
            "  UNION \n" +
            "    { \n" +
            "     ?reaction  ?rel  ?complex .\n" +
            "     ?complex rdf:type biopax3:Complex .\n" +
            "     ?complex ?comp ?protein .\n" +
            "    } \n" +
            " } \n" +
            "?factor rdf:type efo:EFO_0000270 . \n" +
            "?value atlasterms:hasFactorValue ?factor . \n" +
            "?value atlasterms:isMeasurementOf ?probe . \n" +
            "?value atlasterms:pValue ?pvalue . \n" +
            "?value rdfs:label ?expressionValue . \n" +
            "?probe atlasterms:dbXref ?dbXref .\n" +
            "}\n" +
            "ORDER BY ASC (?pvalue)\n"
    },

    {
        shortname : "Query 6",
        description: "Get all mappings for a given probe e.g. A-AFFY-1/661_at",
        query: "SELECT distinct *\n" +
            "WHERE {\n" +
            "?probe atlasterms:dbXref ?dbXref .\n" +
            "?dbXref a ?dbXrefType .\n" +
            "}\n"
    }


]