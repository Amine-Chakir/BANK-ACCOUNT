package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.BankApp;
import tgpr.bank.model.Security;
import tgpr.bank.model.Transfer;
import tgpr.bank.model.User;
import tgpr.bank.view.LoginView;
import tgpr.framework.Controller;
import tgpr.framework.Error;
import tgpr.framework.ErrorList;
import tgpr.framework.Model;
import tgpr.bank.model.UserValidator;


import java.util.List;

public class LoginController extends Controller {
    public void exit() {
        System.exit(0);
    }

    public List<Error> login(String email, String password) {
        var errors = new ErrorList();
        errors.add(UserValidator.isValidEmail(email));
        errors.add(UserValidator.isValidPassword(password));

        if (errors.isEmpty()) {
            var user = User.checkCredentials(email, password);
            if (user != null) {
                Security.login(user);
      //          navigateTo(new MemberListController()); regardr le nom de contoler suivant
            } else
                showError(new Error("invalid credentials"));
        } else
            showErrors(errors);

        return errors;
    }

   public void seedData() {
        Model.seedData(BankApp.DATABASE_SCRIPT_FILE);
    }

    public void connectAsClient() {
        navigateTo(new AccountListController());
    }

    public void connectAsManager() {
        navigateTo(new AgencyListController());
    }

    public void connectAsAdmin() {
        navigateTo(new AdminAgenceListController());
    }

    @Override
    public Window getView() {
        return new LoginView(this);
    }
}
