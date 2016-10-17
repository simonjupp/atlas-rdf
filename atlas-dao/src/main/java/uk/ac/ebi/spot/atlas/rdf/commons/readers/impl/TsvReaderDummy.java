package uk.ac.ebi.spot.atlas.rdf.commons.readers.impl;

import com.google.common.collect.ImmutableList;
import uk.ac.ebi.spot.atlas.rdf.commons.readers.TsvReader;

public class TsvReaderDummy implements TsvReader {

    @Override
    public String[] readLine(long lineIndex) {
        return null;
    }

    @Override
    public ImmutableList<String[]> readAll() {
        return ImmutableList.of();
    }
}
