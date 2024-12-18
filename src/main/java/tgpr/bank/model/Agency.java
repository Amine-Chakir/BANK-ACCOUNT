package tgpr.bank.model;

import tgpr.framework.Model;
import tgpr.framework.Params;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;

import java.sql.Connection;
import tgpr.framework.Model;
import tgpr.framework.Params;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import java.time.LocalDate;



public class Agency extends Model {
    private String name;
    private int manager;
    private int id;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Agency{" +
                "name='" + name + '\'' +
                '}';
    }

    public Agency() {
    }

    public Agency(String name, int manager, int id) {
        this.name = name;
        this.manager = manager;
        this.id = id;
    }
    public  Agency(String name)
    {
        Agency result = queryOne(Agency.class, "select * from agency where name=:name", new Params("name", name));
        this.name = name;
        this.id = result.id;
        this.manager = result.manager;

    }
    public void setName(String name) {
        this.name = name;
    }

    public int getManager() {
        return manager;
    }

    public void setManager(int manager) {
        this.manager = manager;
    }

    public int getId() {
        return id;
    }

    public Agency getByName(String agency_name)
    {
        return queryOne(Agency.class, "select * from agency where name=:name", new Params("name", agency_name));
    }
    public Agency getById(int agence_id)
    {
        return queryOne(Agency.class, "select * from agency where id=:id", new Params("id", agence_id));
    }

    public void setId(int id) {
        this.id = id;
    }
    protected void mapper(ResultSet resultSet) throws SQLException {
        name = resultSet.getString("name");
        manager = resultSet.getInt("manager");
        id = resultSet.getInt("id");
    }


    public boolean delete(String name) {
        int b = execute("delete from agency where name=:name ", new Params()
                .add("name",name));
        return b == 1;
    }


    public boolean save(String name) {
        int b;
        Agency n = getByName(name);
        String sql;
        if (n == null)
            sql = "insert into category (name) " +
                    "values (:name)";
        else
            sql = "update category set name=:name  " +
                    "where id=:id ";
        b = execute(sql, new Params()
                .add("name", name));
        return b == 1;
    }

    public Integer getIdAgency(String name){
        return queryScalar(Integer.class,"SELECT agency.id from agency WHERE agency.name=:name ;"
                ,new Params().add("name",name));
    }



    public boolean update(String nvName,String name){
        int b;
        Integer id=  getIdAgency(name);
        String sql="UPDATE agency SET name=:name where id=:id  ";


        b= execute(sql,new Params()
                .add("name", nvName)
                .add("id",id));

        return b==1;

    }


    @Override
    public void reload() {
        reload("select * from agency where id= id", new Params("id", id));

    }
    public static List<Agency> getAll() {
        return queryList(Agency.class, "select * from agency order by id");
    }

    public  Agency getByPseudo(String pseudo) {
        return queryOne(Agency.class, "select * from agency where id=:id", new Params("id", this.getId()));
    }
    public List<User> getAllMembers()
    {
        return queryList(User.class,"select * from user where agency = :agency_id", new Params("agency_id", this.getId()));
    }
    public List<User> getFilteredMember(String textFilter)
    {   textFilter = '%' + textFilter + '%';
        Params params = new Params("filter", textFilter);

        params.add("agency_id",this.getId());
        return queryList(User.class,"select * from user where agency = :agency_id and (first_name like :filter or last_name like :filter or birth_date like :filter or email like :filter ) ", params);
    }


}

