

package tgpr.bank;

import tgpr.bank.controller.AgencyListController;
import tgpr.bank.controller.CategoryListController;
import tgpr.bank.controller.TestController;

import tgpr.bank.model.Account;
import tgpr.framework.Controller;
import tgpr.framework.Model;
import tgpr.bank.controller.LoginController;

public class BankApp {
       //Controller.navigateTo(new AgencyListController());
       public final static String DATABASE_SCRIPT_FILE = "/database/tgpr-2223-a03.sql";


    public static void main(String[] args) {

        if (!Model.checkDb(DATABASE_SCRIPT_FILE))
            Controller.abort("Database is not available!");
        else
            Controller.navigateTo(new LoginController());

    }


    private static void testModel()
    {
        var account = Account.getAll("bob@test.com");
        for (var u : account)
            System.out.println(u);
    }

}



