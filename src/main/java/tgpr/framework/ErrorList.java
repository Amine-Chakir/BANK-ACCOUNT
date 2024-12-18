package tgpr.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe permet de gérer une liste d'erreurs métier (voir {@link Error}).
 */
public class ErrorList extends ArrayList<Error> {

    /**
     * Ajoute une erreur à la liste.
     * @param error l'erreur à ajouter
     * @return faux si {@code error} est {@code null} ou représente {@code Error.NOERROR} ; vrai dans les autres cas
     */
    @Override
    public boolean add(Error error) {
        if (error == null || error == Error.NOERROR)
            return false;
        super.add(error);
        return true;
    }

    /**
     * Permet d'ajouter un message d'erreur dans la liste, directement à partir du message et de la liste des "champs"
     * sur lesquels il porte.
     * @param message le message
     * @param fields les éventuels champs sur lesquels porte le message
     * @return faux ssi le message est {@code null}
     */
    public boolean add(String message, Enum... fields) {
        if (message == null)
            return false;
        return super.add(new Error(message, fields));
    }

    /**
     * Retourne le premier message d'erreur associé à un "champ" reçu en paramètre. C'est généralement celui que l'on
     * veut afficher en dessous du champ dans le formulaire d'encodage.
     * @param field le champ pour lequel on veut récupérer le premier message d'erreur
     * @return le message d'erreur en question
     */
    public String getFirstErrorMessage(Enum field) {
        return stream()
                .filter(err -> err.getFields().contains(field))
                .map(Error::getMessage)
                .findFirst()
                .orElse("");
    }

    /**
     * Permet de vérifier s'il y a au moins un message d'erreur associé à un champ donné en paramètre.
     * @param field le champ
     * @return vrai ssi il y a au moins un message d'erreur associé à ce champ
     */
    public boolean hasErrors(Enum field) {
        return stream()
                .anyMatch(err -> err.getFields().contains(field));
    }

    /**
     * Permet de récupérer la liste des messages d'erreur associés à un champ donné en paramètre.
     * @param field le champ
     * @return la liste des messages d'erreur associés
     */
    public List<String> getErrorMessages(Enum field) {
        return stream()
                .filter(err -> err.getFields().contains(field))
                .map(Error::getMessage).toList();
    }
}
