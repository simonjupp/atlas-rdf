package uk.ac.ebi.spot.rdf.model;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

public class AssayGroups implements Iterable<AssayGroup> {

    private Map<String, AssayGroup> assayGroupsById;

    public AssayGroups(Collection<AssayGroup> assayGroups) {
        this.assayGroupsById = Maps.newHashMap();
        for (AssayGroup assayGroup : assayGroups) {
            assayGroupsById.put(assayGroup.getId(), assayGroup);
        }
    }

    public Iterator<AssayGroup> iterator() {
        return assayGroupsById.values().iterator();
    }

    public Set<String> getAssayAccessions() {
        Set<String> assayAccessions = Sets.newHashSet();

        for (AssayGroup assayGroup : assayGroupsById.values()) {
            CollectionUtils.addAll(assayAccessions, assayGroup.iterator());
        }

        return assayAccessions;
    }

    public Set<String> getAssayGroupIds() {
        return assayGroupsById.keySet();
    }

    public AssayGroup getAssayGroup(String assayGroupId) {
        return assayGroupsById.get(assayGroupId);
    }
}
