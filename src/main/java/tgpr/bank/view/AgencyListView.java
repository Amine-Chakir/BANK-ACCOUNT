package tgpr.bank.view;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuBar;
import com.googlecode.lanterna.gui2.menu.MenuItem;
import tgpr.bank.controller.AgencyListController;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import tgpr.bank.model.User;
import tgpr.framework.ColumnSpec;
import tgpr.framework.ObjectTable;
import tgpr.framework.ViewManager;
import tgpr.bank.model.Agency;
import java.util.List;

public class AgencyListView extends BasicWindow {
    private final AgencyListController controller;
    private final ObjectTable<Agency> table;
    private User user;


    public AgencyListView(AgencyListController controller) {
        this.controller = controller;

        setTitle("Welcome to MSN" + User.getByEmail("bob@test.com"));
        setHints(List.of(Hint.EXPANDED));
        Panel root = new Panel();

        setComponent(root);
        MenuBar menuBar = new MenuBar().addTo(root);
        Menu menuFile = new Menu("File");
        menuBar.add(menuFile);
        MenuItem menuLogout = new MenuItem("Logout", controller::logout);
        menuFile.add(menuLogout);
        MenuItem menuExit = new MenuItem("Exit", controller::exit);
        menuFile.add(menuExit);
        new EmptySpace().addTo(root);
        new EmptySpace().addTo(root);
        new EmptySpace().addTo(root);
        new Label(">> Your Agencies <<").addTo(root);
        table = new ObjectTable<>(new ColumnSpec<>("name", Agency::getName));
        root.addComponent(table);
        new EmptySpace().addTo(root);
        var btnNewClient = new Button("New Client", () -> {
            User m = controller.addClient(user);
            if (m != null) reloadData();
        }).addTo(root);

        table.setPreferredSize(new TerminalSize(ViewManager.getTerminalColumns(), 15));
        reloadData();
        // spécifie l'action a exécuter quand on presse Enter ou la barre d'espace
        table.setSelectAction(() -> {
            var member = table.getSelected();
            controller.editMember(member);
            reloadData();
            table.setSelected(member);
        });
    }

    public void reloadData() {
        table.clear();
        var agencys = controller.getAgencys();
        table.add(agencys);
    }


    }
