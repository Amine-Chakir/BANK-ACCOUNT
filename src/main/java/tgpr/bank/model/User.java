package tgpr.bank.model;

import org.springframework.util.Assert;
import tgpr.framework.Model;
import tgpr.framework.Params;
import tgpr.framework.Tools;


import java.time.LocalDate;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;

public class User extends Model {

    public enum Fields {
        Fname,Lname,BirthDate,Email,Password,Agency
    }
    private String email ;
    private String password;
    private String last_name;
    private String first_name;
    private LocalDate birth_date;
    private int id;

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    private String Type;

    public User()
    {}

    private int agency;
    public int getAgency() {
        return agency;
    }
    public void setAgency(int agency) {
        this.agency = agency;
    }

    public User(String email, String password) {
        this.email=email;
        this.password=password ;
    }

    @Override
    public String toString() {
        return "("+email+ " - " + Type+" - use system date/time)";
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public enum Type {
        client,manager,admin
    }

    public User(String email, String password, String last_name, String first_name, LocalDate birth_date, int id) {
        this.email = email;
        this.password = password;
        this.last_name = last_name;
        this.first_name = first_name;
        this.birth_date = birth_date;
    }
    public User(String email, String password, String last_name, String first_name, LocalDate birth_date, String type,int agency ) {
        this.email = email;
        this.password = password;
        this.last_name = last_name;
        this.first_name = first_name;
        this.birth_date = birth_date;
        this.agency = agency;
    }
    public static User createClient(String email, String password, String last_name, String first_name, LocalDate birth_date, int agency){
        return new RegularUser(email,password,last_name,first_name,birth_date,"Client", agency) ;
    }
    public boolean isManager() {
        return this instanceof Manageristrator;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    public int hashCode() {
        // on retourne le hash code du pseudo qui est "unique" puisqu'il correspond à la clef primaire
        return Objects.hash(email);
    }

    /*protected void mapper(ResultSet resultSet) throws SQLException {
        email = resultSet.getString("email");
        password = resultSet.getString("password");
        first_name = resultSet.getString("first_name");
        birth_date = resultSet.getObject("birthdate", LocalDate.class);
        last_name= resultSet.getString("last_name");
    }

    @Override
    public void reloadEmail() {
        reload("select * from user where email=:email", new Params("email", email));
    }
*/
    protected static User newInstance(ResultSet rs) {
        try {
            return rs.getInt("admin") == 0 ? new RegularUser() : new Manageristrator();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<User> getFiltered(String filterText) {
        String filter = '%' + filterText + '%';
        Params params = new Params("filter", filter);
        String sql = "select * from user where (first_name like :filter or last_name like :filter or birth_date like :filter)";
        return queryList(User::newInstance, sql, params);
    }

    public User save() {
        int c;
        User m = getByEmail(email);
        String sql;
        if (m == null) {
//            Assert.isTrue(Security.isManager(), "Only manager may create client");
            sql = "insert into user (email,password,last_name,first_name,birth_date,agency) " +
                    "values (:email,:password,:last_name,:first_name,:birth_date, :agency_id)";
        } else {
//            Assert.isTrue(Security.isManager() || Security.isLoggedUser(this), "Update only allowed if manager or own data");
//            Assert.isTrue(!Security.isLoggedUser(this) || m.isManager() == isManager(), "Connected user may not change his own role");
            if (password == null || password.isBlank())
                sql = "update user set last_name=:last_name, first_name=:first_name, " +
                        "birth_date=:birth_date,agency=:agency_id,password=:password  where email=:email";
            else
                sql = "update user set password=:password, first_name=:first_name,last_name=:last_name, " +
                        "birth_date=:birth_date,agency=:agency_id,password=:password where email=:email";
        }
        c = execute(sql, new Params()
                .add("email", email)
                .add("password", password)
                .add("last_name", last_name)
                .add("first_name", first_name)
                .add("birth_date", birth_date)
                .add("agency_id",agency));
        Assert.isTrue(c == 1, "Something went wrong");
        return this;
    }

    public void delete() {
//        Assert.isTrue(Security.isManager(), "Only manager may delete members");
        execute("delete from user where email=:email", new Params("email", email));
    }


    public User ( String first_name , String last_name ,  LocalDate birth_date, String email) {
        this.first_name=first_name;
        this.last_name=last_name;
        this.birth_date=birth_date;
        this.email=email;

    }

    @Override
    public void reload() {
        reload("SELECT user.last_name,user.first_name,user.birth_date,user.email FROM user,agency WHERE user.agency=agency.id and agency.id=1;", new Params("id", id));
    }


    public static User getById(String name) {
        return queryOne(User.class, "SELECT agency.id FROM agency WHERE agency.name=\"agency1\";", new Params("name", name));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public LocalDate getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(LocalDate birth_date) {
        this.birth_date = birth_date;
    }

    protected void mapper(ResultSet resultSet) throws SQLException {
        email = resultSet.getString("email");
        id = resultSet.getInt("id");
        first_name = resultSet.getString("first_name");
        last_name = resultSet.getString("last_name");
        password = resultSet.getString("password");
        birth_date = resultSet.getObject("birth_date", LocalDate.class);
        Type=resultSet.getString("type");
    }

    public static List<User> getAll()
    {
        return queryList(User.class,"select * from user order by email");
    }

    public static  User checkCredentials(String email, String password) {
        var user = User.getByEmail(email);
        if (user != null && user.password.equals(Tools.hash(password)))
            return user;
        return null;
    }



    public static User getByEmail(String email) {
     return queryOne(User.class,"select * from user where email=:email",new Params("email",email));
    }



}

