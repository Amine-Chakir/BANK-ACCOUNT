package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Category;
import tgpr.bank.view.EditCategoryView;
import tgpr.framework.Controller;

public class EditCategoryController extends Controller {
    private final EditCategoryView view;
    private Category category;
    private final boolean isNew;

    public EditCategoryController() {
        this(null);
    }
    public EditCategoryController(Category category) {
        this.category = category;
        isNew = category == null;
        view = new EditCategoryView(this, category);
    }

    @Override
    public Window getView() {
        return view;
    }

    public Category getCategory() {
        return category;
    }
}
