package tgpr.bank.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.bank.controller.AdminEditAgenceController;
import tgpr.bank.model.Agency;

import java.util.List;

public class AdminEditAgenceView extends DialogWindow {
    private final AdminEditAgenceController controller;

    private final Agency agency;

    public AdminEditAgenceView(AdminEditAgenceController controller, Agency agency) {
        // définit le titre de la fenêtre
        super((agency == null ? "Add " : "Update ") + "Agency");

        this.agency = agency;
        this.controller = controller;

        setHints(List.of(Hint.CENTERED, Hint.FIXED_SIZE));
        // permet de fermer la fenêtre en pressant la touche Esc
        setCloseWindowWithEscape(true);
        // définit une taille fixe pour la fenêtre de 15 lignes et 70 colonnes
        setFixedSize(new TerminalSize(70, 15));


    }
}
