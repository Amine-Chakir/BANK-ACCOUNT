package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.*;
import tgpr.bank.view.DisplayAccountView;
import tgpr.bank.view.DisplayCategoryView;
import tgpr.framework.Controller;

import java.util.List;

public class DisplayAccountController extends Controller {
    private final DisplayAccountView view;
    private Account account;

    private  Category category;

    private Favourite favourite;


    public Transfer addTransfer() {
        var controller = new EditTransferController();
        navigateTo(controller);
        return controller.getTransfer();
    }
    public DisplayAccountController(Account account, User user) {
        this.account = account;
        view = new DisplayAccountView(this, account, user);
    }
    public void editCategory(Category category) {
        if(category.isAccount() == true)
            navigateTo(new DisplayCategoryController(category));
        else
            showError("You may not delete a system category");
    }
    public List<Transfer> getTransfers() {
        return account.getActualTransfer(account);
    }
    public List<Transfer> getFilteredTransfers(String txt, Account acnt){
        return account.getFilteredTransfers(txt, acnt);
    }
    public List<Account> getFavAcc(){
        return account.getFavourites(account);
    }
    public List<Account> getOthers(int user){
        return account.getOthers(user);
    }

    public boolean deleteFavourite(int userId, Account acc){
        this.favourite = new Favourite(userId, acc.getId());
        this.account = acc;
        if (askConfirmation("You are about to delete this favourite account. Please confirm. \n" + acc.getIbanWithTitle(), "Remove favourite")){
            return favourite.deleteById(acc.getId(), userId);
        }
        return false;
    }
    public String getIban(){
        return account.getIban();
    }
    public void editMember(Category category) {
        navigateTo(new DisplayCategoryController(category));
    }
    public void getHistoryInfo(Transfer transfer) {
        navigateTo(new DisplayTransferInfoController(transfer,account));
    }

    public List<Category> getCategory() {
        return Category.getByaccount(account.getId());
    }

    public void saveCategoryInTransfer(){

    }
    public void save(String name,int account) {
         category = new Category();
        category.save(name,account);
    }
    public void saveFav(int account, int user){
        favourite = new Favourite(account,user);
        favourite.save(user,account);
    }
    public Integer getCa(String name) {
        return Category.getUses(name,account.getId());
    }
    public Window getView() {
        return view;
    }
}
