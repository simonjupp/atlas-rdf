#!/bin/sh

base=${0%/*}/..;
current=`pwd`;

mkdir ontology
curl https://raw.githubusercontent.com/simonjupp/atlas-rdf/master/atlas-sparqlapp/src/main/webapp/ontology/gxaterms.owl > ontology/gxaterms.owl
curl https://raw.githubusercontent.com/simonjupp/atlas-rdf/master/atlas-sparqlapp/src/main/webapp/ontology/gxamapping.owl > ontology/gxamapping.owl
curl http://semanticscience.org/ontology/sio.owl > ontology/sio.owl
curl http://edamontology.org/EDAM.owl > ontology/edam.owl