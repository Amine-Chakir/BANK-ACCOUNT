package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;

import tgpr.bank.model.Agency;
import tgpr.bank.model.User;
import tgpr.bank.view.DisplayAgencyView;
import tgpr.framework.Controller;

import java.util.List;

public class DisplayAgencyController extends Controller{
    private final DisplayAgencyView view;
    private final Agency agency;
    private final User user;
    public DisplayAgencyController(Agency agency) {
        this.agency = agency;
        this.user = null;
        view = new DisplayAgencyView(this, agency);
    }

    public void delete() {
        if (askConfirmation("You are about to delete this member. Please confirm.", "Delete member")) {
//            user.delete();
            view.close();
        }
    }

    public  User addClient(Agency agency) {
        var controller = new CreatClientController(agency,user);
        navigateTo(controller);
        return controller.getClient();
    }
    @Override
    public Window getView() {
        return view;
    }
    public List<User> getFilteredMembers(String textFilter, Agency agency)
    {
        List<User> members = agency.getFilteredMember(textFilter);
        return members;
    }

    //Todo We have to pass all the member's parameters especially the agency so that we will be able to select it in the combobox in the CreatClientView the update case
    public void editMember(User member) {
        navigateTo(new CreatClientController(member));
    }
}

