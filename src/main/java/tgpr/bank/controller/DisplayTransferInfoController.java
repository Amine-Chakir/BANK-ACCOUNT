package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Account;
import tgpr.bank.model.Category;
import tgpr.bank.model.Transfer;
import tgpr.bank.model.TransferCategory;
import tgpr.bank.view.DisplayTransferInfoView;
import tgpr.framework.Controller;

import java.util.List;

public class DisplayTransferInfoController extends Controller {
    private final DisplayTransferInfoView view;
    private final Transfer transfer;
    private final Account account;
    private TransferCategory transferCategory;

    public DisplayTransferInfoController(Transfer transfer, Account account){
        this.transfer = transfer;
        this.account = account;
        view = new DisplayTransferInfoView(this, transfer,account);
    }

    @Override
    public Window getView() {
        return view;
    }
    public List<Category> getCategories() {
        return Category.getAll();
    }
    public Category getCategoryByName(String name){
        return Category.getByName(name);
    }
    public void delete(){
        if (askConfirmation("You are about to delete this transfer. Please confirm.", "Delete transfer")){
            transfer.delete();
            view.close();
        }
    }
    public void saveCategory(Category category, Transfer transfer, Account account){
        if (category.getName().equals(transfer.getCategory())){
            showError("You have to select another category from the actual before you can save it");
        } else {
            transferCategory = new TransferCategory(category.getId(), transfer.getId(), transfer.getSource_account());
            transferCategory.save(category.getId(), transfer.getId(), account.getId(), transfer.getCategoryId());
            view.close();
        }

    }
}
