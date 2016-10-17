package uk.ac.ebi.spot.atlas.rdf.profiles;

import uk.ac.ebi.spot.rdf.model.Expression;

public interface ExpressionsRowDeserializerBuilder<V, T extends Expression> {

    ExpressionsRowDeserializerBuilder<V, T> forExperiment(String experimentAccession);

    ExpressionsRowDeserializerBuilder<V, T> withHeaders(String... tsvFileHeaders);

    ExpressionsRowDeserializer<V, T> build();

}
