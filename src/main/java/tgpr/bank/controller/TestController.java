package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import tgpr.bank.model.Test;
import tgpr.framework.Controller;

import java.io.IOException;
import java.util.List;

public class TestController extends Controller {
    //private final TestView view = new TestView(this);

    public TestController() throws IOException {
    }


    private Panel createContent() {
        Panel panel = new Panel().setLayoutManager(new LinearLayout(Direction.VERTICAL).setSpacing(1));


        return panel;
    }
    @Override
    public Window getView() {
        return null;
   }

    public List<Test> getData() {
        return Test.getAll();
    }

    public void displayTest(Test test) {
        showMessage(test.getName(), "Test Detail", MessageDialogButton.OK);
    }
}
