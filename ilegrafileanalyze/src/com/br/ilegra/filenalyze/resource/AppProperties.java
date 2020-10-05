package com.br.ilegra.filenalyze.resource;

import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Anizio Maia
 *
 */
public class AppProperties {

    public static final String PROPERTIES_SOFTWARE_VERSION = "software.version";
    public static final String PATH_FILE_IN       = "path.file.in";
    public static final String PATH_FILE_OUT      = "path.file.out";
    public static final String PATH_FILE_TEMP     = "path.file.temp";
    
    public static final String CHAR_SEPARATOR     = "char.separator";

    private static Properties properties;

    
    /**
     * retorna o arquivo de properties
     *
     * @return
     */
    private static Properties createProperties() {

        try {

            if (properties == null) {
                properties = new Properties();
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                InputStream file = loader.getResourceAsStream("com/br/ilegra/filenalyze/resource/fa.properties");

                if (file == null) {
                    throw new Exception("Não foi possível localizar o arquivo de properties!");
                }

                properties.load(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return properties;
    }


    /**
     * retorna o valor de uma propriedade
     *
     * @param key
     * @return
     */
    public static String getProperties(String key) {
        Properties p = createProperties();
        return p.getProperty(key);
    }
}
