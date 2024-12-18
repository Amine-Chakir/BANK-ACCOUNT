package tgpr.bank.model;

import tgpr.framework.Params;

import java.time.LocalDate;
import java.util.List;

public class RegularUser extends User {

    public RegularUser() {
        super();
    }
    public  RegularUser(String email, String password) {
        super(email,password);
    }

    public RegularUser(String email, String password, String last_name, String first_name, LocalDate birth_date,String type, int agency) {
        super(email, password, last_name, first_name, birth_date,type, agency);
    }

    public static List<RegularUser> getAllRegularUser() {
        return queryList(RegularUser.class, "select * from User order by email where manager=0");
    }

    public static RegularUser getRegularUserByEmail(String pseudo) {
        return queryOne(RegularUser.class, "select * from User where email=:email and manager=0",
                new Params("pseudo", pseudo));
    }
}
