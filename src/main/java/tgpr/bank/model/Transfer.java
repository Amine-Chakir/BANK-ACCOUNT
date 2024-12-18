package tgpr.bank.model;


import org.springframework.util.Assert;
import tgpr.framework.Params;
import tgpr.framework.Model;
import tgpr.framework.Tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static tgpr.framework.Model.queryList;
import static tgpr.framework.Model.queryOne;

public class Transfer extends Model {

    public enum Fields {
        amount, description, iban, title
    }
    private double amount;
    private Account account;
    private String description;
    private int source_account;
    private int target_account;
    private double source_saldo;
    private double targer_saldo;
    private LocalDateTime created_at;
    private int created_by;
    private LocalDate effective_at;
    private int id;
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Transfer()
    {}

    public Transfer(double amount, String description, int source_account, int target_account, double source_saldo, double targer_saldo, LocalDateTime created_at, int created_by, LocalDate effective_at, String state) {
        this.amount = amount;
        this.description = description;
        this.source_account = source_account;
        this.target_account = target_account;
        this.source_saldo = source_saldo;
        this.targer_saldo = targer_saldo;
        this.created_at = created_at;
        this.created_by = created_by;
        this.effective_at = effective_at;
        this.state = state;
    }

    public static List<Transfer> getAll()
    {
        return queryList(Transfer.class,"select * from transfer");
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "amount=" + amount +
                ", description='" + description + '\'' +
                ", source_account=" + source_account +
                ", target_account=" + target_account +
                ", source_saldo=" + source_saldo +
                ", targer_saldo=" + targer_saldo +
                ", created_at=" + created_at +
                ", created_by=" + created_by +
                ", effective_at=" + effective_at +
                ", id=" + id +
                ", state='" + state + '\'' +
                '}';
    }

    public Double getAmount() {
            return  amount;

    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSource_account() {
        return source_account;
    }

    public void setSource_account(int source_account) {
        this.source_account = source_account;
    }

    public int getTarget_account() {
        return target_account;
    }

    public void setTarget_account(int target_account) {
        this.target_account = target_account;
    }

    public double getSource_saldo() {
        return source_saldo;
    }

    public void setSource_saldo(double source_saldo) {
        this.source_saldo = source_saldo;
    }

    public double getTarger_saldo() {
        return targer_saldo;
    }

    public void setTarger_saldo(double targer_saldo) {
        this.targer_saldo = targer_saldo;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public LocalDate getEffective_at() {
        return effective_at;
    }

    public void setEffective_at(LocalDate effective_at) {
        this.effective_at = effective_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public enum State{
        executed("executed"),future("future"),ignored("ignored"),rejected("rejected");

        private String state;
        State(String type) {
            this.state = type;
        }
        public String getType() {
            return state;
        }

    }


    private String iban;
    private String srcIban;

    public String getSrcIban() {
        return srcIban;
    }

    public void setSrcIban(String srcIban) {
        this.srcIban = srcIban;
    }

    private String category;
    private String title;
    private String user;
    private String srcType;
    private String srcSaldo;
    private String trgtType;
    private String trgtSaldo;
    private String srcTitle;
    private int srcId;
    private int categoryId;

    private LocalDate DATUM;

    public LocalDate getDATUM() {
        return DATUM;
    }

    public void setDATUM(LocalDate DATUM) {
        this.DATUM = DATUM;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSrcId() {
        return srcId;
    }

    public void setSrcId(int srcId) {
        this.srcId = srcId;
    }

    public String getSrcTitle() {
        return srcTitle;
    }

    public void setSrcTitle(String srcTitle) {
        this.srcTitle = srcTitle;
    }

    public String getSrcType() {
        return srcType;
    }

    public void setSrcType(String srcType) {
        this.srcType = srcType;
    }

    public String getSrcSaldo() {
        return srcSaldo;
    }

    public void setSrcSaldo(String srcSaldo) {
        this.srcSaldo = srcSaldo;
    }

    public String getTrgtType() {
        return trgtType;
    }

    public void setTrgtType(String trgtType) {
        this.trgtType = trgtType;
    }

    public String getTrgtSaldo() {
        return trgtSaldo;
    }

    public void setTrgtSaldo(String trgtSaldo) {
        this.trgtSaldo = trgtSaldo;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIban() {
        return iban + " - " + getTitle();
    }
    public String getFullSourceAccount(){
        return srcIban + " | " + getSrcTitle() + " | " + getSrcType() + " | " + getSrcSaldo() + " (your account)";
    }
    public String getFullTargetAccount(){
        return iban + " | " + getTitle() + " | " + getTrgtType() + " | " + getTrgtSaldo();
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    protected void mapper(ResultSet resultSet) throws SQLException {

            amount =resultSet.getDouble("transfer.amount");
            description = resultSet.getString("transfer.description");
            source_account=resultSet.getInt("transfer.source_account");
            target_account=resultSet.getInt("transfer.target_account");
            source_saldo =resultSet.getDouble("transfer.source_saldo");
            targer_saldo =resultSet.getDouble("transfer.target_saldo");
            created_by=resultSet.getInt("transfer.created_by");
            created_at=resultSet.getObject("transfer.created_at", LocalDateTime.class);
            effective_at= resultSet.getObject("transfer.effective_at", LocalDate.class);
            state= resultSet.getString("transfer.state");
            id = resultSet.getInt("transfer.id");
            iban = resultSet.getString("TGT.iban");
            category = resultSet.getString("category.name");
            title = resultSet.getString("TGT.title");
            user = resultSet.getString("user.last_name");
            srcIban = resultSet.getString("SRC.iban");
            srcType = resultSet.getString("SRC.type");
            srcSaldo= resultSet.getString("SRC.saldo");
            trgtType= resultSet.getString("TGT.type");
            trgtSaldo= resultSet.getString("TGT.saldo");
            srcTitle= resultSet.getString("SRC.title");
            srcId= resultSet.getInt("SRC.id");
            categoryId = resultSet.getInt("category.id");
            DATUM = resultSet.getObject("DATUM", LocalDate.class);

    }
    public static Transfer getUserById(int id){
        return queryOne(Transfer.class, "select * from user where id=:id",
                new Params("id", id));
    }
    public static Transfer getIbanById(int id) {
        return queryOne(Transfer.class, "select * from account where id=:id",
                new Params("id", id));
    }
    @Override
    public void reload() {
        reload("select * from transfer where id=:id", new Params("id", id));

    }

    public void delete(){
        execute("delete from transfer_category where transfer=:id", new Params("id",id));
        int c = execute("delete from transfer where id=:id", new Params("id", id));
        Assert.isTrue(c == 1, "Something went wrong");
    }


    public boolean save() {
        int c;
        String sql;
        sql="insert into transfer (amount, description,source_account,target_account,source_saldo,target_saldo,created_at,created_by,effective_at,state) " +
                "values (:amount, :description,:source_account,:target_account,:source_saldo,:target_saldo,:created_at,:created_by,:effective_at,:state)";

        c = execute(sql,new Params()
                .add("amount",amount)
                .add("description",description)
                .add("source_account",source_account)
                .add("target_account",target_account)
                .add("source_saldo",source_saldo)
                .add("target_saldo",targer_saldo)
                .add("created_at",created_at)
                .add("created_by",created_by)
                .add("effective_at",effective_at)
                .add("state",state)
        );
        return c==1;

    }
    public static Transfer LastIdTransfer()
    {
        return queryOne(Transfer.class,"SELECT * FROM transfer WHERE id IN (SELECT MAX(id) FROM transfer)");
    }
}
