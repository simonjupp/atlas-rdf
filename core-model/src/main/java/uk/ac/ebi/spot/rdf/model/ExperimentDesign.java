package uk.ac.ebi.spot.rdf.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import uk.ac.ebi.spot.rdf.model.baseline.Factor;
import uk.ac.ebi.spot.rdf.model.baseline.FactorSet;

import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

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
public class ExperimentDesign implements Serializable {

    private SortedSet<String> sampleHeaders = Sets.newTreeSet();

    private SortedSet<String> factorHeaders = Sets.newTreeSet();

    private Map<String, Collection<SampleValue>> samples = Maps.newHashMap();

    private Map<String, FactorSet> factorSetMap = Maps.newHashMap();

    private Map<String, String> arrayDesigns = Maps.newHashMap();

    private List<String> assayHeaders = Lists.newArrayList();

    public void putSample(String runOrAssay, String sampleHeader, String sampleValue) {
        putSample(runOrAssay, sampleHeader, sampleValue, null);
    }
    public void putSample(String runOrAssay, String sampleHeader, String sampleValue, String ontologyTerm) {
        if (!samples.containsKey(runOrAssay)) {
            samples.put(runOrAssay, new HashSet<SampleValue>());
        }
        samples.get(runOrAssay).add(new SampleValue(sampleHeader, sampleValue, ontologyTerm));
        sampleHeaders.add(sampleHeader);
    }
    public void putFactor(String runOrAssay, String factorHeader, String factorValue) {
        putFactor(runOrAssay, factorHeader, factorValue, null);
    }

    public void putFactor(String runOrAssay, String factorHeader, String factorValue, String factorOntologyTerm) {
        Factor factor = new Factor(factorHeader, factorValue, factorOntologyTerm);
        if(!factorSetMap.containsKey(runOrAssay)){
            factorSetMap.put(runOrAssay, new FactorSet());
        }
        factorSetMap.get(runOrAssay).add(factor);
        factorHeaders.add(factorHeader);
    }

    public void putArrayDesign(String runOrAssay, String arrayDesign) {
        arrayDesigns.put(runOrAssay, arrayDesign);
    }

    public String getArrayDesign(String runOrAssay) {
        return arrayDesigns.get(runOrAssay);
    }

    public void addAssayHeader(String assayHeader) {
        assayHeaders.add(assayHeader);
    }

    public List<String> getAssayHeaders() {
        return assayHeaders;
    }

    public SortedSet<String> getSampleHeaders() {
        return Collections.unmodifiableSortedSet(sampleHeaders);
    }

    //NB: factor headers are not normalized (see Factor.normalize), unlike factor type !
    public SortedSet<String> getFactorHeaders() {
        return Collections.unmodifiableSortedSet(factorHeaders);
    }


    public String getSampleValue(String runOrAssay, String sampleHeader) {
        Collection<SampleValue> sampleValues = samples.get(runOrAssay);
        if (!sampleValues.isEmpty()) {
            for (SampleValue values : sampleValues) {
                if (values.getType().equals(sampleHeader)) {
                    return values.getValue();
                }
            }
        }
        return null;
    }

    public String getFactorValue(String runOrAssay, String factorHeader) {
        FactorSet factorSet = factorSetMap.get(runOrAssay);
        if (factorSet != null) {

            Factor factor = factorSet.getFactorByType(Factor.normalize(factorHeader));
            return factor == null ? null : factor.getValue();
        }
        return null;
    }

    /**
     *
     * @param runOrAssay run or assay id
     * @return  map of {factorHeader, factorValue}
     */
    public Map<String, String> getFactorValues(String runOrAssay) {
        Map<String, String> valueByHeader = Maps.newHashMap();
        FactorSet factorSet = factorSetMap.get(runOrAssay);

        if (factorSet == null){
            return null;
        }
        for (Factor factor : factorSet){
            valueByHeader.put(factor.getHeader(), factor.getValue());
        }

        return valueByHeader;
    }

    public FactorSet getFactors(String runOrAssay){
        if(factorSetMap.containsKey(runOrAssay)){
            return factorSetMap.get(runOrAssay);
        }
        return null;
    }

    private String getFactorValueOntologyTerm(String runOrAssay, String factorHeader){
        FactorSet factorSet = factorSetMap.get(runOrAssay);
        if(factorSet != null){
            Factor factor = factorSet.getFactorByType(Factor.normalize(factorHeader));
            return factor == null ? null : factor.getValueOntologyTerm();
        }
        return null;
    }

    public Collection<SampleValue> getSamples(String runOrAssay) {
        return samples.get(runOrAssay);
    }

    public SortedSet<String> getAllRunOrAssay() {
        return Sets.newTreeSet(samples.keySet());
    }

    public List<String[]> asTableData() {
        List<String[]> tableData = Lists.newArrayList();
        for (String runOrAssay : getAllRunOrAssay()) {
            tableData.add(composeTableRow(runOrAssay));
        }
        return tableData;
    }

    protected String[] composeTableRow(String runOrAssay) {
        List<String> row = Lists.newArrayList(runOrAssay);

        String arrayDesign = getArrayDesign(runOrAssay);
        if (arrayDesign != null) {
            row.add(arrayDesign);
        }

        for (String sampleHeader : getSampleHeaders()) {
            row.add(getSampleValue(runOrAssay, sampleHeader));
        }

        for (String factorHeader : getFactorHeaders()) {
            row.add(getFactorValue(runOrAssay, factorHeader));
        }

        return row.toArray(new String[row.size()]);
    }

    public List<String[]> asTableOntologyTermsData() {
        List<String[]> tableData = Lists.newArrayList();
        for (String runOrAssay : getAllRunOrAssay()) {
            tableData.add(composeTableRowWithOntologyTerms(runOrAssay));
        }
        return tableData;
    }

    protected String[] composeTableRowWithOntologyTerms(String runOrAssay) {
        List<String> row = Lists.newArrayList(runOrAssay);

        String arrayDesign = getArrayDesign(runOrAssay);
        if (arrayDesign != null) {
            row.add(arrayDesign);
        }

        for (String sampleHeader : getSampleHeaders()) {
            row.add(getSampleValue(runOrAssay, sampleHeader));
        }

        for (String factorHeader : getFactorHeaders()) {
            row.add(getFactorValue(runOrAssay, factorHeader));
        }

        for (String factorHeader: getFactorHeaders()) {
            row.add(getFactorValueOntologyTerm(runOrAssay, factorHeader));
        }

        return row.toArray(new String[row.size()]);
    }

    public Set<String> getSpeciesForAssays(Set<String> assayAccessions) {
        Set<String> species = Sets.newHashSet();
        for (String assayAccession: assayAccessions){
            Collection<SampleValue> assaySamples = getSamples(assayAccession);

            checkNotNull(assaySamples, String.format("Assay accession %s does not exist or has no samples", assayAccession));

            for (SampleValue value : assaySamples) {
                if ("organism".equalsIgnoreCase(value.getType())){
                    species.add(value.getValue());
                }
            }
        }
        return species;
    }

    // header, value


}
