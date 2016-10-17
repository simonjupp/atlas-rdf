package uk.ac.ebi.spot.atlas.rdf.profiles;

import uk.ac.ebi.spot.atlas.rdf.commons.ObjectInputStream;
import uk.ac.ebi.spot.rdf.model.Expression;
import uk.ac.ebi.spot.rdf.model.Profile;

public interface ProfileStreamFactory<T extends ProfileStreamOptions<X>, P
 extends Profile<X, ? extends Expression>,X> {

    ObjectInputStream<P> create(T options);

}
