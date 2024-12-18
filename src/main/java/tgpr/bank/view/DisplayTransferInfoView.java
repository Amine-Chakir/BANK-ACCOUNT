package tgpr.bank.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.input.KeyStroke;
import tgpr.bank.controller.DisplayTransferInfoController;
import tgpr.bank.model.Account;
import tgpr.bank.model.Category;
import tgpr.bank.model.Transfer;
import tgpr.framework.Tools;
import tgpr.framework.ViewManager;
import com.googlecode.lanterna.gui2.Window;

import javax.tools.Tool;
import java.util.List;

public class DisplayTransferInfoView extends DialogWindow {
    private final DisplayTransferInfoController controller;
    private Transfer transfer;
    private Account account;
    private final Label lblCreated_At;
    private final Label lblEffective_At;
    private final Label lblCreated_By;
    private final Label lblsrcAccount;
    private final Label lbltrgtAccount;
    private final Label lblAmount;
    private final Label lblSaldoAfterTransfer;
    private final Label lblDescription;
    private final Label lblState;
    //private final Label lblCategory;

    private final ComboBox<Category> cboCategroy;


    public DisplayTransferInfoView(DisplayTransferInfoController controller, Transfer transfer, Account account){
        super("View Transfer");

        this.controller = controller;
        this.transfer = transfer;
        this.account = account;

        setHints(List.of(Hint.CENTERED));
        setCloseWindowWithEscape(true);

        Panel root = new Panel();
        setComponent(root);

        Panel fields = new Panel().setLayoutManager(new GridLayout(2).setTopMarginSize(1)).addTo(root);
        fields.addComponent(new Label("Created At:"));
        lblCreated_At = new Label("").addTo(fields).addStyle(SGR.BOLD);
        fields.addComponent(new Label("Effective At:"));
        lblEffective_At = new Label("").addTo(fields).addStyle(SGR.BOLD);
        fields.addComponent(new Label("Created By:"));
        lblCreated_By = new Label("").addTo(fields).addStyle(SGR.BOLD);
        fields.addComponent(new Label("Source Account:"));
        lblsrcAccount = new Label("").addTo(fields).addStyle(SGR.BOLD);
        fields.addComponent(new Label("Target Account:"));
        lbltrgtAccount = new Label("").addTo(fields).addStyle(SGR.BOLD);
        fields.addComponent(new Label("Amount:"));
        lblAmount = new Label("").addTo(fields).addStyle(SGR.BOLD);
        fields.addComponent(new Label("Saldo after transfer:"));
        lblSaldoAfterTransfer = new Label("").addTo(fields).addStyle(SGR.BOLD);
        fields.addComponent(new Label("Description:"));
        lblDescription = new Label("").addTo(fields).addStyle(SGR.BOLD);
        fields.addComponent(new Label("State:"));
        lblState = new Label("").addTo(fields).addStyle(SGR.BOLD);
        fields.addComponent(new Label("Category:"));
        //lblCategory = new Label("").addTo(fields).addStyle(SGR.BOLD);
        cboCategroy = new ComboBox<Category>(getCategoryOrNull()).setPreferredSize(new TerminalSize(15, 1)).addTo(fields);
        new EmptySpace().addTo(root);
        createButtonsPanel().addTo(root);
        refresh();
    }

    private Panel createButtonsPanel(){
        var buttons = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL)).setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));;
        if (transfer.getState().equals("future")){
            var btnDelete = new Button("Delete", this::delete).addTo(buttons);
            ViewManager.addShortcut(this, btnDelete, KeyStroke.fromString("<A-u>"));
        }
        new Button("Save", this::save).addTo(buttons);
        new Button("Close", this::close).addTo(buttons);
        return buttons;
    }
    public Category getCategoryOrNull(){
        if (transfer.getCategory() == null){
            return controller.getCategoryByName("None");
        } else {
            return controller.getCategoryByName(transfer.getCategory());
        }
    }
    private void delete(){
        controller.delete();
    }
    private void save(){
        controller.saveCategory(cboCategroy.getSelectedItem(), this.transfer, this.account);
    }
    private void refresh() {
        if (transfer != null) {
            lblCreated_At.setText(Tools.toString(transfer.getCreated_at()));
            lblEffective_At.setText(Tools.toString(transfer.getEffective_at()));
            lblCreated_By.setText(transfer.getUser());
            lblsrcAccount.setText(transfer.getFullSourceAccount());
            lbltrgtAccount.setText(transfer.getFullTargetAccount());
            lblAmount.setText(Tools.toString(transfer.getAmount()));
            lblSaldoAfterTransfer.setText(Tools.toString(transfer.getSource_saldo()));
            lblDescription.setText(transfer.getDescription());
            lblState.setText(transfer.getState());
            for( var ctg : controller.getCategories()){
                if (!(ctg.getName().equals(transfer.getCategory())))
                    cboCategroy.addItem(ctg);
            }
           // lblCategory.setText(Tools.ifNull(transfer.getCategory(), " "));
        }
    }
}
