package tgpr.bank.model;

import tgpr.framework.Model;
import tgpr.framework.Params;

import tgpr.framework.Model;
import tgpr.framework.Params;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public  class Category extends Model {
    private String name;
    private int account;
    private boolean isAccount;
    private Transfer transfer;



    public boolean isAccount() {
        return isAccount;
    }

    public void setAccount(boolean account) {
        isAccount = account;
    }

    public int getIdaccount() {
        return idaccount;
    }

    public void setIdaccount(int idaccount) {
        this.idaccount = idaccount;
    }

    public int idaccount;

    private int id;

    public Category() {
    }
    public Category(String name, int account, boolean isAccount, int id) {
        this.name = name;
        this.account = account;
        this.isAccount = isAccount;
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static   Integer getUses(String name, int idaccount){
        return   queryScalar(Integer.class, "SELECT COUNT(*)\n" +
                "from category,transfer_category\n" +
                "WHERE category.id=transfer_category.category and category.name=:name and transfer_category.account=:idaccount\n" +
                        "group by category.name"
                ,new Params().add("idaccount",idaccount).add("name",name)
                );
    }


    @Override
    protected void mapper(ResultSet resultSet) throws SQLException {
        name = resultSet.getString("name");
        account = resultSet.getInt("account");
        isAccount = resultSet.getBoolean("account");
        id = resultSet.getInt("id");
    }

    @Override
    public void reload() {
        reload("select * from category where name=:name", new Params("name", name));
    }
    public static List<Category> getByaccount(int idaccount){
        return queryList(Category.class, "select * from category where account=:idaccount or account is null order by name",new Params("idaccount",idaccount));
    }


    public static Category getByName(String name,int account) {
        return queryOne(Category.class, "select * from category where name=:name", new Params().add("name", name).add("account", account));
    }

    public static Category getByNameAndId(String name,int idaccount) {
        return queryOne(Category.class, "select * from category where name=:name", new Params().add("name",name).add("account",idaccount));
    }


    public static Category getByName(String name){
        return queryOne(Category.class, "select * from category where name=:name", new Params().add("name",name));

    }

    public Integer getIdtransfer(String name,int account){

        Integer i=getIdCategory(name,account);
        return queryScalar(Integer.class,
                "SELECT COUNT(category.id) FROM transfer_category,category " +
                "WHERE transfer_category.category=category.id and category.id=:id",new Params()
                .add("id",i));

    }


    public boolean delete(String name,int account) {
        int c;
        Integer idc=  getIdCategory(name,account);
        if(getIdtransfer(name,account)!=0){
            c = execute("DELETE FROM `transfer_category` WHERE transfer_category.category=:id", new Params()
                    .add("name", name)
                    .add("id",idc)
                    .add("account", account));

        }
        else {

                c = execute("delete from category where name=:name and account=:account", new Params()
                        .add("name", name)
                        .add("account", account));
        }
        return c == 1;
    }


    public boolean save(String name,int account) {
        int c;
        Category m = getByName(name,account);
        String sql;
        if (m == null)
            sql = "insert into category (name, account) " +
                    "values (:name,:account)";
        else
            sql = "update category set name=:name , account=:account " +
                    "where name=:name and account=:account";
        c = execute(sql, new Params()
                .add("name", name)
                .add("account", account));
        return c == 1;
    }

    public static Integer getIdCategory(String name,int account){
        return queryScalar(Integer.class,"SELECT category.id from category WHERE category.name=:name and category.account=:account;"
                ,new Params().add("name",name).add("account",account));
    }


    public boolean update(String newName,String lastName,int account){
        int c;
        Integer id=  getIdCategory(lastName,account);
         String sql="UPDATE category SET name=:name,account=:account  where id=:id  ";

        c = execute(sql,new Params()
               .add("name", newName)
                .add("id",id)
                .add("account",account));

        return c==1;
    }

    @Override
    public boolean equals(Object o) {
        // s'il s'agit du même objet en mémoire, retourne vrai
        if (this == o) return true;
        // si l'objet à comparer est null ou n'est pas issu de la même classe que l'objet courant, retourne faux
        if (o == null || getClass() != o.getClass()) return false;
        // transtype l'objet reçu en Member
        Category category = (Category) o;
        // retourne vrai si les deux objets ont le même pseudo
        // remarque : cela veut dire que les deux objets sont considérés comme identiques s'ils on le même pseudo
        //            ce qui a du sens car c'est la clef primaire de la table. Attention cependant car cela signifie
        //            que si d'autres attributs sont différents, les objets seront malgré tout considérés égaux.
        return name.equals(category.name);
    }

    @Override
    public int hashCode() {
        // on retourne le hash code du pseudo qui est "unique" puisqu'il correspond à la clef primaire
        return Objects.hash(name);
    }
    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }
    public static List<Category> getAll()
    {
        return queryList(Category.class,"SELECT * FROM category");
    }

    public static Category getIdbyName(String name)
    {
        return queryOne(Category.class,"SELECT * FROM `category` WHERE category.name = :name",new Params("name",name));
    }

    @Override
    public String toString() {
        return name;
    }


}
