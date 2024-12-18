package tgpr.bank.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.bank.model.Account;
import tgpr.framework.Tools;
import tgpr.bank.controller.DisplayCategoryController;
import tgpr.bank.model.Category;

import java.util.List;


public class DisplayCategoryView  extends DialogWindow {
    private final DisplayCategoryController controller;
    private Category category;
    private Account account;

    private final TextBox txtName;
    private final Label lblAccount;

    public DisplayCategoryView(DisplayCategoryController controller, Category category) {
        super("Edit Category");

        this.controller = controller;
        this.category = category;

        setHints(List.of(Hint.CENTERED));
        setCloseWindowWithEscape(true);

        Panel root = new Panel();
        setComponent(root);

        Panel fields = new Panel().setLayoutManager(new GridLayout(2).setTopMarginSize(1)).addTo(root);

        fields.addComponent(new Label("Name:"));
        txtName = new TextBox(new TerminalSize(24, 1)).addTo(fields);

        fields.addComponent(new Label("Account:"));
        lblAccount = new Label("").addTo(fields).addStyle(SGR.BOLD);

        new EmptySpace().addTo(root);

        var buttons = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        new Button("Update", this::update).addTo(buttons);
        new Button("Delete", this::delete).addTo(buttons);
        new Button("Close", this::close).addTo(buttons);
        root.addComponent(buttons, LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        refresh();

    }




    private void refresh() {
        if (category != null) {
            txtName.setText(category.getName());
            lblAccount.setText(category.isAccount() ? "local" : "system");
        }
    }
    private void delete() {
        controller.delete(category.getName(),category.getAccount());
    }


    private void update() {
         controller.update(txtName.getText(),category.getName(),category.getAccount());

    }

}
