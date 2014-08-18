package uk.ac.ebi.spot.atlas.rdf.commons;

import java.io.IOException;
import java.util.Enumeration;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class SequenceObjectInputStream<T> implements ObjectInputStream<T> {

    Enumeration<ObjectInputStream<T>> e;
    ObjectInputStream<T> in;

    public SequenceObjectInputStream(Enumeration<ObjectInputStream<T>> e) {
        this.e = e;
        try {
            nextStream();
        } catch (IOException ex) {
            // This should never happen
            throw new Error("panic");
        }
    }

    /**
     * Continues reading in the next stream if an EOF is reached.
     */
    final void nextStream() throws IOException {
        if (in != null) {
            in.close();
        }

        if (e.hasMoreElements()) {
            in = e.nextElement();
            if (in == null)
                throw new NullPointerException();
        } else in = null;

    }

    @Override
    public T readNext() {
        if (in == null) {
            return null;
        }
        T c = in.readNext();
        if (c == null) {
            try {
                nextStream();
            } catch (IOException ex) {
                throw new IllegalStateException("Next stream failed.", ex);
            }
            return readNext();
        }
        return c;
    }

    @Override
    public void close() throws IOException {
        do {
            nextStream();
        } while (in != null);
    }
}
