package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Agency;
import tgpr.bank.view.EditAgencyView;
import tgpr.bank.view.EditAgencyView;
import tgpr.framework.Controller;

public class EditAgencyController extends Controller {
    private final EditAgencyView view;
    private Agency agency;
    private final boolean isNew;

    public EditAgencyController() {
        this(null);
    }
    public EditAgencyController(Agency agency) {
        this.agency = agency;
        isNew = agency == null;
        view = new EditAgencyView(this, agency);
    }

    @Override
    public Window getView() {
        return view;
    }

    public Agency getAgency() {
        return agency;
    }


}
