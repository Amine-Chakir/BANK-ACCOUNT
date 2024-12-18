package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import tgpr.bank.model.Agency;
import tgpr.bank.model.User;
import tgpr.bank.model.UserValidator;
import tgpr.bank.view.CreatClientView;
import tgpr.framework.Controller;
import tgpr.framework.ErrorList;
import tgpr.framework.Tools;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CreatClientController extends Controller{
    private final CreatClientView view ;
    private final User user;

    private  Agency agency;
    private Boolean isNew = null;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CreatClientController(Agency agency, User user)
    {
        this.user = user;
        this.agency = agency;
        isNew = user == null ;
        view = new CreatClientView(this, agency, (User) user);
    }
    public CreatClientController(User user) {
        this.user = user;
        isNew = user==null ;
        view = new CreatClientView(this, (tgpr.bank.model.User) user);
    }
    @Override
    public Window getView() {
        return view;
    }

    public User getClient() {
        return user;
    }

    public void save(String agency_name ,String Fname, String Lname, String BirthDate, String Email, String Password) {
          var errors = validate(String.valueOf(agency),Lname,Fname,BirthDate,Email,Password);

            Agency agency1 = new Agency(agency_name);
            var hashedPassword = Password.isBlank() ? Password : Tools.hash(Password);
            User u;
            u = User.createClient(Email, hashedPassword, Lname, Fname, Tools.toDate(BirthDate), agency1.getId());
            u.save();
            view.close();

    }
    public ErrorList validate(String role, String email, String birthDate, String password, String lname, String fname) {
        var errors = new ErrorList();
        LocalDate birthday_parsed = null;
        try{
            birthday_parsed = LocalDate.parse(birthDate, DATE_FORMATTER);

        }catch (Exception e)
        {
            errors.add("invalid birth date", tgpr.bank.model.User.Fields.BirthDate);
        }

        if (isNew) {
            errors.add(UserValidator.isValidEmail(email));
            errors.add(UserValidator.isValidPassword(password));
        }


//        if (!birthDate.isBlank() && !Tools.isValidDate(birthDate))
//            errors.add("invalid birth date", tgpr.bank.model.User.Fields.BirthDate);
        if (!password.equals(password))
            errors.add("must match password", tgpr.bank.model.User.Fields.Password);

        var hashedPassword = password.isBlank() ? password : Tools.hash(password);
        var u = tgpr.bank.model.User.createClient(email,password,lname, fname, birthday_parsed, agency != null ? agency.getId():0);
        errors.addAll(UserValidator.validate(u));
        return errors;
    }
    public void delete(User user)
    {
        if (askConfirmation("You are about to delete this client. Please confirm.", "Delete Client")) {
            user.delete();
            view.close();
        }
    }


}

