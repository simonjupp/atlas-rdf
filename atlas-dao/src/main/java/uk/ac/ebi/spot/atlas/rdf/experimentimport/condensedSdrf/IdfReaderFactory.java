package uk.ac.ebi.spot.atlas.rdf.experimentimport.condensedSdrf;

import org.springframework.beans.factory.annotation.Value;
import uk.ac.ebi.spot.atlas.rdf.commons.readers.FileTsvReaderBuilder;
import uk.ac.ebi.spot.atlas.rdf.commons.readers.TsvReader;
import uk.ac.ebi.spot.atlas.rdf.commons.readers.UrlTsvReaderBuilder;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

@Named
public class IdfReaderFactory {

    private UrlTsvReaderBuilder urlTsvReaderBuilder;
    private FileTsvReaderBuilder fileTsvReaderBuilder;

    @Inject
    public IdfReaderFactory(@Value("#{configuration['experiment.magetab.idf.url.template']}") String idfUrlTemplate,
                            @Value("#{configuration['experiment.magetab.idf.path.template']}") String idfPathTemplate,
                            UrlTsvReaderBuilder urlTsvReaderBuilder, FileTsvReaderBuilder fileTsvReaderBuilder) {
        this.urlTsvReaderBuilder = urlTsvReaderBuilder.forTsvFileUrlTemplate(idfUrlTemplate);
        this.fileTsvReaderBuilder = fileTsvReaderBuilder.forTsvFilePathTemplate(idfPathTemplate);
    }

    public TsvReader create(String experimentAccession) {
        try {
            return urlTsvReaderBuilder.withExperimentAccession(experimentAccession).build();
        } catch (IOException e) {
            return fileTsvReaderBuilder.withExperimentAccession(experimentAccession).build();
        }
    }

}