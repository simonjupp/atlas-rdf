package uk.ac.ebi.spot.atlas.rdf.commons.readers;

import java.util.List;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public interface TsvReader {

    String[] readLine(long lineIndex);

    List<String[]> readAll();

}