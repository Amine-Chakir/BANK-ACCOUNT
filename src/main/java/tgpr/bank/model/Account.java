package tgpr.bank.model;

import tgpr.framework.Model;
import tgpr.framework.Params;
import javax.lang.model.element.TypeElement;
import java.time.LocalDate;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
public class Account extends Model {
    private String iban;
    private String title;
    private double floor;
    private String Type;
    private double saldo;
    private int id;




    public Account(String iban, String title, double floor, double saldo, int id,String type) {
        this.iban = iban;
        this.title = title;
        this.floor = floor;
        this.saldo = saldo;
        this.id = id;
        this.Type=type;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    @Override
    protected void mapper(ResultSet resultSet) throws SQLException {
        iban = resultSet.getString("iban");
        id = resultSet.getInt("id");
        title = resultSet.getString("title");
        floor = resultSet.getDouble("floor");
        saldo = resultSet.getDouble("saldo");
        Type=resultSet.getString("type");
    }


    @Override
    public void reload() {
        reload("select * from account ", new Params());

    }
    public Account()
    {}


    public String getIban() {
        return iban;
    }
    public String getIbanWithTitle(){
        return getIban() + " - " + getTitle();
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getFloor() {
        return floor;
    }

    public void setFloor(double floor) {
        this.floor = floor;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static List<Account> getAll(String email)
    {
        return queryList(Account.class,"SELECT account.* from account,access,user\n" +
                "WHERE\n" +
                "account.id = access.account\n" +
                "AND\n" +
                "access.user=user.id\n" +
                "AND \n" +
                "user.email = :email",new Params("email",email));

    }

    public static List<Account> getAllSaufTargetAccount(String email,String iban)
    {
        return queryList(Account.class,"SELECT account.* from account,access,user\n" +
                "                               WHERE\n" +
                "                               account.id = access.account\n" +
                "                                AND\n" +
                "                                access.user=user.id\n" +
                "                                AND\n" +
                "                                user.email = :email" +
                "                                AND\n" +
                "                             account.iban != :iban",new Params().add("email",email).add("iban",iban));
    }

    public static List<Account> getFavAcc(String email)
    {
        return queryList(Account.class,"SELECT account.* from user,favourite,account WHERE favourite.user = user.id AND account.id = favourite.account AND user.email=:email",new Params("email",email));
    }
    public static List<Account> getOthers(int user){
        return queryList(Account.class,
                "SELECT * FROM account WHERE account.id NOT IN(SELECT favourite.account FROM favourite WHERE favourite.user =:user)",
                new Params("user", user));
    }

    public static List<Account> getFavAccById(int id){
        return queryList(Account.class,"SELECT favourite.*, account.* from user,favourite,account WHERE favourite.user = user.id AND account.id = favourite.account AND user.id=:id",new Params("id",id));
    }
    public static Account getIdForAccountAbyIban(String iban)
    {
        return queryOne(Account.class,"SELECT * from account\n" +
                "WHERE\n" +
                "account.iban=:iban",new Params("iban",iban));
    }

    public List<Transfer> getActualTransfer(Account current) {
        return queryList(Transfer.class,
                // transfer.effective_at, transfer.description, account.iban, category.name, transfer.amount, transfer.source_saldo, transfer.state
                "SELECT * ,IFNULL(transfer.effective_at,transfer.created_at) DATUM \n" +
                        "FROM `transfer`\n" +
                        "LEFT JOIN transfer_category ON transfer.id = transfer_category.transfer\n" +
                        "LEFT JOIN category ON category.id = transfer_category.category\n" +
                        "LEFT JOIN account TGT ON TGT.id = transfer.target_account \n" +
                        "LEFT JOIN account SRC ON SRC.id = transfer.source_account\n" +
                        "LEFT JOIN user ON user.id = transfer.created_by\n" +
                        "LEFT JOIN account secondSRC ON secondSRC.id = transfer.target_account\n" +
                        "LEFT JOIN account secondTGT ON secondTGT.id = transfer.source_account\n" +
                        "WHERE (transfer.source_account =:id OR transfer.target_account =:id)\n" +
                        "ORDER BY DATUM ASC",
                new Params("account", current.id).add("id", id));
    }
    public List<Transfer> getFilteredTransfers(String txt, Account current){
        txt = '%' + txt + '%';
        return queryList(Transfer.class,
                // transfer.effective_at, transfer.description, account.iban, category.name, transfer.amount, transfer.source_saldo, transfer.state
                "SELECT * ,IFNULL(transfer.effective_at,transfer.created_at) DATUM \n" +
                        "FROM `transfer`\n" +
                        "LEFT JOIN transfer_category ON transfer.id = transfer_category.transfer\n" +
                        "LEFT JOIN category ON category.id = transfer_category.category\n" +
                        "LEFT JOIN account TGT ON TGT.id = transfer.target_account \n" +
                        "LEFT JOIN account SRC ON SRC.id = transfer.source_account\n" +
                        "LEFT JOIN user ON user.id = transfer.created_by\n" +
                        "LEFT JOIN account secondSRC ON secondSRC.id = transfer.target_account\n" +
                        "LEFT JOIN account secondTGT ON secondTGT.id = transfer.source_account\n" +
                        "WHERE (transfer.source_account =:id OR transfer.target_account =:id) AND\n" +
                        "(TGT.iban like :filter OR transfer.description like :filter OR category.name like :filter OR transfer.state like :filter)\n" +
                        "ORDER BY DATUM ASC",
                new Params("account", current.id).add("id", id).add("filter", txt));
    }
    public List<Account> getFavourites(Account current) {
        return queryList(Account.class,
                "SELECT * FROM account, favourite, user WHERE account.id = favourite.account AND favourite.user = user.id AND user.id =:id",
                new Params("account", current.id).add("id", id));
    }

    public static Account getById(int id) {
        return queryOne(Account.class, "select * from account where id=:id", new Params("id", id));
    }


    public static Account getIbanForAccountAbyid(int id)
    {
        return queryOne(Account.class,"SELECT * from account\n" +
                "WHERE\n" +
                "account.id=:id",new Params("id",id));

    }

    @Override
    public String toString() {
        return  iban+" " + '|' +" "+
                title+" " +'|'+" "+Type+" "+'|'+saldo;

    }

    public void Update(int id,Double solde)
    {
        String sql;
        sql="update account set saldo=:solde where id=:id";
        execute(sql,new Params().add("solde",solde).add("id",id));
    }


}
