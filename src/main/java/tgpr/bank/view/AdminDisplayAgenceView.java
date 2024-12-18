package tgpr.bank.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.bank.controller.AdminDisplayAgenceController;
import tgpr.bank.model.Agency;


import java.util.List;

public class AdminDisplayAgenceView  extends DialogWindow {
    private final AdminDisplayAgenceController controller;
    private Agency agency;



    private  TextBox txtName;


    public AdminDisplayAgenceView(AdminDisplayAgenceController controller, Agency agency) {
        super("Edit Agency");

        this.controller = controller;
        this.agency = agency;

        setHints(List.of(Hint.CENTERED));
        setCloseWindowWithEscape(true);
        setFixedSize(new TerminalSize( 50, 3));
        Panel root = new Panel().setLayoutManager(new LinearLayout(Direction.VERTICAL).setSpacing(1));
        setComponent(root);





        createAgencyPanel().addTo(root);


        refresh();
    }


    private Panel createAgencyPanel() {
        var panel = new Panel()
                .setLayoutManager(new GridLayout(4).setTopMarginSize(1));
        txtName = new TextBox(new TerminalSize(20, 1)).addTo(panel);

        var buttons = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        new Button("update", this::update).addTo(buttons);
        new Button("delete", this::delete).addTo(buttons);
        new Button("close", this::close).addTo(buttons);
        panel.addComponent(buttons, LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        refresh();

        return panel;
    }



    private void update() {
        controller.update(txtName.getText(),agency.getName());

    }


    private void delete() {
        controller.delete(agency.getName());
    }
    private void refresh() {
        if (agency != null) {
            txtName.setText(agency.getName());
            // demande au contrôleur la liste des membres
            // ajoute l'ensemble des membres au tableau

        }
    }
}
