package tgpr.bank.controller;
import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Agency;
import tgpr.bank.view.AdminAgenceListView;
import tgpr.framework.Controller;
import java.util.List;
public class AdminAgenceListController extends Controller {
    private List<Agency> agency;
    @Override
    public Window getView() {
        return new AdminAgenceListView(this);
    }
    public List<Agency> getAgency() {
        return Agency.getAll();
    }

    public void displayAgency(Agency agency) {
        navigateTo(new AdminDisplayAgenceController(agency));
    }

    public void logout() {
        navigateTo(new LoginController());
    }


    public void exit() {
        System.exit(0);
    }

    //public  Agency addClient() {
    //   var controller = new EditClientController();
    //  navigateTo(controller);
    // return (Agency) controller.getClient();
    //}
}
