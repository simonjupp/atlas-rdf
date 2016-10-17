package uk.ac.ebi.spot.atlas.rdf.profiles.baseline;

import uk.ac.ebi.spot.atlas.rdf.profiles.ProfileStreamOptions;
import uk.ac.ebi.spot.rdf.model.baseline.Factor;

import java.util.Set;

public interface BaselineProfileStreamOptions extends ProfileStreamOptions<Factor> {

    String getExperimentAccession();

    double getCutoff();

    String getQueryFactorType();

    Set<Factor> getSelectedFilterFactors();

    Double getThresholdForPremium();

    Double getFractionForPremium();

}
