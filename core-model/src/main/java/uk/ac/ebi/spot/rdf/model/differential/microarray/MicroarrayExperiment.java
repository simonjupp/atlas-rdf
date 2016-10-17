package uk.ac.ebi.spot.rdf.model.differential.microarray;

import uk.ac.ebi.spot.rdf.model.ExperimentDesign;
import uk.ac.ebi.spot.rdf.model.ExperimentType;
import uk.ac.ebi.spot.rdf.model.Species;
import uk.ac.ebi.spot.rdf.model.differential.Contrast;
import uk.ac.ebi.spot.rdf.model.differential.DifferentialExperiment;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

public class MicroarrayExperiment extends DifferentialExperiment {

    private SortedSet<String> arrayDesignAccessions;
    private SortedSet<String> arrayDesignNames;

    public MicroarrayExperiment(ExperimentType type, String accession, Date lastUpdate, Set<Contrast> contrasts,
                                String description, boolean hasRData,
                                Species species, SortedSet<String>
                                        arrayDesignAccessions,
                                SortedSet<String> arrayDesignNames, ExperimentDesign experimentDesign, Set<String> pubMedIds) {

        super(type, accession, lastUpdate, contrasts, description, hasRData, species,pubMedIds, experimentDesign);
        this.arrayDesignAccessions = arrayDesignAccessions;
        this.arrayDesignNames = arrayDesignNames;
    }

    public SortedSet<String> getArrayDesignAccessions() {
        return arrayDesignAccessions;
    }

    public SortedSet<String> getArrayDesignNames() {return arrayDesignNames;}


    @Override
    public Map<String, ?> getAttributes(){
        Map<String, Object> result = new HashMap<>();
        result.putAll(super.getAttributes());
        //For showing the QC REPORTS button in the header
        result.put("qcArrayDesigns", getArrayDesignAccessions());
        result.put("allArrayDesigns",getArrayDesignNames());
        return result;

    }
}
