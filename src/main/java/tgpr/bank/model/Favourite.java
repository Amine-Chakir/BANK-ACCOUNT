package tgpr.bank.model;

import tgpr.framework.Model;
import tgpr.framework.Params;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Favourite extends Model  {

    private int user;
    private int account;


    public Favourite(int account,int user) {
        this.account = account;
        this.user = user;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }


    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        account = rs.getInt("account");
        user = rs.getInt("user");
    }
    public boolean deleteById(int account, int user){
        int c = execute("delete from favourite where user=:user AND account=:account", new Params().add("user", user).add("account", account));
        return c == 1;
    }
    public static List<Account> getFavAcc(String email)
    {
        return queryList(Account.class,"SELECT account.* from user,favourite,account WHERE favourite.user = user.id AND account.id = favourite.account AND user.email=:email",new Params("email",email));
    }

    public void save(int user,int account) {
        int c;
        String sql;
        sql="insert into favourite values (:user ,:account)";

        c = execute(sql,new Params().add("user",user).add("account",account));

    }
    public static Favourite getFavourite(int user, String iban){
        return queryOne(Favourite.class,"SELECT favourite.* from account,favourite\n" +
                "WHERE\n" +
                "user=:user AND account.iban=:iban AND account.id = favourite.account",new Params().add("user",user).add("iban",iban));
    }

    @Override
    public void reload() {
        reload();
    }
}
