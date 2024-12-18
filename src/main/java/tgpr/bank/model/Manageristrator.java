package tgpr.bank.model;

import tgpr.framework.Params;

import java.time.LocalDate;
import java.util.List;

public class Manageristrator extends User {

    public Manageristrator() {
        super();
    }

    public Manageristrator(String email, String password) {
        super(email, password);
    }

    public Manageristrator(String email, String password, String last_name, String first_name, LocalDate birth_date,int agency) {
        super(email, password, last_name, first_name,birth_date, agency);
    }

    public static List<RegularUser> getAllManageristrators() {
        return queryList(RegularUser.class, "select * from user,agency WHERE user.id=agency.manager ");
    }

    public static RegularUser getManageristratorByEmail(String email) {
        return queryOne(RegularUser.class, "select * from user,agency WHERE user.email=:email and user.id=agency.manager",
                new Params("email", email));
    }

}
