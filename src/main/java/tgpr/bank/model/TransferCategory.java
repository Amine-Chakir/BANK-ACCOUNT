package tgpr.bank.model;

import tgpr.framework.Model;
import tgpr.framework.Params;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransferCategory extends Model {
    private int category;
    private int transfer;
    private int account;

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getTransfer() {
        return transfer;
    }

    public void setTransfer(int transfer) {
        this.transfer = transfer;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public TransferCategory(int category, int transfer, int account) {
        this.category = category;
        this.transfer = transfer;
        this.account = account;
    }

    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        category = rs.getInt("category");
        transfer = rs.getInt("transfer");
        account = rs.getInt("account");
    }

    @Override
    public  void reload() {
        reload("select * from transfer_category where category=:category", new Params("category", category));
    }


    public void add(int idCategory,int idTransfer,int idAccount) {
        String sql = "insert into transfer_category values (:idTransfer,:idAccount,:idCategory)";
        execute(sql, new Params().add("idCategory", idCategory).add("idTransfer", idTransfer).add("idAccount", idAccount));
    }


    public boolean save(int category, int transfer, int account, int oldCategory){
        int c;
        int tf = getTFCategoryByTransferAndCategory(transfer, oldCategory);
        String sql;

        if (tf == 0){
            sql = "INSERT INTO transfer_category(transfer, account,category) VALUES (:transfer,:account,:category)";
        } else {
            sql = "UPDATE transfer_category SET transfer_category.category=:category WHERE transfer_category.transfer =:transfer AND transfer_category.category=:oldCategory";
        }
        c = execute(sql, new Params()
                .add("category", category)
                .add("transfer", transfer)
                .add("account", account)
                .add("oldCategory", oldCategory));
        return c == 1;
    }
    public static TransferCategory getTFCategoryByCategory(int id)
    {
        return queryOne(TransferCategory.class,"SELECT * from transfer_category\n" +
                "WHERE\n" +
                "category=:id",new Params("id",id));
    }
    public Integer getTFCategoryByTransferAndCategory(int id, int category)
    {
        return queryScalar(Integer.class,"SELECT COUNT(*) from transfer_category\n" +
                "WHERE\n" +
                "transfer_category.transfer=:id AND transfer_category.category=:idC",new Params().add("id",id).add("idC", category));
    }
}
