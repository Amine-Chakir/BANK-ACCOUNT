package tgpr.bank.view;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuBar;
import com.googlecode.lanterna.gui2.menu.MenuItem;
import tgpr.bank.controller.AdminAgenceListController;
import tgpr.framework.ColumnSpec;
import tgpr.framework.ObjectTable;
import tgpr.framework.ViewManager;
import tgpr.bank.model.Agency;

import java.util.List;

public class AdminAgenceListView extends BasicWindow {

    private final AdminAgenceListController controller;
    private final ObjectTable<Agency> table;
    private Agency agency;

    public AdminAgenceListView(AdminAgenceListController controller) {
        this.controller = controller;

        setTitle("Welcome to Agency" +
                "");
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
        new Label(">> Your Agencies <<").addTo(root);
        table = new ObjectTable<>(

                new ColumnSpec<>("name", Agency::getName)
        );
        root.addComponent(table);

        table.setSelectAction(() -> {
            agency = table.getSelected();
            controller.displayAgency(agency);
            reloadData();
            table.setSelected(agency);
        });

        new EmptySpace().addTo(root);
        var btnNewClient = new Button("New Agency", () -> {
            // Agency m = controller.addClient();
            // if (m != null)
            //    reloadData();
        }).addTo(root);

        table.setPreferredSize(new TerminalSize(ViewManager.getTerminalColumns(), 10));
        reloadData();
        table.setPreferredSize(new TerminalSize(ViewManager.getTerminalColumns(), 10));
        reloadData();

    }

    public void reloadData() {
        table.clear();
        var agency = controller.getAgency();
        table.add(agency);

    }
}
