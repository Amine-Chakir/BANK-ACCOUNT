package tgpr.bank.view;


import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.bank.controller.DisplayAgencyController;
import tgpr.bank.model.Agency;
import tgpr.bank.model.User;
import tgpr.framework.ColumnSpec;
import tgpr.framework.ObjectTable;
import tgpr.framework.ViewManager;

import java.util.List;


public class DisplayAgencyView  extends DialogWindow{

    private final DisplayAgencyController controller;
    private Agency agency;
    private final TextBox txtFilter = new TextBox(new TerminalSize(15, 1));
    private final ObjectTable<User> table;


    public DisplayAgencyView(DisplayAgencyController controller, Agency agency) {
        super("Agency Detaills");

        this.controller = controller;
        this.agency = agency;

        setHints(List.of(Hint.CENTERED));
        setCloseWindowWithEscape(true);

        //Set a resized panel from the beginning
        Panel root = new Panel().setLayoutManager(new LinearLayout(Direction.VERTICAL).setSpacing(1));
        setComponent(root);
        createFilterPanel().addTo(root);
        //Remplir la liste des membres de l'agence en question
        //Récupérer d'abord la liste des mebmres de cette agence
        new Label(">> Agency Members <<").addTo(root) ;
//        User member
        table = new ObjectTable<>(
                new ColumnSpec<>("first_name",User::getFirst_name),
                new ColumnSpec<>("last_name", User::getLast_name),
                new ColumnSpec<>("birth_date", User::getBirth_date),
                new ColumnSpec<>("email", User::getEmail)
        );

        root.addComponent(table);
        new EmptySpace().addTo(root);
        //table.setPreferredSize(new TerminalSize(ViewManager.getTerminalColumns(), 4));
        var buttons = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        var btnNewClient = new Button("New Client", () -> {
            User m = controller.addClient(agency);
            if (m != null)
                reloadData();
        }).addTo(root);
        btnNewClient.addTo(buttons);
        new Button("Close", this::close).addTo(buttons);
        root.addComponent(buttons, LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        // spécifie l'action a exécuter quand on presse Enter ou la barre d'espace
        table.setSelectAction(() -> {
            var member = table.getSelected();
            controller.editMember(member);
            reloadData();
            table.setSelected(member);
        });
        reloadData();

    }
    private Panel createFilterPanel() {
        Panel panel = new Panel()
                .setLayoutManager(new GridLayout(1).setLeftMarginSize(1));

        new Label("Filter:").addTo(panel);
        txtFilter.addTo(panel).takeFocus().setTextChangeListener((txt, byUser) -> filterData());
        return panel;
    }

    public void reloadData()
    {
        table.clear();
        if (agency != null)
        {
            List<User> members = agency.getAllMembers();
            table.add(members);
        }
    }
    public void filterData()
    {
        table.clear();
        if (agency != null)
        {
            if(txtFilter.getText() != "")
            // demande au contrôleur la liste filtrée des clients
            {
                var members = controller.getFilteredMembers(txtFilter.getText(), agency);
                table.add(members);
            }else
            // recharger tous les clients si le txtFilter est vide donc pas de filtrage
            {
                reloadData();
            }
        }
    }

    private void update() {
//      agency = controller.update();
        reloadData();
    }

    private void delete() {
        controller.delete();
    }
}


