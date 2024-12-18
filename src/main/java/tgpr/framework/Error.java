package tgpr.framework;

import java.util.List;

/**
 * <p>Cette classe modélise une erreur "métier".</p>
 * <p>Elle est constituée d'un message d'erreur et d'une liste de "champs"
 * auxquels se rapporte l'erreur. Généralement une erreur est associée à un seul champ, mais dans certains situations
 * on peut vouloir associer la même erreur à deux ou plusieurs champs (par exemple lorsqu'une date de début d'une
 * période est supérieure à la date de fin de la même période : dans ce cas on associera le message d'erreur aux
 * deux champs {@code dateDebut} et {@code dateFin}).</p>
 * <p>Les "champs" sont représentés par les valeurs d'un {@link Enum} défini au niveau d'une classe du modèle ou
 * d'un contrôleur.</p>
 * <p><u>Exemple:</u></p>
 * <p>Supposons que nous ayons une classe métier {@code Person} avec des attributs {@code lastName} et {@code firstName}.
 * On définira au sein de la classe {@code Person} l'enum suivant :</p>
 * <p><pre>{@code
 * enum Fields { LastName, FirstName; }
 * }</pre></p>
 * <p>On pourra ensuite, dans une méthode de validation par exemple, créer des erreurs et les associer à ces champs :</p>
 * <p><pre>{@code
 * var error1 = new Error("required", Person.Fields.LastName);
 * var error2 = new Error("lastname and firstname must be different",
 *                        Person.Fields.LastName,
 *                        Person.Fields.FirstName);
 * }</pre></p>
 */
public class Error {
    /**
     * Retourne une instance statique particulière de la classe {@link Error} qui représente le fait qu'il n'y a pas
     * d'erreur ;-)
     */
    public static final Error NOERROR = new Error("");

    private final String message;
    private final List<Enum> fields;

    /**
     * Retourne le message d'erreur.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Retourne la liste des "champs" sur lesquels porte l'erreur.
     */
    public List<Enum> getFields() {
        return fields;
    }

    /**
     * Permet de créer une {@link Error}.
     * @param error le message d'erreur
     * @param fields une liste explicite optionnelle de champs sur lesquels porte l'erreur
     */
    public Error(String error, Enum... fields) {
        this.message = error;
        this.fields = List.of(fields);
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
