package tgpr.bank.controller;
import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Account;
import tgpr.bank.model.Category;
import tgpr.bank.view.CategoryListView;
import tgpr.framework.Controller;

import java.util.List;

public class CategoryListController extends Controller {
    private final CategoryListView view;
    private List<Category> categories;
    private Category category;
    private Account account;
    private final boolean isNew;

    public CategoryListController() {
        this(null);
    }
    public CategoryListController(Category category){
        this.category=category;
        isNew=category==null;
        view=new CategoryListView(this,category);
    }



    @Override
    public Window getView() {
        return new CategoryListView(this,category);
    }

    public List<Category> getCategory() {
        return Category.getByaccount(account.getId());
    }
   /* public void save(String name,int idaccount) {
        category = new Category();
        category.save(name,idaccount);
        view.close();
    }*/
}
