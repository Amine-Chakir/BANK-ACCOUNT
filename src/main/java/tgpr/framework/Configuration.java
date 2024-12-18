package tgpr.framework;

import java.io.IOException;
import java.util.Properties;

/**
 * Permet de lire des valeurs de paramètres applicatifs stockés dans le fichier {@code /config.properties} des ressources
 * du projet.
 */
public class Configuration {
    private static final Properties props = new Properties();

    static {
        try {
            props.load(Configuration.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Permet de récupérer dans un {@code String} la valeur du paramètre dont le nom est passé en argument.
     * @param key le nom (la clef) du paramètre
     * @return la valeur du paramètre
     */
    public static String get(String key) {
        var res = props.getProperty(key);
        return res == null ? "" : res;
    }

    /**
     * Permet de récupérer dans un {@code int} la valeur du paramètre dont le nom est passé en argument.
     * @param key le nom (la clef) du paramètre
     * @return la valeur du paramètre
     */
    public static int getInt(String key) {
        var res = props.getProperty(key);
        return Integer.parseInt(res);
    }
}
