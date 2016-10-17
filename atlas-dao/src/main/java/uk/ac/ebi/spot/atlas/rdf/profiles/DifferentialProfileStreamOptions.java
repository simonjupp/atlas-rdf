package uk.ac.ebi.spot.atlas.rdf.profiles;

import uk.ac.ebi.spot.rdf.model.differential.Contrast;
import uk.ac.ebi.spot.rdf.model.differential.Regulation;

public interface DifferentialProfileStreamOptions extends ProfileStreamOptions<Contrast> {
    Regulation getRegulation();
    String getExperimentAccession();
    double getPValueCutOff();
    double getFoldChangeCutOff();
}
