package uk.ac.ebi.spot.atlas.rdf.commons.readers;

import java.util.List;

public interface TsvReader {
    String[] readLine(long lineIndex);
    List<String[]> readAll();
}