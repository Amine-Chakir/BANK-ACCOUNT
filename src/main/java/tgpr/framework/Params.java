package tgpr.framework;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Cette classe représente une liste de paramètres (avec leur valeur) pour une requête SQL.</p>
 * <p>Dans la requête SQL, on représente un paramètre en lui donnant un nom précédé du caractère {@code ':'}.</p>
 * <p>Par exemple:</p>
 * <p><pre>{@code
 * SELECT * FROM person WHERE last_name = :lastName
 *                        AND first_name = :firstName
 * }</pre></p>
 * <p>Pour exécuter une telle requête, on aura besoin d'une instance de {@link Params} contenant une valeur pour
 * chacun des paramètres</p>
 * <p><pre>{@code
 * var params = new Params().add("lastName", "Smith")
 *                          .add("firstName", "John");
 * }</pre></p>
 * <p>ou encore :</p>
 * <p><pre>{@code
 * var params = new Params("lastName", "Smith")
 *                    .add("firstName", "John");
 * }</pre></p>
 * <p><u>Remarques :</u></p>
 * <ul>
 * <li>Lorsqu'un même paramètre est utilisé plusieurs fois dans la requête SQL, il ne doit être ajouté qu'une seule fois
 * dans {@link Params}.</li>
 * <li>Un paramètre ajouté dans {@link Params} ne doit pas forcément être utilisé dans la requête SQL ; le contraire
 * n'est pas vrai.</li>
 * <li>En revanche, un paramètre utilisé dans la requête SQL doit <u>obligatoirement</u> être ajouté dans {@link Params}.</li>
 * </ul>
 */
public class Params {
    private final HashMap<String, Object> map = new HashMap<>();

    /**
     * Crée une instance "vide" (aucun paramètre défini).
     */
    public Params() {
    }

    /**
     * Crée une instance et y ajoute directement un premier paramètre dont le nom et la valeur sont fournis en argument.
     * @param name le nom du paramètre
     * @param value la valeur pour le paramètre
     */
    public Params(String name, Object value) {
        add(name, value);
    }

    /**
     * Ajoute un paramètre dont le nom et la valeur sont fournis en argument.
     * @param name le nom du paramètre
     * @param value la valeur pour le paramètre
     */
    public Params add(String name, Object value) {
        map.put(name, value);
        return this;
    }

    /**
     * Vérifie si un paramètre portant un nom donné a déjà été défini.
     * @param name le nom du paramètre
     * @return vrai ssi un paramètre répondant à ce nom existe déjà
     */
    public boolean contains(String name) {
        return map.containsKey(name);
    }

    /**
     * Retourne la valeur associée au paramètre portant le nom donné.
     * @param name le nom du paramètre
     * @return la valeur du paramètre ou {@code null} s'il n'est pas défini
     */
    public Object get(String name) {
        return map.get(name);
    }

    /**
     * Retourne le {@link Map} interne contenant tous les paramètres et leur valeur.
     */
    Map<String, Object> getMap() {
        return map;
    }
}
