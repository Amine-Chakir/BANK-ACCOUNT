package tgpr.bank.view;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuBar;
import com.googlecode.lanterna.gui2.menu.MenuItem;
import com.googlecode.lanterna.input.KeyStroke;
import tgpr.bank.controller.AccountListController;
import java.util.List;

import com.googlecode.lanterna.gui2.table.Table;
import tgpr.bank.model.Account;
import tgpr.bank.model.Transfer;
import tgpr.framework.ColumnSpec;
import tgpr.framework.ObjectTable;
import tgpr.framework.Tools;
import tgpr.framework.ViewManager;
import com.googlecode.lanterna.TerminalSize;
import tgpr.bank.model.User;

    public class AccountListView extends BasicWindow {
    private final AccountListController controller;
    private final ObjectTable<Account> table;

    public AccountListView(AccountListController controller) {

        this.controller = controller;

        setTitle("Welcome to MSN"+User.getByEmail("bob@test.com"));

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

        // ajoute une ligne vide
        new EmptySpace().addTo(root);

        // crée un tableau de données pour l'affichage des membres
        table = new ObjectTable<>(
                new ColumnSpec<>("IBAN", Account::getIban),
                new ColumnSpec<>("Title", m -> Tools.ifNull(m.getTitle(), "")),
                new ColumnSpec<Account>("Type",Account::getType),
                new ColumnSpec<>("Floor", m -> Tools.ifNull(m.getFloor(), "")),
                new ColumnSpec<>("Saldo", m -> Tools.ifNull(m.getSaldo(), ""))
                );

        // ajoute le tableau au root panel
        root.addComponent(table);
        // spécifie que le tableau doit avoir la même largeur quee le terminal et une hauteur de 15 lignes
        table.setPreferredSize(new TerminalSize(ViewManager.getTerminalColumns(), 15));
        table.setSelectAction(() -> {
            var account = table.getSelected();
            controller.displayAccount(account, User.getByEmail("bob@test.com"));
            reloadData();
            table.setSelected(account);
        });
        new EmptySpace().addTo(root);



        // crée un bouton pour l'ajout d'un compte et lui associe une fonction lambda qui sera appelée
        // quand on clique sur le bouton
        var btnAddMember = new Button("New Transfer", () -> {
            Transfer m = controller.addTransfer();
            if (m != null)
                reloadData();
        }).addTo(root);
        // charge les données dans la table
        reloadData();
    }
    public void reloadData() {
        // vide le tableau
        table.clear();
        // demande au contrôleur la liste des comptes
        var accounts = controller.getAccounts();
        // ajoute l'ensemble des comptes au tableau
        table.add(accounts);
    }


}
