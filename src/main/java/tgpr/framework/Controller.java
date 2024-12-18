package tgpr.framework;

import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import tgpr.bank.model.Agency;

import java.util.List;

/**
 * Classe abstraite de base pour les contrôleurs.
 */
public abstract class Controller {
    /**
     * Permet de récupérer la vue associée au contrôleur. Cette méthode doit être surchargée dans les classes héritantes.
     * @return la vue en question
     */
    public abstract Window getView();

    /**
     * Méthode principale pour permettre la navigation dans l'application : un contrôleur permet de naviguer vers un
     * autre contrôleur passé en paramètre. Concrètement, c'est la vue associée au contrôleur vers lequel on navigue
     * qui sera affichée.
     * @param controller le contrôleur vers lequel on veut naviguer
     */
    public static void navigateTo(Controller controller) {
        Window view = controller.getView();
        if (view instanceof DialogWindow)
            ViewManager.showModal((DialogWindow) view);
        else
            ViewManager.navigateTo(view);
    }

    /**
     * Permet d'afficher une boîte de dialogue modale présentant les erreurs reçues en paramètre.
     * Les erreurs sont représentées par des instances de {@link Error}.
     * @param errors la liste des erreurs à afficher
     */
    public static void showErrors(List<Error> errors) {
        ViewManager.showErrors(errors);
    }

    /**
     * Permet d'afficher une boîte de dialogue modale présentant une erreur reçue en paramètre.
     * L'erreur est représentée par une instances de {@link Error}.
     * @param error l'erreur à afficher
     */
    public static void showError(Error error) {
        ViewManager.showErrors(List.of(error));
    }

    /**
     * Permet d'afficher une boîte de dialogue modale présentant les erreurs reçues en paramètre, en spécifiant le titre
     * de la fenêtre d'erreur.
     * Les erreurs sont représentées par des instances de {@link Error}.
     * @param errors la liste des erreurs à afficher
     * @param title titre de la fenêtre d'erreur
     */
    public static void showErrors(List<Error> errors, String title) {
        ViewManager.showErrors(errors, title);
    }

    /**
     * Permet d'afficher une boîte de dialogue modale présentant une erreur reçue en paramètre, en spécifiant le titre
     * de la fenêtre d'erreur.
     * L'erreur est représentée par une instances de {@link Error}.
     * @param error l'erreur à afficher
     * @param title titre de la fenêtre d'erreur
     */
    public static void showError(Error error, String title) {
        ViewManager.showErrors(List.of(error), title);
    }

    /**
     * Permet d'afficher une boîte de dialogue modale présentant une erreur reçue en paramètre, en spécifiant le titre
     * de la fenêtre d'erreur.
     * L'erreur est représentée par un {@code String}.
     * @param error l'erreur à afficher
     * @param title titre de la fenêtre d'erreur
     */
    public static void showError(String error, String title) {
        showError(new Error(error), title);
    }

    /**
     * Permet d'afficher une boîte de dialogue modale présentant une erreur reçue en paramètre.
     * L'erreur est représentée par un {@code String}.
     * @param error l'erreur à afficher
     */
    public static void showError(String error) {
        showError(new Error(error));
    }

    /**
     * Permet d'afficher une boîte de dialogue modale présentant un titre et un message reçus en paramètre, ainsi qu'une
     * liste optionnelle de boutons prédéfinis à afficher.
     * @param message le message
     * @param title le titre
     * @param buttons la liste explicite des boutons (parmi les valeurs de {@link MessageDialogButton})
     * @return le bouton que l'utilisateur a utilisé pour fermer la boîte de dialogue de message
     */
    public static MessageDialogButton showMessage(String message, String title, MessageDialogButton... buttons) {
        return ViewManager.showMessage(message, title, buttons);
    }

    /**
     * Permet d'afficher une boîte de dialogue modale présentant un titre et un message reçus en paramètre et demandant
     * à l'utilisateur de confirmer ou d'infirmer l'assertion ou la question qui se trouve dans le message en répondant
     * {@code Yes} ou {@code No}.
     * @param message le message
     * @param title le titre
     * @return le bouton que l'utilisateur a choisi ({@code Yes} ou {@code No})
     */
    public static boolean askConfirmation(String message, String title) {
        return ViewManager.showMessage(message, title, MessageDialogButton.Yes, MessageDialogButton.No) == MessageDialogButton.Yes;
    }

    /**
     * Permet d'avorter l'exécution de l'application après avoir affiché un message d'erreur passé en paramètre.
     * @param error l'erreur à afficher
     */
    public static void abort(String error) {
        showError(new Error(error), "Abort");
        System.exit(1);
    }


}
