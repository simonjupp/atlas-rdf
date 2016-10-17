package uk.ac.ebi.spot.atlas.rdf.profiles;

import java.util.Set;

public interface ProfileStreamOptions<T> {

    Integer getHeatmapMatrixSize();

    boolean isSpecific();

    Set<T> getSelectedQueryFactors();

    Set<T> getAllQueryFactors();

}
