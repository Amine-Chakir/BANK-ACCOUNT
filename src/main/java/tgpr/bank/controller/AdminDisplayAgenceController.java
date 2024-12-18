package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.*;
import tgpr.bank.view.AdminDisplayAgenceView;
import tgpr.framework.Controller;

import java.util.List;

public class AdminDisplayAgenceController extends Controller {
    private final AdminDisplayAgenceView view;
    private final Agency agency;


    public AdminDisplayAgenceController(Agency agency) {
        this.agency = agency;
        view = new AdminDisplayAgenceView(this, agency);
    }
    public void delete(String name) {
        if (askConfirmation("You are about to delete this agency. Please confirm.", "Delete agency")) {
            agency.delete(name);
            view.close();
        }
    }

    public Agency update(String nvName,String name) {
        agency.update(nvName,name);
        view.close();
        return agency;
    }


    public List<Agency> getAgency() {
        return Agency.getAll();
    }
    public Window getView() {
        return view;
    }
}
