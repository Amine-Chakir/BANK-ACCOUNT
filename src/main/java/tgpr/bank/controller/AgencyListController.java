package tgpr.bank.controller;


import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Agency;
import tgpr.bank.model.User;
import tgpr.bank.view.AgencyListView;
import tgpr.framework.Controller;

import java.util.List;

public class AgencyListController extends Controller {
    private List<Agency> agencys;

    @Override
    public Window getView() {
        return new AgencyListView(this);
    }

    public List<Agency> getAgencys() {
        return Agency.getAll();
    }

    //To add a client to the Agency we should verify that the logged user is a Manager
    //That's why we need to pass the user logged as a parameter
    public User addClient(User user) {
        var controller = new CreatClientController(user);
        navigateTo(controller);
        return (User) controller.getClient();
    }

    public void logout() {
        navigateTo(new LoginController());
    }

    public void CreatClient(Agency agency) {
        navigateTo(new DisplayAgencyController(agency));
    }

    public void exit() {
        System.exit(0);
    }


    public void f(Agency agency) {
        navigateTo(new DisplayAgencyController(agency));
    }

    public void editMember(Agency member) {
        navigateTo(new DisplayAgencyController(member));
    }



}



