package tgpr.framework;

import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.mariadb.jdbc.MariaDbDataSource;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe de base pour les classes du modèle.
 */
public abstract class Model {
    private static Connection db = null;

    /**
     * Méthode abstraite à surcharger dans chaque classe du modèle pour copier (<i>mapper</i>) les données
     * récupérées en base de données (à travers un {@link ResultSet}) dans les attributs correspondant de la classe du modèle.
     *
     * @param rs le {@link ResultSet} contenant les données récupérées en base de données ; il est positionné sur
     *           l'enregistrement pour lequel on veut <i>mapper</i> les données vers l'instance courante.
     * @throws SQLException en cas d'erreur de lecture ou de confusion de format de données
     */
    protected abstract void mapper(ResultSet rs) throws SQLException;

    private static boolean checkIfDatabaseExists(String resourceName) throws SQLException {
        var server = Configuration.get("db.server");
        var database = Configuration.get("db.database");
        var user = Configuration.get("db.user");
        var password = Configuration.get("db.password");
        try {
            db = DriverManager.getConnection("jdbc:mariadb://" + server + ":3306/" + database + "?user=" + user + "&password=" + password);
        } catch (SQLException e) {
            if (e.getMessage().contains("Unknown database")) {
                db = DriverManager.getConnection("jdbc:mariadb://" + server + ":3306" + "?user=" + user + "&password=" + password);
                seedData(resourceName);
            }
        }
        try {
            if (db != null)
                return db.isValid(0);
            return false;
        } finally {
            if (db != null)
                db.close();
            db = null;
        }
    }

    private static Connection getDb() throws SQLException {
        var server = Configuration.get("db.server");
        var database = Configuration.get("db.database");
        var user = Configuration.get("db.user");
        var password = Configuration.get("db.password");
        if (db == null) {
            MariaDbDataSource ds = new MariaDbDataSource();
            ds.setUrl("jdbc:mariadb://" + server + ":3306/" + database);
            ds.setUser(user);
            ds.setPassword(password);
            if (Configuration.get("db.debug").equals("true"))
                db = ProxyDataSourceBuilder.create(ds)
                        .logQueryToSysOut()    // logQueryBySlf4j(), logQueryByJUL(), logQueryToSysOut()
                        .build().getConnection();
            else
                db = ds.getConnection();
        }
        return db;
    }

    /**
     * Vérifie si le gestionnaire de base de données tourne et est joignable.
     *
     * @return vrai ssi c'est le cas
     */
    public static boolean checkDb(String resourceName) {
        try {
            return checkIfDatabaseExists(resourceName);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Exécute le script SQL se trouvant dans le fichier dont le chemin est passé en paramètre. Ce fichier doit
     * être stocké dans les ressources du projet et le chemin doit correspondre à ce fichier.
     * @param resourceName le chemin vers le fichier ressource contenant le script SQL à exécuter
     */
    public static void seedData(String resourceName) {
        try {
            var res = Model.class.getResourceAsStream(resourceName);
            if (res != null) {
                ScriptRunner sr = new ScriptRunner(getDb());
                sr.setAutoCommit(false);
                Reader reader = new BufferedReader(new InputStreamReader(res));
                sr.runScript(reader);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected static <T extends Model> T newInstance(Class<T> clazz, ResultSet rs) {
        if (Modifier.isAbstract(clazz.getModifiers()))
            throw new RuntimeException("Trying to instanciate an abstract class. You should pass your own " +
                    "implementation of newInstance that creates instances of concrete classes inheriting from this class.");
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Effectue une requête SQL de lecture en base de données, supposée ne retourner qu'un seul enregistrement, et retourne
     * l'objet métier correspondant à l'enregistrement trouvé, en faisant appel à {@link #mapper(ResultSet)}.
     *
     * @param type   le type d'objet à retourner
     * @param sql    la requête SQL de type {@code SELECT} ; les éventuels paramètres sont représentés par {@code :name}
     *               où {@code name} représente le nom du paramètre
     * @param params la liste des valeurs pour les différents paramètres de la requête (voir {@link Params})
     * @param <T>    la classe correspondant au type d'objet à retourner (doit hériter de {@link Model})
     * @return une instance de la classe {@code T} contenant les données lues en base données
     */
    public static <T extends Model> T queryOne(Class<T> type, String sql, Params params) {
        T obj = null;
        try {
            var stmt = convertToUnnamedParams(sql, params);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                obj = newInstance(type, rs);
                assert obj != null;
                obj.mapper(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    /**
     * Effectue une requête SQL de lecture en base de données, supposée ne retourner qu'un seul enregistrement, et retourne
     * l'objet métier correspondant à l'enregistrement trouvé, en faisant appel à {@link #mapper(ResultSet)}.
     *
     * @param type   le type d'objet à retourner
     * @param sql    la requête SQL de type {@code SELECT} ; il s'agit d'une requête sans paramètre
     * @param <T>    la classe correspondant au type d'objet à retourner (doit hériter de {@link Model})
     * @return une instance de la classe {@code T} contenant les données lues en base données
     */
    public static <T extends Model> T queryOne(Class<T> type, String sql) {
        return queryOne(type, sql, new Params());
    }

    /**
     * Effectue une requête SQL de lecture en base de données, supposée ne retourner qu'un seul enregistrement, et retourne
     * l'objet métier correspondant à l'enregistrement trouvé, en faisant appel à {@link #mapper(ResultSet)}.
     *
     * @param getInstance retourne une nouvelle instance d'une classe du modèle en fonction de la valeur du discriminateur
     * @param sql    la requête SQL de type {@code SELECT} ; les éventuels paramètres sont représentés par {@code :name}
     *               où {@code name} représente le nom du paramètre
     * @param params la liste des valeurs pour les différents paramètres de la requête (voir {@link Params})
     * @param <T>    la classe correspondant au type d'objet à retourner (doit hériter de {@link Model})
     * @return une instance de la classe {@code T} ou d'une classe héritant de {@code T} contenant les données lues en base données
     */
    public static <T extends Model> T queryOne(Function<ResultSet, T> getInstance, String sql, Params params) {
        T obj = null;
        try {
            var stmt = convertToUnnamedParams(sql, params);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                obj = getInstance.apply(rs);
                assert obj != null;
                obj.mapper(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    /**
     * Effectue une requête SQL de lecture en base de données, supposée ne retourner qu'un seul enregistrement, et retourne
     * l'objet métier correspondant à l'enregistrement trouvé, en faisant appel à {@link #mapper(ResultSet)}.
     *
     * @param getInstance retourne une nouvelle instance d'une classe du modèle en fonction de la valeur du discriminateur
     * @param sql    la requête SQL de type {@code SELECT} ; il s'agit d'une requête sans paramètre
     * @param <T>    la classe correspondant au type d'objet à retourner (doit hériter de {@link Model})
     * @return une instance de la classe {@code T} ou d'une classe héritant de {@code T} contenant les données lues en base données
     */
    public static <T extends Model> T queryOne(Function<ResultSet, T> getInstance, String sql) {
        return queryOne(getInstance, sql, new Params());
    }

    /**
     * Méthode abstraite permettant de rafraîchir les données de l'objet métier courant en les récupérant à nouveau
     * depuis la base de données. Cette méthode doit être surchangée dans les classes héritantes.
     */
    public abstract void reload();

    /**
     * Permet de rafraîchir les données d'un objet métier en les récupérant à nouveau depuis la base de données.
     *
     * @param sql    la requête SQL à effectuer
     * @param params les valeurs des éventuels paramètres pour la requête
     */
    public void reload(String sql, Params params) {
        try {
            var stmt = convertToUnnamedParams(sql, params);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                this.mapper(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Effectue une requête SQL de lecture en base de données, supposée retourner zéro, un ou plusieurs enregistrements,
     * et retourne une liste contenant les objets métier correspondants aux enregistrements trouvés,
     * en faisant appel à {@link #mapper(ResultSet)}.
     *
     * @param type   le type d'objet à retourner
     * @param sql    la requête SQL de type {@code SELECT} ; les éventuels paramètres sont représentés par {@code :name}
     *               où {@code name} représente le nom du paramètre
     * @param params la liste des valeurs pour les différents paramètres de la requête (voir {@link Params})
     * @param <T>    la classe correspondant au type d'objet à retourner (doit hériter de {@link Model})
     * @return liste d'instances de la classe {@code T} contenant les données lues en base données
     */
    public static <T extends Model> List<T> queryList(Class<T> type, String sql, Params params) {
        List<T> list = new ArrayList<>();
        try {
            var stmt = convertToUnnamedParams(sql, params);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                T obj = newInstance(type, rs);
                assert obj != null;
                obj.mapper(rs);
                list.add(obj);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * Effectue une requête SQL de lecture en base de données, supposée retourner zéro, un ou plusieurs enregistrements,
     * et retourne une liste contenant les objets métier correspondants aux enregistrements trouvés,
     * en faisant appel à {@link #mapper(ResultSet)}.
     *
     * @param type   le type d'objet à retourner
     * @param sql    la requête SQL de type {@code SELECT} ; il s'agit d'une requête sans paramètre
     * @param <T>    la classe correspondant au type d'objet à retourner (doit hériter de {@link Model})
     * @return liste d'instances de la classe {@code T} contenant les données lues en base données
     */
    public static <T extends Model> List<T> queryList(Class<T> type, String sql) {
        return queryList(type, sql, new Params());
    }

    /**
     * Effectue une requête SQL de lecture en base de données, supposée retourner zéro, un ou plusieurs enregistrements,
     * et retourne une liste contenant les objets métier correspondants aux enregistrements trouvés,
     * en faisant appel à {@link #mapper(ResultSet)}.
     *
     * @param getInstance retourne une nouvelle instance d'une classe du modèle en fonction de la valeur du discriminateur
     * @param sql    la requête SQL de type {@code SELECT} ; les éventuels paramètres sont représentés par {@code :name}
     *               où {@code name} représente le nom du paramètre
     * @param params la liste des valeurs pour les différents paramètres de la requête (voir {@link Params})
     * @param <T>    la classe correspondant au type d'objet à retourner (doit hériter de {@link Model})
     * @return liste d'instances de la classe {@code T} ou de classes héritant de {@code T} contenant les données lues en base données
     */
    public static <T extends Model> List<T> queryList(Function<ResultSet, T> getInstance, String sql, Params params) {
        List<T> list = new ArrayList<>();
        try {
            var stmt = convertToUnnamedParams(sql, params);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                T obj = getInstance.apply(rs);
                assert obj != null;
                obj.mapper(rs);
                list.add(obj);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * Effectue une requête SQL de lecture en base de données, supposée retourner zéro, un ou plusieurs enregistrements,
     * et retourne une liste contenant les objets métier correspondants aux enregistrements trouvés,
     * en faisant appel à {@link #mapper(ResultSet)}.
     *
     * @param getInstance retourne une nouvelle instance d'une classe du modèle en fonction de la valeur du discriminateur
     * @param sql    la requête SQL de type {@code SELECT} ; il s'agit d'une requête sans paramètre
     * @param <T>    la classe correspondant au type d'objet à retourner (doit hériter de {@link Model})
     * @return liste d'instances de la classe {@code T} ou de classes héritant de {@code T} contenant les données lues en base données
     */
    public static <T extends Model> List<T> queryList(Function<ResultSet, T> getInstance, String sql) {
        return queryList(getInstance, sql, new Params());
    }

    /**
     * Effectue une requête SQL de lecture en base de données, supposée retourner zéro, un ou plusieurs enregistrements,
     * et retourne un {@link ResultSet} contenant les enregistrements trouvés.
     *
     * @param sql    la requête SQL de type {@code SELECT} ; les éventuels paramètres sont représentés par {@code :name}
     *               où {@code name} représente le nom du paramètre
     * @param params la liste des valeurs pour les différents paramètres de la requête (voir {@link Params})
     * @return le {@link ResultSet} contenant les enregistrements trouvés
     */
    public static ResultSet queryResultSet(String sql, Params params) {
        try {
            var stmt = convertToUnnamedParams(sql, params);
            return stmt.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Effectue une requête SQL d'écriture en base de données.
     *
     * @param sql    la requête SQL de type {@code INSERT}, {@code UPDATE} ou {@code DELETE} ;
     *               les éventuels paramètres sont représentés par {@code :name} où {@code name} représente le nom du paramètre
     * @param params la liste des valeurs pour les différents paramètres de la requête (voir {@link Params})
     * @return le nombre d'enregistrements mis à jour en base de données
     */
    public static int execute(String sql, Params params) {
        int count = 0;
        try {
            var stmt = convertToUnnamedParams(sql, params);
            count = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return count;
    }

    /**
     * Effectue une requête SQL de lecture en base de données, supposée ne retourner qu'une seule valeur scalaire et
     * retourne la valeur trouvée. Typiquement, cette méthode est utile pour effectuer une requête du type
     * {@code SELECT COUNT(*) FROM ...}.
     *
     * @param type   le type de valeur à retourner (typiquement Integer, String, Float, ...)
     * @param sql    la requête SQL de type {@code SELECT AGG(...) FROM ...} ; les éventuels paramètres sont représentés
     *               par {@code :name} où {@code name} représente le nom du paramètre
     * @param params la liste des valeurs pour les différents paramètres de la requête (voir {@link Params})
     * @param <T>    la classe correspondant au type d'objet à retourner (doit hériter de {@link Model})
     * @return la valeur scalaire trouvée
     */
    public static <T> T queryScalar(Class<T> type, String sql, Params params) {
        try {
            var stmt = convertToUnnamedParams(sql, params);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getObject(1, type);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static int getLastInsertId(Statement st) throws SQLException {
        ResultSet keys = st.getGeneratedKeys();
        if (keys.next())
            return keys.getInt(1);
        return 0;
    }

    /**
     * Effectue une requête SQL de type INSERT et retourne la clé primaire entière générée par auto-incrémentation.
     *
     * @param sql    la requête SQL de type {@code INSERT} ; les éventuels paramètres sont représentés par {@code :name}
     *               où {@code name} représente le nom du paramètre
     * @param params la liste des valeurs pour les différents paramètres de la requête (voir {@link Params})
     * @return la valeur de la clé primaire auto-générée
     */
    public static int insert(String sql, Params params) {
        int key = -1;
        try {
            var stmt = convertToUnnamedParams(sql, params);
            var count = stmt.executeUpdate();
            key = getLastInsertId(stmt);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return key;
    }

    private static PreparedStatement convertToUnnamedParams(String sql, Params params) throws SQLException {
        List<String> fields = new ArrayList<>();
        var caseInsensitiveParams = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        caseInsensitiveParams.putAll(params.getMap());
        Pattern findParametersPattern = Pattern.compile("(?<!')(:\\w+)(?!')");
        Matcher matcher = findParametersPattern.matcher(sql);
        while (matcher.find()) {
            String name = matcher.group().substring(1).toLowerCase();
            if (name.isBlank())
                throw new RuntimeException("Invalid named parameter in sql statement " + sql);
            if (!caseInsensitiveParams.containsKey(name))
                throw new RuntimeException("No value given for parameter '" + name + "' (received: " + caseInsensitiveParams + ")");
            fields.add(name);
        }
        PreparedStatement stmt = getDb().prepareStatement(sql.replaceAll(findParametersPattern.pattern(), "?"), Statement.RETURN_GENERATED_KEYS);
        for (int i = 0; i < fields.size(); ++i)
            stmt.setObject(i + 1, caseInsensitiveParams.get(fields.get(i)));
        return stmt;
    }
}
