package uk.ac.ebi.spot.atlas.rdf.commons.readers;

import uk.ac.ebi.spot.atlas.rdf.commons.readers.impl.TsvReaderImpl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class TsvReaderBuilder {

    private String experimentAccession;

    private String tsvFilePathTemplate;

    public TsvReaderBuilder() {
    }

    public TsvReaderBuilder withExperimentAccession(String experimentAccession){
        this.experimentAccession = experimentAccession;
        return this;
    }

    public TsvReaderBuilder forTsvFilePathTemplate(String tsvFilePathTemplate) {
        this.tsvFilePathTemplate = tsvFilePathTemplate;
        return this;
    }

    public TsvReader build() {
        String tsvFilePath = MessageFormat.format(tsvFilePathTemplate, experimentAccession);
        Path tsvFileSystemPath = FileSystems.getDefault().getPath(tsvFilePath);
        try {
            InputStreamReader tsvFileInputStreamReader = new InputStreamReader(Files.newInputStream(tsvFileSystemPath));
            return new TsvReaderImpl(tsvFileInputStreamReader);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read Tsv file from path " + tsvFileSystemPath.toString(), e);
        }
    }


}
