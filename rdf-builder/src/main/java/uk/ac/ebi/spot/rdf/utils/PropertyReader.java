package uk.ac.ebi.spot.rdf.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class PropertyReader {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private Properties prop;
    private String propertiesFile;

    public PropertyReader() {

    }

    public PropertyReader (Properties p, String file) {
        this.prop = p;
        this.propertiesFile = file;
        try {
            InputStream in = this.getClass().getResourceAsStream(getPropertiesFile());
            prop.load(in);
            log.debug("properties read");
            in.close();
        } catch (IOException e) {
            log.error("Couldn't read properties file: " + getPropertiesFile());
            e.printStackTrace();
        }
    }

    public void setProp(Properties prop) {
        this.prop = prop;
    }

    public String getPropertiesFile() {
        return propertiesFile;
    }

    public void setPropertiesFile(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }


    public Properties getProp() {

        return prop;
    }
}
