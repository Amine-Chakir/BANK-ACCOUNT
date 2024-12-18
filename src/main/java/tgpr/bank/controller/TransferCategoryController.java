package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Category;
import tgpr.bank.model.TransferCategory;
import tgpr.bank.view.DisplayTransferInfoView;
import tgpr.framework.Controller;

public class TransferCategoryController extends Controller {

    private TransferCategory transferCategory;


    public TransferCategory getTransferCategory() {
        return transferCategory;
    }

    public void setTransferCategory(TransferCategory transferCategory) {
        this.transferCategory = transferCategory;
    }

    public void save(int account, int category, int transfer, int oldCategory) {
        transferCategory = new TransferCategory(category,transfer,account);
        transferCategory.save(category,transfer,account,oldCategory);
    }

    @Override
    public Window getView() {
        return null;
    }
}
