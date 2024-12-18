package tgpr.bank.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.bank.controller.EditCategoryController;
import tgpr.bank.model.Category;

import java.util.List;

public class EditCategoryView extends DialogWindow {
    private final EditCategoryController controller;

    private final Category category;

    public EditCategoryView(EditCategoryController controller, Category category) {
        // définit le titre de la fenêtre
        super((category == null ? "Add " : "Update ") + "Category");

        this.category = category;
        this.controller = controller;

        setHints(List.of(Hint.CENTERED, Hint.FIXED_SIZE));
        // permet de fermer la fenêtre en pressant la touche Esc
        setCloseWindowWithEscape(true);
        // définit une taille fixe pour la fenêtre de 15 lignes et 70 colonnes
        setFixedSize(new TerminalSize(70, 15));


    }
}
