package uk.ac.ebi.spot.atlas.rdf.profiles;

import uk.ac.ebi.spot.atlas.rdf.commons.ObjectInputStream;
import uk.ac.ebi.spot.rdf.model.Expression;

// <T extends Profile, ...> would avoid the interface be implemented by BaselineExpressionsInputStream
public interface ExpressionProfileInputStream<T, K extends Expression> extends ObjectInputStream<T> {
    T createProfile();
    void addExpressionToBuilder(K expression);
    void addGeneInfoValueToBuilder(String[] values);
}
