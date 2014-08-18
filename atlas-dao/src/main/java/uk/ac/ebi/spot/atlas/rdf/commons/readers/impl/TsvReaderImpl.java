package uk.ac.ebi.spot.atlas.rdf.commons.readers.impl;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.base.Predicate;
import org.apache.log4j.Logger;
import uk.ac.ebi.spot.atlas.rdf.commons.readers.TsvReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class TsvReaderImpl implements TsvReader {

    private static final Logger LOGGER = Logger.getLogger(TsvReaderImpl.class);

    private InputStreamReader tsvFileInputStreamReader;

    public TsvReaderImpl(InputStreamReader tsvFileInputStreamReader) {
        this.tsvFileInputStreamReader = tsvFileInputStreamReader;
    }

    @Override
    public String[] readLine(long lineIndex) {

        try (CSVReader csvReader = new CSVReader(tsvFileInputStreamReader, '\t')){
            String[] line = null;
            for (int i = 0; i <= lineIndex; i++) {
                line = csvReader.readNext();
            }
            return line;

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new IllegalStateException("Cannot read Tsv file", e);
        }
    }

    @Override
    public List<String[]> readAll() {
        return readAndFilter(new IsNotCommentPredicate());
    }

    List<String[]> readAndFilter(Predicate<String> acceptanceCriteria) {

        try (CSVReader csvReader = new CSVReader(tsvFileInputStreamReader, '\t')){
            List<String[]> rows = new ArrayList<>();
            for (String[] row : csvReader.readAll()) {
                if (acceptanceCriteria.apply(row[0])) {
                    rows.add(row);
                }
            }
            return rows;

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new IllegalStateException("Cannot read Tsv file", e);
        }
    }

    protected static class IsCommentPredicate implements Predicate<String> {

        @Override
        public boolean apply(String rowHeader) {
            return rowHeader.trim().startsWith("#");
        }
    }

    protected static class IsNotCommentPredicate extends IsCommentPredicate {

        @Override
        public boolean apply(String rowHeader) {
            return !super.apply(rowHeader);
        }
    }


}