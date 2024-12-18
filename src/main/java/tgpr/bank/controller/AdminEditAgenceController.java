package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Agency;
import tgpr.bank.view.AdminEditAgenceView;
import tgpr.framework.Controller;

public class AdminEditAgenceController extends Controller {
    private final AdminEditAgenceView view;
    private Agency agency;
    private final boolean isNew;

    public AdminEditAgenceController() {
        this(null);
    }
    public AdminEditAgenceController(Agency agency) {
        this.agency = agency;
        isNew = agency == null;
        view = new AdminEditAgenceView(this, agency);
    }

    @Override
    public Window getView() {
        return view;
    }

    public Agency getAgency() {
        return agency;
    }


}
