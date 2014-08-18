package uk.ac.ebi.spot.rdf.model.baseline;

/*
 * Copyright 2008-2013 Microarray Informatics Team, EMBL-European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * For further details of the Gene Expression Atlas project, including source code,
 * downloads and documentation, please see:
 *
 * http://gxa.github.com/gxa
 */
public class AssayGroupFactor implements Comparable<AssayGroupFactor>{

    private String assayGroupId;
    private Factor factor;

    public AssayGroupFactor(String assayGroupId, Factor factor) {
        this.assayGroupId = assayGroupId;
        this.factor = factor;
    }

    //used in heatmap.tag
    public String getAssayGroupId() {
        return assayGroupId;
    }

    public Factor getFactor() {
        return factor;
    }

    public String getValue() {
        return factor.getValue();
    }

    public String getType() {
        return factor.getType();
    }

    //used in heatmap.tag
    public String getValueOntologyTerm() {
        return factor.getValueOntologyTerm();
    }

    @Override
    public int compareTo(AssayGroupFactor assayGroupFactor) {
        return factor.compareTo(assayGroupFactor.getFactor());
    }
}
