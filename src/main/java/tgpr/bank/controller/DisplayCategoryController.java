package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Account;
import tgpr.bank.model.Category;
import tgpr.bank.model.Transfer;
import tgpr.bank.model.TransferCategory;
import tgpr.bank.view.DisplayCategoryView;
import tgpr.framework.Controller;

public class DisplayCategoryController extends Controller {
    private final DisplayCategoryView view;
    private TransferCategory tr;
    private Transfer transfer;
    private Account account;

    //////////////////////////////////////////////////////////
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
////////////////////////////////////////////////////////////////
    private Category category;


    public DisplayCategoryController(Category category) {
        this.category = category;
        view = new DisplayCategoryView(this, category);
    }
    public void delete(String name,int account) {
        if (askConfirmation("You are about to delete this category. Please confirm.", "Delete category")) {
            category.delete(name,account);
            view.close();
        }
    }

    public Category update(String newName,String lastNmae,int account) {
        category.update(newName,lastNmae,account);
        view.close();
        return category;
    }

    @Override
    public Window getView() {
        return view;
    }
}
