package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Account;
import tgpr.bank.model.Transfer;
import tgpr.bank.model.User;
import tgpr.bank.view.AccountListView;
import tgpr.bank.view.DisplayAccountView;
import tgpr.framework.Controller;

import java.util.List;

public class AccountListController extends Controller {

    private List<Account> accounts;

    public Window getView(){
        return new AccountListView(this);
    }
public List<Account> getAccounts()
{
    return Account.getAll("bob@test.com");
}
    public Transfer addTransfer() {
        var controller = new EditTransferController();
        navigateTo(controller);
        return controller.getTransfer();
    }
     public void logout() {
        navigateTo(new LoginController());
    }
    public void displayAccount(Account account, User user) {
        navigateTo(new DisplayAccountController(account, user));
    }
    public void exit() {
        System.exit(0);
    }



}
