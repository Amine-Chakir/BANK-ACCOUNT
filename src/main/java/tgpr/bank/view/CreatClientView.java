package tgpr.bank.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.input.KeyStroke;
import tgpr.bank.controller.CreatClientController;
import tgpr.bank.model.Agency;
import tgpr.bank.model.User;
import tgpr.framework.Tools;
import tgpr.framework.ViewManager;

import java.util.List;

public class CreatClientView extends DialogWindow {
    private final CreatClientController controller;
    private final User user ;


    private  Agency agency;
    private TextBox txtFname;
    private  TextBox txtLname;
    private  TextBox txtBirthDate;
    private  TextBox txtEmail;
    private  TextBox txtPassword;

    private ComboBox<String> cboAgency;
    private final Label errFname = new Label("");
    private final Label errLname = new Label("");
    private final Label errBirthDate = new Label("");
    private final Label errEmail = new Label("");
    private final Label errPassword = new Label("");
    private final Label errAgency = new Label("");
    private  Button btnAddUpdate;

    public CreatClientView(CreatClientController controller, Agency agency, User user) {
        super((user == null ? "Add" : "Update") + "Client");
        this.user = user;
        this.agency = agency;
        this.controller = controller;
        setHints(List.of(Hint.CENTERED, Hint.FIXED_SIZE));
        setCloseWindowWithEscape(true);
        setFixedSize(new TerminalSize(40, 15));

        Panel root = new Panel().setLayoutManager(new LinearLayout(Direction.VERTICAL).setSpacing(1));
        setComponent(root);
        createFieldsGrid().addTo(root);
        createButtonsPanel().addTo(root);
        refresh();

        txtFname.takeFocus();
    }
    public CreatClientView(CreatClientController controller, User user) {
        super((user == null ? "Add" : "Update") + "Client");
        this.user = user;
        this.controller = controller;
        setHints(List.of(Hint.CENTERED, Hint.FIXED_SIZE));
        setCloseWindowWithEscape(true);
        setFixedSize(new TerminalSize(40, 15));
        Panel root = new Panel().setLayoutManager(new LinearLayout(Direction.VERTICAL).setSpacing(1));
        setComponent(root);
        createFieldsGrid().addTo(root);
        createButtonsPanel().addTo(root);
        refresh();
        txtFname.takeFocus();
    }


    private Panel createFieldsGrid() {
        var panel = new Panel()
                .setLayoutManager(new GridLayout(2).setTopMarginSize(1));

        new Label("Agency : ").addTo(panel);
        //Todo we should fill the combobox fronm the database agency table
        cboAgency = new ComboBox<>("Agency1", "Agency2", "Agency3").addTo(panel).setPreferredSize(new TerminalSize(11, 1));
        //if the user is assigned to an agency then we're in the update case and we need to put the user's agency in the combobox
        if(user != null)
        {
            Agency agency1 = new Agency();
            agency1.getById(user.getAgency());
            cboAgency.setSelectedItem(agency1.getName());
        }else
            cboAgency.setSelectedItem("Agency");
        new EmptySpace().addTo(panel);
        errAgency.addTo(panel).setForegroundColor(TextColor.ANSI.RED);

        new Label("First Name:").addTo(panel);
        txtFname = new TextBox(new TerminalSize(11, 1)).addTo(panel)
//                .setValidationPattern(Pattern.compile("[a-zA-Z][a-zA-Z0-9]{2,7}"))
                .setTextChangeListener((txt, byUser) -> validate());
        new EmptySpace().addTo(panel);
        errFname.addTo(panel)
                .setForegroundColor(TextColor.ANSI.RED);

        new Label("Last Name:").addTo(panel);
        txtLname = new TextBox(new TerminalSize(11, 1)).addTo(panel)
//                .setValidationPattern(Pattern.compile("[a-zA-Z][a-zA-Z0-9]{2,7}"))
                .setTextChangeListener((txt, byUser) -> validate());
        new EmptySpace().addTo(panel);
        errLname.addTo(panel)
                .setForegroundColor(TextColor.ANSI.RED);

        new Label("Birth Date:").addTo(panel);
        txtBirthDate = new TextBox(new TerminalSize(11, 1)).addTo(panel)
//                .setValidationPattern(Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$"))
                .setTextChangeListener((txt, byUser) -> validate());
        new EmptySpace().addTo(panel);
        errBirthDate.addTo(panel).setForegroundColor(TextColor.ANSI.RED);

        new Label("Email:").addTo(panel);
        txtEmail = new TextBox(new TerminalSize(11, 1)).addTo(panel)
//                .setValidationPattern(Pattern.compile("^(.+)@(\\S+)$"))
                .setReadOnly(user != null)
                .setTextChangeListener((txt, byUser) -> validate());
        new EmptySpace().addTo(panel);
        errEmail.addTo(panel).setForegroundColor(TextColor.ANSI.RED);

        new Label("Password:").addTo(panel);
        txtPassword = new TextBox(new TerminalSize(11, 1)).addTo(panel)
                .setMask('*')
                .setTextChangeListener((txt, byUser) -> validate());
        new EmptySpace().addTo(panel);
        errPassword.addTo(panel).setForegroundColor(TextColor.ANSI.RED);
        return panel;
    }

    private Panel createButtonsPanel() {

        var panel = new Panel()
                .setLayoutManager(new LinearLayout(Direction.HORIZONTAL))
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        new Button("Cancel", this::close).addTo(panel);
        new Button("Delete", this::delete).addTo(panel);
        btnAddUpdate = new Button(user == null ?"Add" : "Update",this::add).addTo(panel).setEnabled(false);
        ViewManager.addShortcut(this, btnAddUpdate, KeyStroke.fromString(user == null ? "<A-a>" : "<A-u>"));

        return panel;
    }
    private void refresh() {
        if (user != null) {
            cboAgency.setSelectedItem("Agency1");
            txtFname.setText(user.getFirst_name());
            txtLname.setText(user.getLast_name());
            txtEmail.setText(user.getEmail());
            txtBirthDate.setText(Tools.toString(user.getBirth_date()));
        }
    }


    private void add() {
        controller.save(cboAgency.getSelectedItem(),
                txtFname.getText(),
                txtLname.getText(),
                txtBirthDate.getText(),
                txtEmail.getText(),
                txtPassword.getText()
        );
    }

    private void delete() {
        controller.delete(user);
    }
    private void validate() {
        var errors = controller.validate(
                cboAgency.getText(),
                txtEmail.getText(),
                txtBirthDate.getText(),
                txtPassword.getText(),
                txtLname.getText(),
                txtFname.getText()
        );

        errFname.setText(errors.getFirstErrorMessage(User.Fields.Fname));
        errLname.setText(errors.getFirstErrorMessage(User.Fields.Lname));
        errPassword.setText(errors.getFirstErrorMessage(User.Fields.Password));
        errEmail.setText(errors.getFirstErrorMessage(User.Fields.Email));
        errBirthDate.setText(errors.getFirstErrorMessage(User.Fields.BirthDate));
        errAgency.setText(errors.getFirstErrorMessage(User.Fields.Agency));
        btnAddUpdate.setEnabled(errors.isEmpty());
    }
}


