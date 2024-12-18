package tgpr.bank.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import tgpr.bank.controller.LoginController;
import tgpr.framework.Configuration;
import tgpr.framework.Layouts;
import tgpr.framework.Panels;
import tgpr.framework.ViewManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginView extends BasicWindow  {

    private final LoginController controller;
    private final TextBox txtEmail;
    private final TextBox txtPassword;
    private final Button btnLogin;
    private final CheckBox sysDate;
    private final TextBox customDate;


    public LoginView(LoginController controller) {
        this.controller = controller;

        setTitle("Login");
        setHints(List.of(Hint.CENTERED));

        Panel root = new Panel();
        setComponent(root);

        Panel panel = new Panel().setLayoutManager(new GridLayout(2).setTopMarginSize(1).setVerticalSpacing(1))
                .setLayoutData(Layouts.LINEAR_CENTER).addTo(root);

        panel.addComponent(new Label("Email:"));

        txtEmail = new TextBox(new TerminalSize(15,1)).addTo(panel);

        panel.addComponent(new Label("Password:"));
        txtPassword = new TextBox(new TerminalSize(15,1)).setMask('*').addTo(panel);



        panel.addComponent(new Label("Use system Date/Time:"));
        sysDate = new CheckBox().setChecked(true).addTo(panel);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        panel.addComponent(new Label("Custum System Date/Time :"));
        customDate = new TextBox(new TerminalSize(15,1)).setText(dtf.format(now)).addTo(panel);
        sysDate.addListener(b -> customDate.setEnabled(!sysDate.isChecked()));


        new EmptySpace().addTo(root);

        Panel buttons = new Panel().setLayoutManager(new LinearLayout(Direction.HORIZONTAL))
                .setLayoutData(Layouts.LINEAR_CENTER).addTo(root);
        btnLogin = new Button("Login", this::login).addTo(buttons);
        Button btnExit = new Button("Exit", this::exit).addTo(buttons);

        new EmptySpace().addTo(root);

        Button btnSeedData = new Button("Reset Database", this::seedData);
        Panel debug = Panels.verticalPanel(LinearLayout.Alignment.Center,
                new Button("Login as default admin", this::logAsDefaultAdmin),
                new Button("Login as default manager", this::logAsDefaultManager),
                new Button("Login as default client", this::logAsDefaultClient),
                btnSeedData
        );
        debug.withBorder(Borders.singleLine(" For debug purpose ")).addTo(root);

        txtEmail.takeFocus();
    }



    private void seedData() {
        controller.seedData();
        btnLogin.takeFocus();
    }

    private void exit() {
        controller.exit();
    }

    private void login() {
        var errors = controller.login(txtEmail.getText(), txtPassword.getText());
        if (!errors.isEmpty()) {
            txtEmail.takeFocus();
        }
    }

    private void logAsDefaultAdmin() {
        controller.login(Configuration.get("default.admin.email"), Configuration.get("default.admin.password"));
        controller.connectAsAdmin();
    }

    private void logAsDefaultManager() {
        controller.connectAsManager();
    }

    private void logAsDefaultClient() {
        controller.connectAsClient();
    }


}
