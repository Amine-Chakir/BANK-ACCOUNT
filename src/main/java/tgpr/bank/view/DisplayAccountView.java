package tgpr.bank.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.input.KeyStroke;
import tgpr.bank.controller.CategoryListController;
import tgpr.bank.controller.DisplayAccountController;
import tgpr.bank.model.*;
import tgpr.framework.ColumnSpec;
import tgpr.framework.ObjectTable;
import tgpr.framework.Tools;
import tgpr.framework.ViewManager;

import java.text.DecimalFormat;
import java.util.List;

public class DisplayAccountView  extends DialogWindow {
    private final DisplayAccountController controller;
    private Account account;
    private User user;
    DecimalFormat df = new DecimalFormat("#.00");
    private final Label lblIban = new Label("");
    private final Label lblTitle = new Label("");
    private final Label lblSaldo = new Label("");
    private final Label lblType = new Label("");
    String email="bob@test.com";
    private ComboBox cbxFavAccount ;
    private ObjectTable<Transfer> historyTable;
    private ObjectTable<Category> categoryTable;
    private ObjectTable<Account> favorisTable;
    private final TextBox boxFilter = new TextBox(new TerminalSize(15, 1));

    private List<Account> l;

    private  TextBox txtCategory;
    private Account selectedBox;
    private  Button btnAddUpdate;
    private Category category;
    private Favourite favourite;
    public DisplayAccountView(DisplayAccountController controller, Account account, User user) {
        super("Account Details");

        this.controller = controller;
        this.account = account;
        this.user = user;


        setHints(List.of(Window.Hint.CENTERED));
        setCloseWindowWithEscape(true);

        Panel root = new Panel().setLayoutManager(new LinearLayout(Direction.VERTICAL).setSpacing(1));
        setComponent(root);
        Panel twoBorders = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL).setSpacing(1));
        Panel twoButtons = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL).setSpacing(1)).setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        createInfo().addTo(root);

        createHistoryPanel().addTo(root);

        twoBorders.addTo(root);
        createCategoryPanel().addTo(twoBorders);
        createFavorisPanel().addTo(twoBorders);
        twoButtons.addTo(root);

        var btnAddMember = new Button("New Transfer", () -> {
            Transfer m = controller.addTransfer();
            if (m != null)
                refresh();
        }).addTo(twoButtons);
        new Button("Close", this::close).addTo(twoButtons);

        refresh();

    }

    private Panel createInfo() {
        Panel panel = new Panel().setLayoutManager(new GridLayout(4).setTopMarginSize(1));

        panel.addComponent(new Label("IBAN:"));
        lblIban.addTo(panel).addStyle(SGR.BOLD);

        panel.addComponent(new Label("Type:"));
        lblType.addTo(panel).addStyle(SGR.BOLD);

        panel.addComponent(new Label("Title:"));
        lblTitle.addTo(panel).addStyle(SGR.BOLD);

        panel.addComponent(new Label("Saldo:"));
        lblSaldo.addTo(panel).addStyle(SGR.BOLD);

        return panel;
    }

    private Border createHistoryPanel() {
        var panel = new Panel().setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        Border border = panel.withBorder(Borders.singleLine(" History "));
        Panel filter = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL).setSpacing(1));
        new Label("Filter:").addTo(filter);
        boxFilter.addTo(filter).takeFocus().setTextChangeListener((txt, byUser) -> filtering());
        filter.addTo(panel);
        historyTable = new ObjectTable<>(
                new ColumnSpec<>("Effect Date", m -> Tools.toString(m.getDATUM())),
                new ColumnSpec<>("Description", Transfer::getDescription),
                new ColumnSpec<>("From/To", Transfer::getIban).setMinWidth(30),
                new ColumnSpec<>("Category", m -> Tools.ifNull(m.getCategory(), " ")),
                new ColumnSpec<>("Amount", Transfer::getAmount).alignRight().setMinWidth(15),
                new ColumnSpec<>("Saldo", Transfer::getSource_saldo).alignRight().setMinWidth(15),
                new ColumnSpec<>("State", Transfer::getState)

        ).addTo(panel);

        historyTable.setPreferredSize(new TerminalSize(ViewManager.getTerminalColumns(), 6));

        historyTable.setSelectAction(()  -> {
            var history = historyTable.getSelected();
            controller.getHistoryInfo(history);
            refresh();
            historyTable.setSelected(history);
            refresh();
           // historyTable.getSelected().delete();

        });
        return border;
    }
    public void filtering() {
        historyTable.clear();
        if (account != null) {
            if(boxFilter.getText() != "") {
                var transfers = controller.getFilteredTransfers(boxFilter.getText(), account);
                historyTable.add(transfers);
            }else {
                refresh();
            }
        }
    }

    private Border createFavorisPanel()
    {

        var panel = new Panel().setPreferredSize(new TerminalSize(ViewManager.getTerminalColumns() / 2, 10));
        Border border = panel.withBorder(Borders.singleLine(" Favourite Accounts "));

        favorisTable = new ObjectTable<>(
                new ColumnSpec<>("IBAN", Account::getIban).setMinWidth(30),
                new ColumnSpec<>("Type", Account::getType ).setMinWidth(10)
        ).addTo(panel);

        favorisTable.setSelectAction(() -> {
            var account = favorisTable.getSelected();
            controller.deleteFavourite(user.getId(), account);
            refresh();
        });

        new EmptySpace().addTo(panel);
        new EmptySpace().addTo(panel);
        new EmptySpace().addTo(panel);
        new EmptySpace().addTo(panel);



        cbxFavAccount = new ComboBox<>(Account.getOthers(user.getId())).addTo(panel);
        l = controller.getOthers(user.getId());
        //selectedBox = (Account) cbxFavAccount.getSelectedItem();
        /*cbxFavAccount.addListener((oldIndex, newIndex, byUser) -> {
            selectedBox = (Account) cbxFavAccount.getSelectedItem();
        });*/

        new Button("Add", this::addFav).addTo(panel);

        return border;

    }
    private Border createCategoryPanel() {
        var panel = new Panel().setPreferredSize(new TerminalSize(ViewManager.getTerminalColumns() / 2, 10));
        Border border = panel.withBorder(Borders.singleLine(" Category "));
        categoryTable = new ObjectTable<>(
                new ColumnSpec<>("Name", Category::getName).setMinWidth(30),
                new ColumnSpec<>("Type", m ->m.isAccount() ? "local" : "system"),
                new ColumnSpec<>("Uses", m->Tools.ifNull(controller.getCa(m.getName()),"0"))

                //new ColumnSpec<>("Name",  ));
        ).addTo(panel);

        categoryTable.setPreferredSize(new TerminalSize(ViewManager.getTerminalColumns(), 10));
        categoryTable.setSelectAction(() -> {
            var categorie = categoryTable.getSelected();
            controller.editCategory(categorie);

            reloadCategories();

            refresh();
            categoryTable.setSelected(categorie);
            refresh();
        });

        txtCategory = new TextBox(new TerminalSize(12, 1)).addTo(panel).setTextChangeListener((txt, byUser) -> validate());
        var buttons = new Panel().addTo(panel).setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        btnAddUpdate = new Button(category == null ? "Add" : "Update", this::add).addTo(buttons);
        new Button("Reset", this::rest).addTo(buttons);

        if (category != null) {
            txtCategory.setText(category.getName());
        }
        refresh();

        return border;
    }

    private void rest() {
        txtCategory.setText("");
    }
    private void addFav(){
        if (selectedBox != null) {
            controller.saveFav(selectedBox.getId(), user.getId());
            reloadFavourite();
        }

    }

    private void validate() {
    }


    private void add() {
        controller.save(
                txtCategory.getText(),
                account.getId()
        );
        reloadCategories();
    }

    public void reloadCategories(){
        categoryTable.clear();
        var categorys=controller.getCategory();
        categoryTable.add(categorys);
    }


    public void reloadTransfer(){
        historyTable.clear();
        var transfers = controller.getTransfers();
        historyTable.add(transfers);
    }

    public void reloadFavourite(){
        favorisTable.clear();
        var favourites = Account.getFavAccById(user.getId());
        favorisTable.add(favourites);
        cbxFavAccount.clearItems();
        l = controller.getOthers(user.getId());
        for (Account elem : l) {

            cbxFavAccount.addItem(elem.getIban()+" - "+elem.getTitle());
        }
    }

    public void refresh() {
        if (account != null) {

            lblIban.setText(account.getIban());
            lblTitle.setText(account.getTitle());
            lblType.setText(account.getType());
            lblSaldo.setText(Tools.ifNull(df.format(account.getSaldo()), "00") + " €");
            reloadTransfer();
            reloadCategories();
            if (favorisTable!=null) {
                reloadFavourite();
            }


        }
    }
    
}
